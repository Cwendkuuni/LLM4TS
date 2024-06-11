package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @Test
  public void testDefaultDateTypeAdapterConstructor_DateType() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructor_StringDatePattern() {
    String datePattern = "dd-MM-yyyy HH:mm:ss";
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(new SimpleDateFormat(datePattern, Locale.US), dateAdapter.enUsFormat);
    assertEquals(new SimpleDateFormat(datePattern), dateAdapter.localFormat);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructor_intDateStyle_intTimeStyle() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(DateFormat.getDateInstance(Calendar.SHORT, Locale.US), dateAdapter.enUsFormat);
    assertEquals(DateFormat.getDateInstance(Calendar.SHORT), dateAdapter.localFormat);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapterConstructor_InvalidDateType() {
    new DefaultDateTypeAdapter(String.class);
  }

  @Test
  public void testDefaultDateTypeAdapterWrite() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    String expected = dateAdapter.enUsFormat.format(date);

    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    assertEquals(expected, System.out.getBytes().toString());
  }

  @Test
  public void testDefaultDateTypeAdapterRead_NullInput() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));

    assertNull(dateAdapter.read(jsonReader));
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapterRead_InvalidDateFormat() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("invalid_date_format"));

    dateAdapter.read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapterRead_ValidDateFormat() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String validDateFormat = "2022-12-31";
    JsonReader jsonReader = new JsonReader(new StringReader(validDateFormat));

    Date actual = dateAdapter.read(jsonReader);
    Date expected = new Date(1671139200000L); // Directly converting String to Date using SimpleDateFormat

    assertEquals(expected, actual);
  }

  @Test
  public void testDefaultDateTypeAdapterRead_ValidTimestampFormat() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    String validTimestampFormat = "2022-12-31T12:34:56.789Z";
    JsonReader jsonReader = new JsonReader(new StringReader(validTimestampFormat));

    Timestamp actual = (Timestamp) dateAdapter.read(jsonReader);
    Timestamp expected = new Timestamp(1671142496789L); // Directly converting String to Timestamp using SimpleDateFormat

    assertEquals(expected, actual);
  }

  @Test
  public void testDefaultDateTypeAdapterRead_ValidDateFormat_JavaSqlDate() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    String validDateFormat = "2022-12-31";
    JsonReader jsonReader = new JsonReader(new StringReader(validDateFormat));

    java.sql.Date actual = (java.sql.Date) dateAdapter.read(jsonReader);
    java.sql.Date expected = new java.sql.Date(1671139200000L); // Directly converting String to java.sql.Date using SimpleDateFormat

    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testDefaultDateTypeAdapterRead_InvalidDateType() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT));
    JsonReader jsonReader = new JsonReader(new StringReader(""));

    dateAdapter.read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapterDeserializeToDate() throws ParseException {
    String validDateFormat = "2022-12-31";
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date actual = dateAdapter.deserializeToDate(validDateFormat);
    Date expected = new Date(1671139200000L); // Directly converting String to Date using SimpleDateFormat

    assertEquals(expected, actual);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterDeserializeToDate_InvalidFormat() throws ParseException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    String invalidFormat = "invalid_date_format";

    dateAdapter.deserializeToDate(invalidFormat);
  }

  @Test
  public void testDefaultDateTypeAdapterToString() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"));

    String expected = SIMPLE_NAME + "(Date.class, java.text.SimpleDateFormat[dd-MM-yyyy hh:mm:ss], java.text.SimpleDateFormat[dd-MM-yyyy hh:mm:ss])";
    String actual = dateAdapter.toString();

    assertEquals(expected, actual);
  }
}