package com.google.gson.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * Unit tests for {@link DefaultDateTypeAdapter}.
 */
public class DefaultDateTypeAdapterTest {

  private static final String TEST_DATE = "2022-01-01T12:34:56.789Z";
  private static final String TEST_SQL_DATE = "2022-01-01";
  private static final String TEST_TIMESTAMP = "2022-01-01T12:34:56.789Z";
  private static final String TEST_EMPTY_STRING = "";
  private static final String TEST_NULL_STRING = null;
  private static final String TEST_INVALID_FORMAT = "InvalidFormat";

  private static final Locale US_LOCALE = Locale.US;

  private static final DateFormat EN_US_FORMAT;
  private static final DateFormat LOCAL_FORMAT;

  static {
    EN_US_FORMAT = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, US_LOCALE);
    LOCAL_FORMAT = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
  }

  private static <T extends Date> T parseDate(String dateString, DateFormat format) {
    try {
      return (T) format.parse(dateString);
    } catch (ParseException e) {
      throw new AssertionError("Failed to parse date: " + dateString, e);
    }
  }

  @Test
  public void testValidDate() {
    DateFormat format = LOCAL_FORMAT;
    Date date = parseDate(TEST_DATE, format);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, format, format);

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    String json = gson.toJson(date);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement dateElement = jsonObject.get("date");

    assertEquals(TEST_DATE, dateElement.getAsString());

    Date deserializedDate = adapter.read(JsonReader.ofString(json));
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testValidSqlDate() {
    DateFormat format = LOCAL_FORMAT;
    Date sqlDate = new Date(parseDate(TEST_SQL_DATE, format).getTime());
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class, format, format);

    Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, adapter).create();

    String json = gson.toJson(sqlDate);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement dateElement = jsonObject.get("date");

    assertEquals(TEST_SQL_DATE, dateElement.getAsString());

    java.sql.Date deserializedDate = (java.sql.Date) adapter.read(JsonReader.ofString(json));
    assertEquals(sqlDate, deserializedDate);
  }

  @Test
  public void testValidTimestamp() {
    DateFormat format = LOCAL_FORMAT;
    Timestamp timestamp = new Timestamp(parseDate(TEST_TIMESTAMP, format).getTime());
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class, format, format);

    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, adapter).create();

    String json = gson.toJson(timestamp);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement dateElement = jsonObject.get("date");

    assertEquals(TEST_TIMESTAMP, dateElement.getAsString());

    Timestamp deserializedTimestamp = (Timestamp) adapter.read(JsonReader.ofString(json));
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testEmptyString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, EN_US_FORMAT, LOCAL_FORMAT);

    String json = "{}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertNull(adapter.read(JsonReader.ofJsonElement(jsonObject)));
  }

  @Test
  public void testNullString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, EN_US_FORMAT, LOCAL_FORMAT);

    JsonReader reader = new JsonReader(new StringReader(TEST_NULL_STRING));
    reader.setLenient(true);

    assertThrows(JsonParseException.class, () -> adapter.read(reader));
  }

  @Test
  public void testInvalidFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, EN_US_FORMAT, LOCAL_FORMAT);

    String json = "{\"date\":\"" + TEST_INVALID_FORMAT + "\"}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertThrows(JsonSyntaxException.class, () -> adapter.read(JsonReader.ofJsonElement(jsonObject)));
  }

  @Test
  public void testDeserializationWithISO8601Utils() {
    DateFormat format = LOCAL_FORMAT;
    Date date = parseDate(TEST_DATE, format);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, format, format);

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    String json = gson.toJson(date);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement dateElement = jsonObject.get("date");

    assertEquals(TEST_DATE, dateElement.getAsString());

    // Test deserialization with ISO8601Utils.
    JsonReader reader = new JsonReader(new StringReader(json));
    Date deserializedDate = adapter.read(reader);

    assertEquals(date, deserializedDate);
  }
}