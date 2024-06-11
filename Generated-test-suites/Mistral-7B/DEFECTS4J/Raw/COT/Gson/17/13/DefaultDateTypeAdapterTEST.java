package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
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
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateType() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapterConstructorWithInvalidDateType() {
    new DefaultDateTypeAdapter(String.class);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDatePattern() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", dateAdapter.enUsFormat.toPattern());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyles() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @Test
  public void testWriteNullDate() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(System.out);
    dateAdapter.write(writer, null);
    writer.flush();

    String output = System.out.getBuffer().toString();
    assertNull(output);
  }

  @Test
  public void testWriteDate() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(System.out);

    Date date = new Date();
    dateAdapter.write(writer, date);
    writer.flush();

    String output = System.out.getBuffer().toString();
    SimpleDateFormat expectedFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    String expected = expectedFormat.format(date);
    assertEquals(expected, output);
  }

  @Test
  public void testReadNull() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("null"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    assertNull(dateAdapter.read(reader));
  }

  @Test
  public void testReadEmptyString() throws IOException {
    JsonReader reader = new JsonReader(new StringReader(""));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    assertNull(dateAdapter.read(reader));
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidDateFormat1() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("invalidDateFormat"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    dateAdapter.read(reader);
  }

  @Test
  public void testReadValidDateFormat1() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01T00:00:00Z"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date date = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    Date expected = expectedFormat.parse("2022-01-01T00:00:00Z");
    assertSame(expected, date);
  }

  @Test
  public void testReadValidDateFormat2() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01 00:00:00"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date date = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expected = expectedFormat.parse("2022-01-01 00:00:00");
    assertSame(expected, date);
  }

  @Test
  public void testReadValidDateFormat3() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date date = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date expected = expectedFormat.parse("2022-01-01");
    assertSame(expected, date);
  }

  @Test
  public void testReadValidDateFormat4() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01T00:00:00.000+01:00"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date date = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date expected = expectedFormat.parse("2022-01-01T00:00:00.000+01:00");
    assertSame(expected, date);
  }

  @Test
  public void testReadInvalidDateFormatWithCustomErrorMessage() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("invalidDateFormat"));
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class) {
      @Override
      public void handleMalformedJson(@Nonnull JsonParser jsonParser, @Nonnull JsonSyntaxException e) {
        throw new JsonParseException("Custom error message", e);
      }
    };

    try {
      dateAdapter.read(reader);
    } catch (JsonParseException e) {
      assertEquals("Custom error message", e.getMessage());
    }
  }

  @Test
  public void testReadValidTimestampFormat1() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01T00:00:00.000Z"));
    DefaultDateTypeAdapter<Timestamp> dateAdapter = new DefaultDateTypeAdapter<>(Timestamp.class);

    Timestamp timestamp = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date date = expectedFormat.parse("2022-01-01T00:00:00.000Z");
    Timestamp expected = new Timestamp(date.getTime());
    assertSame(expected, timestamp);
  }

  @Test
  public void testReadValidTimestampFormat2() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01 00:00:00.123"));
    DefaultDateTypeAdapter<Timestamp> dateAdapter = new DefaultDateTypeAdapter<>(Timestamp.class);

    Timestamp timestamp = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date date = expectedFormat.parse("2022-01-01 00:00:00.123");
    Timestamp expected = new Timestamp(date.getTime());
    assertSame(expected, timestamp);
  }

  @Test
  public void testReadValidSqlDateFormat1() throws IOException {
    JsonReader reader = new JsonReader(new StringReader("2022-01-01"));
    DefaultDateTypeAdapter<java.sql.Date> dateAdapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);

    java.sql.Date sqlDate = dateAdapter.read(reader);
    SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = expectedFormat.parse("2022-01-01");
    java.sql.Date expected = new java.sql.Date(date.getTime());
    assertSame(expected, sqlDate);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
    String toString = dateAdapter.toString();
    assertTrue(toString.contains(DefaultDateTypeAdapter.SIMPLE_NAME));
    assertTrue(toString.contains(DateFormat.class.getSimpleName()));
  }
}