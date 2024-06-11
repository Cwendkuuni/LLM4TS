package com.google.gson.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_ValidDates() throws IOException, ParseException, SQLException {
    final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    final TypeAdapter<Date> defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    // Test with valid US English formatted date strings
    Gson gson = new Gson();
    String jsonString = "{\"date\":\"" + dateFormat.format(new Date()) + "\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    Date date = defaultDateTypeAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals(new Date(), date);

    // Test with valid local formatted date strings
    Locale locale = Locale.FRENCH;
    dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
    jsonString = "{\"date\":\"" + dateFormat.format(new Date()) + "\"}";
    jsonElement = JsonParser.parseString(jsonString);
    date = defaultDateTypeAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals(new Date(), date);

    // Test with valid ISO-8601 formatted date strings
    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    jsonString = "{\"date\":\"2022-12-31T23:59:59.999-05:00\"}";
    jsonElement = JsonParser.parseString(jsonString);
    date = defaultDateTypeAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals(new Date(iso8601Format.parse("2022-12-31T23:59:59.999-05:00").getTime()), date);

    // Test with valid Timestamp formatted date strings
    jsonString = "{\"timestamp\":\"2022-12-31 23:59:59.999-05:00\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    jsonReader.setLenient(true); // Allow for incorrect format due to testing
    jsonReader.beginObject();
    jsonReader.nextName();
    JsonToken token = jsonReader.peek();
    eq(JsonToken.NAME, token);
    jsonReader.nextName();
    token = jsonReader.peek();
    eq(JsonToken.STRING, token);
    jsonReader.nextString(); // Read the name "timestamp"
    jsonReader.nextName();
    token = jsonReader.peek();
    eq(JsonToken.STRING, token);
    String timestampString = jsonReader.nextString();
    jsonReader.endObject();
    Timestamp timestamp = new Timestamp(ISO8601Utils.parse(timestampString, new ParsePosition(0)).getTime());
    Date deserializedDate = defaultDateTypeAdapter.read(gson.newJsonReader(new StringReader(gson.toJson(timestamp))));
    assertEquals(timestamp, new Timestamp(deserializedDate.getTime()));

    // Test with valid SQL Date formatted date strings
    jsonString = "{\"sqlDate\":\"2022-12-31\"}";
    jsonElement = JsonParser.parseString(jsonString);
    java.sql.Date sqlDate = new java.sql.Date(iso8601Format.parse("2022-12-31").getTime());
    date = defaultDateTypeAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals(sqlDate, new java.sql.Date(date.getTime()));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidDateFormats() throws IOException, ParseException {
    final TypeAdapter<Date> defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    // Test with missing date string
    String jsonString = "{\"date\":null}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextNull();
    jsonReader.endObject();
    try {
      defaultDateTypeAdapter.read(jsonReader);
    } catch (JsonIOException e) {
      assertEquals("Expected a string but was null", e.getMessage());
      throw e;
    }

    // Test with empty string
    jsonString = "{\"date\":\"\"}";
    jsonReader = new JsonReader(new StringReader(jsonString));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextString();
    jsonReader.endObject();
    try {
      defaultDateTypeAdapter.read(jsonReader);
    } catch (JsonSyntaxException e) {
      throw e;
    } catch (IOException e) {
      fail("Expected JsonSyntaxException");
    }

    // Test with invalid date string with non-US English format
    jsonString = "{\"date\":\"31.12.2022 23:59:59\"}"; // German date format
    jsonReader = new JsonReader(new StringReader(jsonString));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextString();
    jsonReader.endObject();
    try {
      defaultDateTypeAdapter.read(jsonReader);
    } catch (JsonParseException e) {
      throw new JsonSyntaxException("Invalid date format: " + jsonString, e);
    } catch (IOException e) {
      fail("Expected JsonSyntaxException");
    }

    // Test with invalid date string with mixed formats
    jsonString = "{\"date\":\"2022-12-31 23:59:59.999Z\"}"; // ISO-8601 with timezone
    jsonReader = new JsonReader(new StringReader(jsonString));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextString();
    jsonReader.endObject();
    try {
      defaultDateTypeAdapter.read(jsonReader);
    } catch (JsonSyntaxException e) {
      throw new JsonSyntaxException("Invalid date format: " + jsonString, e);
    } catch (IOException e) {
      fail("Expected JsonSyntaxException");
    }
  }

  private void eq(JsonToken expectedToken, JsonToken actualToken) {
    assertEquals("Expected token " + expectedToken.name() + " but was " + actualToken.name(), expectedToken, actualToken);
  }
}