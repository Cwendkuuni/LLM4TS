package com.google.gson.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test writing a Date
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date();
    adapter.write(writer, date);
    writer.flush();
    String jsonDate = System.out.getBuffer().toString();
    assertEquals("Mon Aug 23 16:58:37 IST 2021", jsonDate.trim());

    // Test reading a Date from JSON string
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\": \"Mon Aug 23 16:58:37 IST 2021\"}"));
    JsonElement jsonDateElement = reader.nextValue();
    Date actualDate = adapter.read(reader);
    assertEquals(date, actualDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_StringDatePatternConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy HH:mm:ss z");

    // Test writing a Date
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date();
    adapter.write(writer, date);
    writer.flush();
    String jsonDate = System.out.getBuffer().toString();
    assertEquals("23/08/2021 16:58:37 IST", jsonDate.trim());

    // Test reading a Date from JSON string
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\": \"23/08/2021 16:58:37 IST\"}"));
    JsonElement jsonDateElement = reader.nextValue();
    Date actualDate = adapter.read(reader);
    assertEquals(date, actualDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStylesConstructor() throws SQLException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVATED_DATE_FORMAT, DateFormat.SHORT);

    // Test writing a Date
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date();
    adapter.write(writer, date);
    writer.flush();
    String jsonDate = System.out.getBuffer().toString();
    assertEquals("Aug 23, 16:58:37", jsonDate.trim());

    // Test reading a Date from JSON string
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\": \"Aug 23, 16:58:37\"}"));
    JsonElement jsonDateElement = reader.nextValue();
    Date actualDate = adapter.read(reader);
    assertEquals(date, actualDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_TimestampConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    // Test writing a Timestamp
    JsonWriter writer = new JsonWriter(System.out);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    adapter.write(writer, timestamp);
    writer.flush();
    String jsonTimestamp = System.out.getBuffer().toString();
    assertEquals("\"2021-08-23T16:58:37.123Z\"", jsonTimestamp.trim());

    // Test reading a Timestamp from JSON string
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"timestamp\": \"2021-08-23T16:58:37.123Z\"}"));
    JsonElement jsonTimestampElement = reader.nextValue();
    Timestamp actualTimestamp = adapter.read(reader);
    assertEquals(timestamp, actualTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_SqlDateConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    // Test writing a java.sql.Date
    JsonWriter writer = new JsonWriter(System.out);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    adapter.write(writer, sqlDate);
    writer.flush();
    String jsonSqlDate = System.out.getBuffer().toString();
    assertEquals("\"2021-08-23\"", jsonSqlDate.trim());

    // Test reading a java.sql.Date from JSON string
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"sqlDate\": \"2021-08-23\"}"));
    JsonElement jsonSqlDateElement = reader.nextValue();
    java.sql.Date actualSqlDate = adapter.read(reader);
    assertEquals(sqlDate, actualSqlDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapter_InvalidDateTypeException() {
    new DefaultDateTypeAdapter(Integer.class);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidJsonSyntaxException() {
    JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\": \"Invalid Date\"}"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(reader);
  }
}