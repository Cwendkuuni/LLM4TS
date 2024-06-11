package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateType() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(java.util.Date)", dateTypeAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithStringDatePattern() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    assertEquals("DefaultDateTypeAdapter(java.util.SimpleDateFormat[yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'])", dateTypeAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithIntDateStyles() {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals("DefaultDateTypeAdapter(java.util.Date, int, int)", dateTypeAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteNullValue() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, null);
    jsonWriter.flush();
    assertEquals("null", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteValidDateValue() throws IOException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = dateFormat.parse("2022-12-31 23:59:59.999999");
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();
    assertEquals("\"2022-12-31 23:59:59.999999\"", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteInvalidDateFormatValue() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, "invalid date format");
    jsonWriter.flush();
    assertEquals("{\"type\":\"json.parse.exception.NumberFormatError\", \"message\":\"For input string: \"invalid date format\"\"}", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadNullValue() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    assertNull(dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapterReadEmptyStringValue() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    assertNull(dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapterReadValidDateFormatValue() throws IOException {
    Gson gson = new Gson();
    JsonElement jsonElement = new JsonParser().parse("{\"date\":\"2022-12-31 23:59:59.999999\"}");
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    Date date = dateTypeAdapter.read(new JsonReader(new StringReader(gson.toJson(jsonElement))));
    assertEquals("Fri Dec 31 23:59:59 GMT+00:00 2022", date.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidDateFormatValue() {
    Gson gson = new Gson();
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"date\":\"invalid date format\"}", Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidDateFormatValueWithCustomExceptionMessage() {
    Gson gson = new GsonBuilder().setDefaultExceptionMessage("Custom message").create();
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"date\":\"invalid date format\"}", Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValue() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalid\":\"json\"}"));
    assertThrows(JsonSyntaxException.class, () -> dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithCustomExceptionMessage() {
    Gson gson = new GsonBuilder().setDefaultExceptionMessage("Custom message").create();
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalid\":\"json\"}"));
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithNullDateType() {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"2022-12-31 23:59:59.999999\"}"));
    assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(null).read(jsonReader));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithNullDateTypeThrowsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"2022-12-31 23:59:59.999999\"}"));
    new DefaultDateTypeAdapter(null).read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapterReadValidJsonValueWithTimestampType() throws IOException {
    Gson gson = new Gson();
    JsonElement jsonElement = new JsonParser().parse("{\"date\":\"2022-12-31 23:59:59.999999\"}");
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = (Timestamp) dateTypeAdapter.read(new JsonReader(new StringReader(gson.toJson(jsonElement))));
    assertEquals("Fri Dec 31 23:59:59 GMT+00:00 2022", timestamp.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadValidJsonValueWithSqlDateType() throws IOException {
    Gson gson = new Gson();
    JsonElement jsonElement = new JsonParser().parse("{\"date\":\"2022-12-31\"}");
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = (java.sql.Date) dateTypeAdapter.read(new JsonReader(new StringReader(gson.toJson(jsonElement))));
    assertEquals("Fri Dec 31 00:00:00 GMT+00:00 2022", sqlDate.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithTimestampType() throws IOException {
    Gson gson = new Gson();
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"date\":\"invalid date format\"}", Timestamp.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithSqlDateType() {
    Gson gson = new Gson();
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"date\":\"invalid date format\"}", java.sql.Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithDateTypeThrowsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"invalid date format\"}"));
    assertThrows(JsonSyntaxException.class, () -> new DefaultDateTypeAdapter(Date.class).read(jsonReader));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadInvalidJsonValueWithDateTypeThrowsExceptionDirectly() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"invalid date format\"}"));
    new DefaultDateTypeAdapter(Date.class).read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapterReadJsonValueWithInvalidDateTypeThrowsException() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":2}"));
    assertThrows(JsonSyntaxException.class, () -> new DefaultDateTypeAdapter(Date.class).read(jsonReader));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadJsonValueWithInvalidDateTypeThrowsExceptionDirectly() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":2}"));
    new DefaultDateTypeAdapter(Date.class).read(jsonReader);
  }
}