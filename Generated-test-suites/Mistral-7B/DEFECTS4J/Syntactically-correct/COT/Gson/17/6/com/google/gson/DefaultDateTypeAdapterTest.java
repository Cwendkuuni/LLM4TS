package com.google.gson.tester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
  public void testDefaultDateTypeAdapterConstructorWithDateType() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateTypeAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDatePattern() throws IOException {
    String datePattern = "dd-MM-yyyy HH:mm:ss";
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals(Date.class, dateTypeAdapter.dateType);
    assertEquals(new SimpleDateFormat(datePattern, Locale.US), dateTypeAdapter.enUsFormat);
    assertEquals(new SimpleDateFormat(datePattern), dateTypeAdapter.localFormat);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyle() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, dateTypeAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithInts() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, dateTypeAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithInvalidDateType() throws IOException {
    Class<?> invalidDateType = Integer.class;
    assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(invalidDateType, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)));
  }

  @Test
  public void testDefaultDateTypeAdapterWriteNull() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, null);
    jsonWriter.close();
    assertEquals("null", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteDate() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = new Date();
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.close();
    assertEquals(dateTypeAdapter.toString() + "(\"2023-03-15 14:30:12\")", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteTimestamp() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    dateTypeAdapter.write(jsonWriter, timestamp);
    jsonWriter.close();
    assertEquals(dateTypeAdapter.toString() + "(\"2023-03-15 14:30:12.513\")", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteJavaSqlDate() throws IOException {
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.close();
    assertEquals(dateTypeAdapter.toString() + "(\"2023-03-15\")", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadNull() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertNull(dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapterReadDate() throws IOException {
    String jsonString = "{\"date\":\"2023-03-15 14:30:12\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    Date date = gson.fromJson(jsonElement, Date.class);
    assertEquals(new Date(System.currentTimeMillis()), date);
  }

  @Test
  public void testDefaultDateTypeAdapterReadTimestamp() throws IOException, SQLException {
    String jsonString = "{\"timestamp\":\"2023-03-15 14:30:12.513\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class))
            .registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class))
            .create();
    Timestamp timestamp = gson.fromJson(jsonElement, Timestamp.class);
    assertEquals(new Timestamp(System.currentTimeMillis()), timestamp);
  }

  @Test
  public void testDefaultDateTypeAdapterReadJavaSqlDate() throws IOException {
    String jsonString = "{\"date\":\"2023-03-15\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    java.sql.Date date = gson.fromJson(jsonElement, java.sql.Date.class);
    assertEquals(new java.sql.Date(System.currentTimeMillis()), date);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadInvalidJsonString() throws IOException {
    String jsonString = "{\"invalid\":\"123\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    dateTypeAdapter.read(jsonReader);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapterParseInvalidDateFormat() throws IOException, ParseException {
    String s = "dd-MM-yyyy HH:mm:ssZ";
    DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    dateTypeAdapter.deserializeToDate(s);
  }
}