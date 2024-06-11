package com.google.gson.internal.bind.util; // Import ISO8601Utils for testing

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonToken;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonParser;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private DateFormat enUsFormat;
  private DateFormat localFormat;

  @Before
  public void setUp() {
    TimeZone utc = TimeZone.getTimeZone("UTC");
    enUsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    enUsFormat.setTimeZone(utc);
    localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    localFormat.setTimeZone(utc);

    dateAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
  }

  @Test
  public void testDefaultConstructor() {
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(enUsFormat, dateAdapter.enUsFormat);
    assertEquals(localFormat, dateAdapter.localFormat);
  }

  @Test
  public void testStringConstructor() {
    String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(new SimpleDateFormat(datePattern, Locale.US), dateAdapter.enUsFormat);
    assertEquals(new SimpleDateFormat(datePattern), dateAdapter.localFormat);
  }

  @Test
  public void testDateStyleConstructor() {
    int dateStyle = DateFormat.FULL;
    int timeStyle = DateFormat.SHORT;
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), dateAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle), dateAdapter.localFormat);
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter jsonWriter = mock(JsonWriter.class);
    Date date = new Date();
    dateAdapter.write(jsonWriter, date);
    verify(jsonWriter).value("" + enUsFormat.format(date) + "");
  }

  @Test
  public void testReadDate() throws IOException, ParseException {
    String jsonDate = "\"2012-03-30T14:31:12.000Z\"";
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextString()).thenReturn(jsonDate);
    Date date = dateAdapter.read(jsonReader);
    assertEquals(new Date(1332556772000L), date);
  }

  @Test
  public void testReadTimestamp() throws IOException, ParseException {
    String jsonTimestamp = "\"2012-03-30T14:31:12.000Z\"";
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextString()).thenReturn(jsonTimestamp);
    Timestamp timestamp = dateAdapter.read(jsonReader);
    assertEquals(new Timestamp(1332556772000L), timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException, ParseException {
    String jsonSqlDate = "\"2012-03-30\"";
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextString()).thenReturn(jsonSqlDate);
    java.sql.Date sqlDate = (java.sql.Date) dateAdapter.read(jsonReader);
    assertEquals(new java.sql.Date(1332556800000L), sqlDate);
  }

  @Test
  public void testReadInvalidJsonString() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("invalid");
    assertThrows(JsonParseException.class, () -> dateAdapter.read(jsonReader));
  }

  @Test
  public void testReadNullJsonString() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(null);
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test
  public void testDeserializeToDate() throws ParseException {
    String dateString = "2012-03-30T14:31:12.000Z";
    Date date = dateAdapter.deserializeToDate(dateString);
    assertEquals(new Date(1332556772000L), date);
  }

  @Test
  public void testDeserializeToTimestamp() throws ParseException {
    String timestampString = "2012-03-30T14:31:12.000Z";
    Date timestamp = dateAdapter.deserializeToDate(timestampString);
    assertEquals(new Timestamp(1332556772000L), new Timestamp(timestamp.getTime()));
  }

  @Test
  public void testDeserializeToString() {
    String dateString = "2012-03-30T14:31:12.000Z";
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals(expected, dateAdapter.toString());
  }

  @Test
  public void testReadInvalidJsonWithISO8601Utils() throws IOException, ParseException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextString()).thenReturn("2021-12-31T23:59:60.000+01:00");
    JsonParser parser = new JsonParser();
    assertThrows(JsonIOException.class, () -> parser.parse(jsonReader, new JsonObject()));
  }
}