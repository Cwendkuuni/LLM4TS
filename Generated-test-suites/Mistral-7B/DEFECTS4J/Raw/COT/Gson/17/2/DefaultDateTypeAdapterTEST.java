package com.google.gson.tests;

import static org.junit.Assert.*;

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
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonWriter;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_constructor_Date() {
    Class<Date> dateType = Date.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_String() {
    String datePattern = "dd-MM-yyyy HH:mm:ss";
    Class<Date> dateType = Date.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType, datePattern);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(new SimpleDateFormat(datePattern, Locale.US), adapter.enUsFormat);
    assertEquals(new SimpleDateFormat(datePattern), adapter.localFormat);
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_int() {
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;
    Class<Date> dateType = Date.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), adapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle), adapter.localFormat);
  }

  @Test
  public void testDefaultDateTypeAdapter_constructor_int_int() {
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;
    Class<Date> dateType = Date.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), adapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle), adapter.localFormat);
  }

  @Test
  public void testWrite_NullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, null);
    writer.flush();
    assertEquals("null", writer.toString());
  }

  @Test
  public void testWrite() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date();
    adapter.write(writer, date);
    writer.flush();
    String json = writer.toString();
    assertTrue(json.matches("\"[^\"]+\"")); // Matches any non-empty JSON string
  }

  @Test
  public void testRead_JsonNull() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("null"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(reader);
    assertNull(date);
  }

  @Test
  public void testRead_EmptyString() throws IOException {
    JsonReader reader = new JsonReader(new StringReader(""));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.read(reader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testRead_InvalidFormat() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("xxxx"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.read(reader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testRead_ValidFormat_USLocale() throws IOException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    String jsonDate = dateFormat.format(new Date());
    JsonReader reader = new JsonReader(new StringReader(jsonDate));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(reader);
    assertEquals(dateFormat.parse(jsonDate), date);
  }

  @Test
  public void testRead_ValidFormat_LocalLocale() throws IOException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    String jsonDate = dateFormat.format(new Date());
    JsonReader reader = new JsonReader(new StringReader(jsonDate));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
    Date date = adapter.read(reader);
    assertEquals(dateFormat.parse(jsonDate), date);
  }

  @Test
  public void testRead_ValidFormat_ISOFormat() throws IOException {
    String jsonDate = "2022-06-18T12:34:56.789Z";
    JsonReader reader = new JsonReader(new StringReader(jsonDate));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(reader);
    assertEquals(ISO8601Utils.parse(jsonDate, new ParsePosition(0)), date);
  }

  @Test
  public void testRead_ValidFormat_Timestamp() throws IOException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    String jsonDate = dateFormat.format(new Timestamp(System.currentTimeMillis()));
    JsonReader reader = new JsonReader(new StringReader(jsonDate));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = (Timestamp) adapter.read(reader);
    assertEquals(dateFormat.parse(jsonDate), new Date(timestamp.getTime()));
  }

  @Test
  public void testRead_ValidFormat_java_sql_Date() throws IOException, SQLException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    String jsonDate = dateFormat.format(new java.sql.Date(System.currentTimeMillis()));
    JsonReader reader = new JsonReader(new StringReader(jsonDate));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = (java.sql.Date) adapter.read(reader);
    assertEquals(dateFormat.parse(jsonDate), new Date(sqlDate.getTime()));
  }

  @Test(expected = AssertionError.class)
  public void testRead_InvalidDateType() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-06-18T12:34:56.789Z"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Integer.class);
    adapter.read(reader);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat)", adapter.toString());
  }
}