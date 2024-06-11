package com.google.gson.tests.typeadapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_serialization() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    Date date = new Date(); // current date
    String json = gson.toJson(date, adapter.getClass());

    JsonElement jsonElement = JsonParser.parseString(json);
    Date deserializedDate = adapter.read(gson.newJsonReader(jsonElement.getAsString()));

    assertNotNull(deserializedDate);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_serialization_US_format() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "MM/dd/yyyy hh:mm:ss a z");

    Date date = new Date(); // current date
    String json = gson.toJson(date, adapter.getClass());

    JsonElement jsonElement = JsonParser.parseString(json);
    Date deserializedDate = adapter.read(gson.newJsonReader(jsonElement.getAsString()));

    assertNotNull(deserializedDate);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_serialization_ISO_format() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    Date date = new Date(); // current date
    String json = gson.toJson(date, adapter.getClass());

    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    JsonElement jsonElement = JsonParser.parseString(json);
    Date deserializedDate = adapter.read(gson.newJsonReader(jsonElement.getAsString()));

    assertNotNull(deserializedDate);
    assertEquals(date, deserializedDate);
    assertEquals(isoFormat.format(date), jsonElement.getAsString());
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_deserialization_invalid_string_format() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    String invalidDateString = "Invalid date string";
    adapter.read(gson.newJsonReader(new StringReader(invalidDateString)));
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_deserialization_missing_value() {
    String json = "{}";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    adapter.read(new JsonReader(new StringReader(json)));
  }

  @Test
  public void testDefaultDateTypeAdapter_deserialization_Date() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    String jsonWithDate = "{\"date\":\"01/01/2023 12:00:00 AM UTC\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonWithDate);

    Date deserializedDate = adapter.read(gson.newJsonReader(jsonElement.getAsJson().get("date").getAsString()));

    assertNotNull(deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_deserialization_Timestamp() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    String jsonWithTimestamp = "{\"timestamp\":\"2023-01-01 12:00:00.000000Z\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonWithTimestamp);

    Timestamp deserializedTimestamp = (Timestamp) adapter.read(gson.newJsonReader(jsonElement.getAsJson().get("timestamp").getAsString()));

    assertNotNull(deserializedTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_deserialization_java_sql_Date() {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class, "yyyy-MM-dd");

    String jsonWithSqlDate = "{\"sqlDate\":\"2023-01-01\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonWithSqlDate);

    java.sql.Date deserializedSqlDate = (java.sql.Date) adapter.read(gson.newJsonReader(jsonElement.getAsJson().get("sqlDate").getAsString()));

    assertNotNull(deserializedSqlDate);
  }
}