package com.google.gson.internal.tests; // assuming a package "com.google.gson.internal.tests" exists for testing purposes

import static com.google.gson.internal.bind.util.ISO8601Utils.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonToken;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader_ParseAnnoyingDatesWithNonStandardFormatTest;

public class DefaultDateTypeAdapterTest {

  private static final DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
  private static final DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
  private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(enUsFormat, adapter.enUsFormat);
    assertEquals(localFormat, adapter.localFormat);
  }

  @Test
  public void testDatePatternConstructor() {
    String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals(Date.class, adapter.dateType);
    assertNotSame(enUsFormat, adapter.enUsFormat);
    assertNotSame(localFormat, adapter.localFormat);
    assertEquals(datePattern, adapter.enUsFormat.toPattern());
    assertEquals(datePattern, adapter.localFormat.toPattern());
  }

  @Test
  public void testDateStyleConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT);
    assertEquals(Date.class, adapter.dateType);
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US), adapter.enUsFormat);
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US), adapter.localFormat);
  }

  @Test
  public void testDateStyleConstructorWithLocale() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, Locale.CHINESE);
    assertEquals(Date.class, adapter.dateType);
    Locale locale = Locale.CHINESE;
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT, locale), adapter.enUsFormat);
    assertEquals(DateFormat.getDateInstance(DateFormat.SHORT, locale), adapter.localFormat);
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(writer, date);
    assertEquals("\"2023-03-16 10:20:30.456+00:00\"", writer.toString());
  }

  @Test
  public void testReadDate() throws IOException, ParseException {
    String dateString = "\"2023-03-16 10:20:30.456+00:00\"";
    JsonReader reader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(reader);
    assertEquals(dateString, enUsFormat.format(date));
  }

  @Test
  public void testReadDateWithInvalidFormat() throws IOException, JsonSyntaxException {
    String dateString = "\"2023-03-16 10:20:30\""; // missing timezone information
    JsonReader reader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.read(reader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // expected
    }
  }

  @Test
  public void testReadDateWithNullValue() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("null"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(reader);
    assertNull(date);
  }

  @Test
  public void testReadTimestamp() throws IOException, ParseException {
    String timestampString = "\"2023-03-16 10:20:30.456+00:00\"";
    JsonReader reader = new JsonReader(new StringReader(timestampString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = adapter.read(reader);
    assertEquals(timestampString, enUsFormat.format(timestamp));
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    String sqlDateString = "\"2023-03-16\"";
    JsonReader reader = new JsonReader(new StringReader(sqlDateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = adapter.read(reader);
    assertEquals(sqlDateString, localFormat.format(sqlDate));
  }

  @Test
  public void testReadUnknownType() throws IOException, JsonParseException {
    String unknownTypeString = "\"2023-03-16T10:20:30.456+00:00\""; // different string representation than Date, Timestamp, or java.sql.Date
    JsonReader reader = new JsonReader(new StringReader(unknownTypeString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // expected
    }
  }

  @Test
  public void testWriteCustomDateFormat() throws IOException {
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, localFormat);
    adapter.write(writer, date);
    assertEquals("\"2023-03-16 10:20:30.456+00:00\"", writer.toString());
  }

  @Test
  public void testReadCustomDateFormatDate() throws IOException, ParseException {
    String dateString = "\"2023-03-16 10:20:30.456+00:00\"";
    JsonReader reader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, localFormat);
    Date date = adapter.read(reader);
    assertEquals(dateString, localFormat.format(date));
  }

  @Test
  public void testReadCustomDateFormatTimestamp() throws IOException, ParseException {
    String timestampString = "\"2023-03-16 10:20:30.456+00:00\"";
    JsonReader reader = new JsonReader(new StringReader(timestampString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class, localFormat);
    Timestamp timestamp = adapter.read(reader);
    assertEquals(timestampString, localFormat.format(timestamp));
  }

  @Test
  public void testReadCustomDateFormatSqlDate() throws IOException, ParseException {
    String sqlDateString = "\"2023-03-16\"";
    JsonReader reader = new JsonReader(new StringReader(sqlDateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class, localFormat);
    java.sql.Date sqlDate = adapter.read(reader);
    assertEquals(sqlDateString, localFormat.format(sqlDate));
  }

  @Test
  public void testParseWithNegativeTimeZone() throws ParseException {
    String dateString = "\"2023-03-16 10:20:30.456-04:00\"";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    Date date = DefaultDateTypeAdapter.parse(dateString, sf);
    assertEquals("Thu Mar 16 14:20:30 GMT-04:00 2023", sf.format(date));
  }

  @Test
  public void testParseWithNullDateFormat() {
    String dateString = "\"2023-03-16 10:20:30.456+00:00\"";
    try {
      DefaultDateTypeAdapter.parse(dateString, null);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // expected
    }
  }

  @Test
  public void testParseWithInvalidFormat() {
    String dateString = "\"2023-03-16T10:20:30.456+00:00\"";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    try {
      DefaultDateTypeAdapter.parse(dateString, sf);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // expected
    }
  }

  @Test
  public void testParseWithParseException() {
    String dateString = "\"2023-03-16\"";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      DefaultDateTypeAdapter.parse(dateString, sf);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // expected
    }
  }

  @Test
  public void testParseWithNullDate() {
    String dateString = "null";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    try {
      DefaultDateTypeAdapter.parse(dateString, sf);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // expected
    }
  }

  @Test
  public void testParseWithNullInputString() {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    try {
      DefaultDateTypeAdapter.parse(null, sf);
      fail("Expected JsonIOException");
    } catch (JsonIOException e) {
      // expected
    }
  }

  @Test
  public void testParseWithIOException() {
    String dateString = "\"2023-03-16 10:20:30.456+00:00\"";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z") {
      @Override
      public Date parse(String source, ParsePosition pos) throws ParseException, IOException {
        throw new IOException("IOException for testing");
      }
    };
    try {
      DefaultDateTypeAdapter.parse(dateString, sf);
      fail("Expected JsonIOException");
    } catch (JsonIOException e) {
      // expected
    }
  }

  @Test
  public void testParseWithJsonSyntaxException() {
    String jsonString = "{\"date\": \"2023-03-16T10:20:30.456+00:00\"}";
    JsonReader reader = JsonReader_ParseAnnoyingDatesWithNonStandardFormatTest.createReader(jsonString);
    try {
      DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
      adapter.read(reader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // expected
    }
  }
}