package com.google.gson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.Date;
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
  public void testDefaultDateTypeAdapter_constructor_Date() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(Date)", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_String() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter("DefaultDateTypeAdapter", new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yyyy"));
    assertEquals("DefaultDateTypeAdapter(dd-MM-yyyy)", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_int() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DEFAULT, DateFormat.DEFAULT);
    assertEquals("DefaultDateTypeAdapter(Date, DEFAULT, DEFAULT)", dateAdapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_int_int() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY);
    assertEquals("DefaultDateTypeAdapter(Date, DATE, HOUR_OF_DAY)", dateAdapter.toString());
  }

  @Test
  public void testWrite_Date() throws IOException {
    Date date = new Date();
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String expectedJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateAdapter.write(jsonWriter, date);
    jsonWriter.flush();
    assertEquals(expectedJson, System.out.getStatusCode());
  }

  @Test
  public void testWrite_null() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateAdapter.write(jsonWriter, null);
    jsonWriter.flush();
    assertNull(System.out.getStatusCode());
  }

  @Test
  public void testRead_Date() throws IOException {
    String json = "\"2022-12-31T23:59:59.999Z\"";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Date result = dateAdapter.read(jsonReader);
    assertEquals(new Date(System.currentTimeMillis()), result);
  }

  @Test
  public void testRead_null() throws IOException {
    String json = "null";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidString() throws IOException {
    String json = "InvalidDate";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    dateAdapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testRead_EmptyString() throws IOException {
    String json = "";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    dateAdapter.read(jsonReader);
  }

  @Test
  public void testRead_Timestamp() throws IOException {
    String json = "\"2022-12-31T23:59:59.999Z\"";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp result = dateAdapter.read(jsonReader);
    assertEquals(new Timestamp(System.currentTimeMillis()), result);
  }

  @Test
  public void testRead_java_sql_Date() throws IOException {
    String json = "\"2022-12-31\"";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date result = (java.sql.Date) dateAdapter.read(jsonReader);
    assertEquals(new java.sql.Date(System.currentTimeMillis()), result);
  }

  @Test(expected = AssertionError.class)
  public void testRead_UnexpectedDateType() throws IOException {
    String json = "\"2022-12-31\"";
    JsonElement jsonElement = new JsonParser().parse(json);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(String.class, new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yyyy"));
    dateAdapter.read(jsonReader);
  }

  @Test
  public void testDeserializeToDate_ValidDateFormat() throws ParseException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-12-31";
    Date result = dateAdapter.deserializetoDate(dateString);
    assertEquals(new Date(System.currentTimeMillis()), result);
  }

  @Test(expected = ParseException.class)
  public void testDeserializeToDate_InvalidDateFormat() throws ParseException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "InvalidDateFormat";
    dateAdapter.deserializetoDate(dateString);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_EmptyString() throws ParseException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "";
    dateAdapter.deserializetoDate(dateString);
  }
}