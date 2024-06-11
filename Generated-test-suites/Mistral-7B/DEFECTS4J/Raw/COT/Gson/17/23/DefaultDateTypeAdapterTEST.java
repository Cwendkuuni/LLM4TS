package com.google.gson.tests;

import static org.junit.Assert.*;

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
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.getType());
  }

  @Test
  public void testDateConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.getType());
  }

  @Test
  public void testDateStringConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss z");
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.getType());
  }

  @Test
  public void testDateStyleConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.getType());
  }

  @Test
  public void testTimestampConstructor() {
    TypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter(Timestamp.class);
    assertNotNull(adapter);
    assertEquals(Timestamp.class, adapter.getType());
  }

  @Test
  public void testJavaSqlDateConstructor() {
    TypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    assertNotNull(adapter);
    assertEquals(java.sql.Date.class, adapter.getType());
  }

  @Test
  public void testWriteNullValue() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, null);
    writer.flush();
    assertEquals("null", writer.toString());
  }

  @Test
  public void testWriteValidDate() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, date);
    writer.flush();
    Date deserializedDate = new Gson().fromJson(writer.toString(), Date.class);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testWriteValidTimestamp() {
    TypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, timestamp);
    writer.flush();
    Timestamp deserializedTimestamp = new Gson().fromJson(writer.toString(), Timestamp.class);
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testWriteValidSqlDate() {
    TypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    JsonWriter writer = new JsonWriter(System.out);
    adapter.write(writer, sqlDate);
    writer.flush();
    java.sql.Date deserializedSqlDate = new Gson().fromJson(writer.toString(), java.sql.Date.class);
    assertEquals(sqlDate, deserializedSqlDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateTypeConstructor() {
    new DefaultDateTypeAdapter(String.class);
  }

  @Test
  public void testReadNullValue() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidFormat() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("invalidFormat"));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testReadValidDate() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    JsonElement jsonElement = new JsonParser().parse("\"2022-12-31 23:59:59 Z\"");
    Date date = adapter.read(new JsonReader(new StringReader(jsonElement.getAsJson().getAsString()))).getAsDate();
    assertEquals(new Date(1672633599000L), date);
  }

  @Test
  public void testReadValidTimestamp() {
    TypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonElement jsonElement = new JsonParser().parse("\"2022-12-31 23:59:59.000 Z\"");
    Timestamp timestamp = adapter.read(new JsonReader(new StringReader(jsonElement.getAsJson().getAsString()))).getAsTimestamp();
    assertEquals(new Timestamp(1672633599000L), timestamp);
  }

  @Test
  public void testReadValidSqlDate() {
    TypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonElement jsonElement = new JsonParser().parse("\"2022-12-31\"");
    java.sql.Date sqlDate = adapter.read(new JsonReader(new StringReader(jsonElement.getAsJson().getAsString()))).getAsDate();
    assertEquals(new java.sql.Date(1672633200000L), sqlDate);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidJson() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalidJson\":1}"));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidDateFormat() throws ParseException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidDateFormat = "2022/12/31 23:59:59"; // Invalid format for US locale
    Date date = adapter.read(new JsonReader(new StringReader(invalidDateFormat))).getAsDate();
    fail();
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidEnUsFormat() throws ParseException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidEnUsFormat = "2022-12-31 23:59:59 Z"; // Invalid format for en_US locale
    Date date = adapter.read(new JsonReader(new StringReader(invalidEnUsFormat))).getAsDate();
    fail();
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidLocalFormat() throws ParseException, SQLException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    String invalidLocalFormat = "2022-12-32 23:59:59"; // Invalid date for local format
    Date date = adapter.read(new JsonReader(new StringReader(invalidLocalFormat))).getAsDate();
    fail();
  }

  @Test
  public void testReadValidISODate() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class);
    String isoDate = "2022-12-31T23:59:59Z";
    Date date = adapter.read(new JsonReader(new StringReader(isoDate))).getAsDate();
    assertEquals(new Date(1672633599000L), date);
  }
}