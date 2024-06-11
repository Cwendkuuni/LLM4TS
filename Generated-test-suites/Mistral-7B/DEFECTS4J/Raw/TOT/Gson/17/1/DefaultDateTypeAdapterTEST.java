package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() throws ParseException {
    // Test with Default constructor
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter();
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    // Serialization and Deserialization tests
    Date date = new Date();
    String json = gson.toJson(date);
    Date deserializedDate = gson.fromJson(json, Date.class);
    assertEquals(date, deserializedDate);

    // Test with constructor taking DatePattern
    SimpleDateFormat usFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    TypeAdapter<Date> dateAdapterWithPattern = new DefaultDateTypeAdapter("", usFormat, usFormat);
    Gson gsonWithPattern = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapterWithPattern).create();
    String jsonWithPattern = gsonWithPattern.toJson(date);
    Date deserializedDateWithPattern = gsonWithPattern.fromJson(jsonWithPattern, Date.class);
    assertEquals(date, deserializedDateWithPattern);

    // Test with constructor taking int for date style and int for time style
    TypeAdapter<Date> dateAdapterWithStyle = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    Gson gsonWithStyle = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapterWithStyle).create();
    String jsonWithStyle = gsonWithStyle.toJson(date);
    Date deserializedDateWithStyle = gsonWithStyle.fromJson(jsonWithStyle, Date.class);
    assertEquals(date, deserializedDateWithStyle);
  }

  @Test
  public void testDefaultDateTypeAdapterWithNullInput() {
    // Test with null input
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    JsonElement jsonElement = JsonParser.parseString("{\"date\": null}");

    assertThrows(JsonParseException.class, () -> gson.fromJson(jsonElement, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterWithInvalidFormat() {
    // Test with invalid format
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter("", 12);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    String json = "{\"date\": \"InvalidFormat\"}";

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterWithEmptyString() {
    // Test with empty string
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter("", new SimpleDateFormat(""));
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    String json = "{\"date\": \"\"}";

    assertNull(gson.fromJson(json, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapterWithTimestamp() throws SQLException, ParseException {
    // Test with Timestamp
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    String json = gson.toJson(timestamp);
    Date deserializedDate = gson.fromJson(json, Date.class);
    assertEquals(timestamp, new Timestamp(deserializedDate.getTime()));
  }

  @Test
  public void testDefaultDateTypeAdapterWithSqlDate() throws SQLException, ParseException {
    // Test with java.sql.Date
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    String json = gson.toJson(sqlDate);
    Date deserializedDate = gson.fromJson(json, Date.class);
    assertEquals(sqlDate, new java.sql.Date(deserializedDate.getTime()));
  }
}