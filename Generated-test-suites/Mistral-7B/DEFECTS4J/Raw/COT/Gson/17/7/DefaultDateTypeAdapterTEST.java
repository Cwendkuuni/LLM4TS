package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.DefaultDateTypeAdapter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDatePattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd");
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT, Calendar.SHORT);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterWriteNullValue() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    adapter.write(jsonWriter, null);
    jsonWriter.flush();

    assertEquals("null", jsonWriter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteDate() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    Date date = new Date();
    adapter.write(jsonWriter, date);
    jsonWriter.flush();

    JsonElement jsonElement = JsonParser.parseString(jsonWriter.toString());
    assertEquals("\"2022-12-31T23:59:59.000Z\"", jsonElement.getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteTimestamp() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class)).create();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    adapter.write(jsonWriter, timestamp);
    jsonWriter.flush();

    JsonElement jsonElement = JsonParser.parseString(jsonWriter.toString());
    assertEquals("\"2022-12-31T23:59:59.000Z\"", jsonElement.getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testDefaultDateTypeAdapterWriteSqlDate() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class)).create();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    adapter.write(jsonWriter, sqlDate);
    jsonWriter.flush();

    JsonElement jsonElement = JsonParser.parseString(jsonWriter.toString());
    assertEquals("\"2022-12-31\"", jsonElement.getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testDefaultDateTypeAdapterReadNullValue() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    JsonReader jsonReader = new JsonReader(new StringReader("null"));

    assertNull(gson.fromJson(jsonReader, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterReadDate() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    JsonReader jsonReader = new JsonReader(new StringReader("\"2022-12-31T23:59:59.000Z\""));

    Date date = gson.fromJson(jsonReader, Date.class);
    assertEquals(new Date(), date);
  }

  @Test
  public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class)).create();
    JsonReader jsonReader = new JsonReader(new StringReader("\"2022-12-31T23:59:59.000Z\""));

    Timestamp timestamp = gson.fromJson(jsonReader, Timestamp.class);
    assertEquals(new Timestamp(System.currentTimeMillis()), timestamp);
  }

  @Test
  public void testDefaultDateTypeAdapterReadSqlDate() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class)).create();
    JsonReader jsonReader = new JsonReader(new StringReader("\"2022-12-31\""));

    java.sql.Date sqlDate = gson.fromJson(jsonReader, java.sql.Date.class);
    assertEquals(new java.sql.Date(System.currentTimeMillis()), sqlDate);
  }

  @Test
  public void testDefaultDateTypeAdapterParseStringWithEnUsFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    Date date = adapter.deserializetoDate("2022-12-31T23:59:59.000Z");
    assertEquals(new Date(), date);
  }

  @Test
  public void testDefaultDateTypeAdapterParseStringWithLocalFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    Date date = adapter.deserializetoDate("2022-12-31");
    assertEquals(new Date(), date);
  }

  @Test
  public void testDefaultDateTypeAdapterParseStringWithISO8601Utils() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    Date date = adapter.deserializetoDate("2022-12-31T23:59:59.000Z");
    assertEquals(new Date(), date);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapterParseStringWithInvalidFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    adapter.deserializetoDate("invalid_format");
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterWriteInvalidFormat() throws IOException {
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    adapter.write(jsonWriter, new Date());
    jsonWriter.flush();

    // This should fail with JsonSyntaxException
    JsonParser jsonParser = new JsonParser();
    JsonElement jsonElement = jsonParser.parse(jsonWriter.toString());
  }

}