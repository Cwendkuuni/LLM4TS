package com.google.gson.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateClass() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithStringDatePattern() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter("DefaultDateTypeAdapter", "dd-MM-yyyy HH:mm:ss");
    assertEquals(Date.class, dateAdapter.dateType);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    assertEquals(dateFormat, dateAdapter.enUsFormat);
    assertEquals(dateFormat, dateAdapter.localFormat);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterWriteMethod() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = new Date();
    dateAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    String expectedJson = this.getExpectedJsonFromResources("date_written.json");
    assertJsonEqualsIgnoringWhitespace(expectedJson, System.out.getBuffer().toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithValidDateString() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31T23:59:59Z"));
    Date date = dateAdapter.read(jsonReader);
    assertEquals(new Date(1671131199000L), date);
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithInvalidDateStringFormat() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("invalidDateString"));
    try {
      dateAdapter.read(jsonReader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithNullJsonElement() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("null"));
    Date date = dateAdapter.read(jsonReader);
    assertNull(date);
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithEmptyJsonElement() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(""));
    Date date = dateAdapter.read(jsonReader);
    assertNull(date);
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithTimestampObject() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31T23:59:59.000Z"));
    Timestamp timestamp = (Timestamp) dateAdapter.read(jsonReader);
    assertEquals(new Timestamp(1671131199000L), timestamp);
  }

  @Test
  public void testDefaultDateTypeAdapterReadMethodWithSqlDateObject() throws IOException, SQLException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31"));
    java.sql.Date sqlDate = (java.sql.Date) dateAdapter.read(jsonReader);
    assertEquals(new java.sql.Date(1671131199000L), sqlDate);
  }

  private void assertJsonEqualsIgnoringWhitespace(String expectedJson, String actualJson) {
    String expectedJsonTrimmed = expectedJson.replaceAll("\\s+", "").replaceAll("\\r\\n", "");
    String actualJsonTrimmed = actualJson.replaceAll("\\s+", "").replaceAll("\\r\\n", "");
    assertEquals(expectedJsonTrimmed, actualJsonTrimmed);
  }

  private String getExpectedJsonFromResources(String fileName) {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.fromJson(getClass().getResourceAsStream(fileName), JsonElement.class);
    return jsonElement.getAsJson().toString();
  }
}