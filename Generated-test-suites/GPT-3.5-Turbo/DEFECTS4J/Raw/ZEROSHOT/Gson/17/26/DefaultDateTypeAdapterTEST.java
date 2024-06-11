package com.google.gson;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    adapter.write(writer, null);
    assertEquals(JsonToken.NULL, writer.peek());
  }

  @Test
  public void testWrite_NonNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    Date value = new Date();
    adapter.write(writer, value);
    assertEquals(JsonToken.STRING, writer.peek());
    assertEquals(value.toString(), writer.nextString());
  }

  @Test
  public void testRead_InvalidJson() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.beginArray();
    try {
      adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testRead_ValidJson() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date");
    reader.value("2022-01-01");
    reader.endObject();
    Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }

  @Test
  public void testRead_ValidTimestampJson() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("timestamp");
    reader.value("2022-01-01 12:34:56");
    reader.endObject();
    Date expected = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 12:34:56").getTime());
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }

  @Test
  public void testRead_ValidSqlDateJson() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("sqlDate");
    reader.value("2022-01-01");
    reader.endObject();
    Date expected = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01").getTime());
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }

  @Test
  public void testDeserializeToDate_ValidDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-01-01";
    Date expected = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    Date actual = adapter.deserializeToDate(dateString);
    assertEquals(expected, actual);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_InvalidDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-01-01T12:34:56Z";
    adapter.deserializeToDate(dateString);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actual = adapter.toString();
    assertEquals(expected, actual);
  }
}