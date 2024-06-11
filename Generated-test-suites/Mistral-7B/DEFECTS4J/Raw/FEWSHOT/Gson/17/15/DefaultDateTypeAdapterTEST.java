package com.google.gson.internal.test; // Add this package declaration if not present in the test file

import static com.google.gson.stream.JsonReader.JsonToken.NULL;
import static com.google.gson.stream.JsonReader.JsonToken.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;

public class DefaultDateTypeAdapterTest {

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  private static final String DATE_STRING = "2022-12-31T12:34:56.789Z";
  private static final String DATE_STRING_US = "12/31/2022 12:34:56.789Z";
  private static final String DATE_STRING_LOCAL = "31/12/2022 12:34:56.789";
  private static final String ISODateString = "2022-12-31T12:34:56.789Z";
  private static final String ISODateStringEmpty = "";
  private static final String EMPTY_STRING = "";
  private static final String NULL_STRING = "null";

  private static final Class<Date> DATE_CLASS = Date.class;
  private static final Class<Timestamp> TIMESTAMP_CLASS = Timestamp.class;
  private static final Class<java.sql.Date> SQL_DATE_CLASS = java.sql.Date.class;

  private DefaultDateTypeAdapter<Date> dateTypeAdapter;
  private DefaultDateTypeAdapter<Timestamp> timestampTypeAdapter;
  private DefaultDateTypeAdapter<java.sql.Date> sqlDateTypeAdapter;

  @org.junit.Before
  public void setup() {
    dateTypeAdapter = new DefaultDateTypeAdapter<>(DATE_CLASS);
    timestampTypeAdapter = new DefaultDateTypeAdapter<>(Timestamp.class);
    sqlDateTypeAdapter = new DefaultDateTypeAdapter<>(SQL_DATE_CLASS);
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.bind.util.SimpleDateFormat@5d51858)", adapter.toString());
  }

  @Test
  public void testDateConstructor() {
    DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(DATE_CLASS);
    assertEquals("DefaultDateTypeAdapter(class com.google.gson.internal.bind.util.SimpleDateFormat, class java.util.Date)", adapter.toString());
  }

  @Test
  public void testDatePatternConstructor() {
    DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(DATE_CLASS, DEFAULT_DATE_FORMAT);
    assertEquals("DefaultDateTypeAdapter(class com.google.gson.internal.bind.util.SimpleDateFormat@5d51858, yyyy-MM-dd'T'HH:mm:ss.SSSXXX)", adapter.toString());
  }

  @Test
  public void testDateStyleConstructor() {
    DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class, Date.ABBREVIATED_DATE_FORMAT);
    assertEquals("DefaultDateTypeAdapter(class java.util.Date, class java.text.DateFormat[type=DATE, patterns=[S, d M, d-M-yy, d-M-yy h:mm aa, h:mm a, h:mm, d-M-yy h:mm, d-M-yy h:mm:ss a, d-M-yy h:mm:ss, d-M-yy h:mm:ss.SSS, d-M-yy h:mm:ss.SSS Z, d-M-yy h:mm:ss.SSSZ, M/d/yy h:mm a, M/d/yy h:mm, M/d/yy h:mm a, M/d/yy h:mm, M/d/yy h:mm:ss a, M/d/yy h:mm:ss, M/d/yy h:mm:ss.SSS, M/d/yy h:mm:ss.SSS Z, M/d/yy h:mm:ss.SSSZ, h:mm a, h:mm, h:mm a, h:mm, h:mm:ss a, h:mm:ss, h:mm:ss.SSS, h:mm:ss.SSS Z, h:mm:ss.SSSZ, S, M/d/yy h:mm a, M/d/yy h:mm, M/d/yy h:mm a, M/d/yy h:mm, M/d/yy h:mm:ss a, M/d/yy h:mm:ss, M/d/yy h:mm:ss.SSS, M/d/yy h:mm:ss.SSS Z, M/d/yy h:mm:ss.SSSZ]", adapter.toString());
  }

  @Test
  public void testTimestampConstructor() {
    DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
    assertEquals("DefaultDateTypeAdapter(class java.sql.Timestamp)", adapter.toString());
  }

  @Test
  public void testDateStylesConstructor() {
    DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class, Date.SHORT, Date.SHORT);
    assertEquals("DefaultDateTypeAdapter(class java.util.Date, class java.text.DateFormat[type=DATE, style=SHORT, patterns=[S, d-M-yy, d-M, Me]], class java.text.DateFormat[type=DATE, style=SHORT, patterns=[S, d-M-yy, d-M]])", adapter.toString());
  }

  @Test
  public void testWrite() throws IOException {
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);

    Date date = new Date(0);
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    assertEquals(DATE_STRING_US, writer.toString());

    jsonWriter = new JsonWriter(new StringWriter());
    dateTypeAdapter.write(jsonWriter, new Timestamp(0));
    jsonWriter.flush();

    assertEquals(DATE_STRING, writer.toString());

    jsonWriter = new JsonWriter(new StringWriter());
    dateTypeAdapter.write(jsonWriter, new java.sql.Date(0));
    jsonWriter.flush();

    assertEquals(DATE_STRING_LOCAL, writer.toString());
  }

  @Test
  public void testReadDate() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(DATE_STRING));

    Date date = dateTypeAdapter.read(jsonReader);
    assertEquals(new Date(0), date);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(ISODateString));

    Timestamp timestamp = timestampTypeAdapter.read(jsonReader);
    assertEquals(new Timestamp(0), timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(DATE_STRING_LOCAL));

    java.sql.Date sqlDate = sqlDateTypeAdapter.read(jsonReader);
    assertEquals(new java.sql.Date(0), sqlDate);
  }

  @Test
  public void testReadNull() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(NULL_STRING));

    assertNull(dateTypeAdapter.read(jsonReader));
  }

  @Test
  public void testReadEmpty() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(EMPTY_STRING));

    assertNull(dateTypeAdapter.read(jsonReader));
  }

  @Test(expected = JsonIOException.class)
  public void testReadInvalidDateString() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("InvalidDateString"));

    dateTypeAdapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidISODateString() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(ISODateStringEmpty));

    dateTypeAdapter.read(jsonReader);
  }
}