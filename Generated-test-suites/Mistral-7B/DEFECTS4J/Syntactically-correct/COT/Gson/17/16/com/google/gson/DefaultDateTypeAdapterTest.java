package com.google.gson.tests;

import static org.junit.Assert.*;

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
import com.google.gson.reflect.TypeToken;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testStringConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd HH:mm:ss");
    assertEquals("yyyy-MM-dd HH:mm:ss", adapter.enUsFormat.toPattern());
  }

  @Test
  public void testIntConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DATE_FIELD, DateFormat.SHORT);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testWriteDateToJson() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();

    Gson gson = new Gson();
    String json = gson.toJson(date, adapter);
    assertEquals("\"2023-03-06 14:25:53.573\"", json);

    JsonElement jsonElement = JsonParser.parseString(json);
    Date deserialized = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
    assertEquals(date.getTime(), deserialized.getTime());
  }

  @Test(expected = JsonParseException.class)
  public void testNullJsonInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testInvalidJsonInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("invalid_input"));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonParseException.class)
  public void testWriteNullToJson() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, null);
  }

  @Test
  public void testWriteDateToJsonWithCustomFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd");
    Date date = new Date();

    Gson gson = new Gson();
    String json = gson.toJson(date, adapter);
    assertEquals("\"2023-03-06\"", json);

    JsonElement jsonElement = JsonParser.parseString(json);
    Date deserialized = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
    assertEquals(date.getTime(), deserialized.getTime());
  }

  @Test(expected = JsonParseException.class)
  public void testInvalidFormatForEnUsFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss");
    String json = "\"2023-03-32\""; // Invalid date format for enUsFormat

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonParseException.class)
  public void testInvalidFormatForLocalFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
    String json = "\"2023-03-32\""; // Invalid date format for localFormat

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    adapter.read(jsonReader);
  }

  @Test
  public void testWriteDateToJsonWithCustomFormatAndNullInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd");

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, null);
    assertEquals("null", jsonWriter.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateType() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(int.class);
  }
}