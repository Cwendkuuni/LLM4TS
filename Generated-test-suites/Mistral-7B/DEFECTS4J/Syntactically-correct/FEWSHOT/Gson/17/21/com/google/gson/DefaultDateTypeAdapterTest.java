package com.google.gson.internal.bind.util; // Import ISO8601Utils for testing

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private static Gson createGson() {
    return new Gson();
  }

  @Test
  public void testDefaultDateTypeAdapter() {
    assertEquals("DefaultDateTypeAdapter(DateFormat@412216d8)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + DateFormat.class.getSimpleName() + ")");
    assertEquals("DefaultDateTypeAdapter(DateFormat@56330212)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + SimpleDateFormat.class.getSimpleName() + ")");
    assertEquals("DefaultDateTypeAdapter(DateFormat@5c33670a)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + SimpleDateFormat.class.getSimpleName() + ")");
    assertEquals("DefaultDateTypeAdapter(java.util.Date)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + Date.class.getSimpleName() + ")");
    assertEquals("DefaultDateTypeAdapter(java.sql.Date)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + java.sql.Date.class.getSimpleName() + ")");
    assertEquals("DefaultDateTypeAdapter(java.sql.Timestamp)", DefaultDateTypeAdapter.class.getSimpleName() + "(" + Timestamp.class.getSimpleName() + ")");
  }

  @Test
  public void testDefaultDateTypeAdapter_Date() {
    Date date = new Date();

    JsonElement jsonElement = createGson().toJsonTree(date);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Date deserializedDate = createGson().fromJson(jsonObject.get(""), Date.class);

    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Date_US() {
    Date date = new Date();
    SimpleDateFormat enUsFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);

    JsonElement jsonElement = createGson().toJsonTree(date, new DefaultDateTypeAdapter(Date.class, enUsFormat, enUsFormat));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Date deserializedDate = createGson().fromJson(jsonObject.get(""), Date.class);

    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Date_Local() {
    Date date = new Date();
    SimpleDateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault());

    JsonElement jsonElement = createGson().toJsonTree(date, new DefaultDateTypeAdapter(Date.class, localFormat, localFormat));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Date deserializedDate = createGson().fromJson(jsonObject.get(""), Date.class);

    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Date_javaSQL() {
    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

    JsonElement jsonElement = createGson().toJsonTree(sqlDate, new DefaultDateTypeAdapter(java.sql.Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    java.sql.Date deserializedDate = createGson().fromJson(jsonObject.get(""), java.sql.Date.class);

    assertEquals(sqlDate, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Timestamp() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    JsonElement jsonElement = createGson().toJsonTree(timestamp);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Timestamp deserializedTimestamp = createGson().fromJson(jsonObject.get(""), Timestamp.class);

    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_Timestamp_US() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat enUsFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS a z", Locale.US);

    JsonElement jsonElement = createGson().toJsonTree(timestamp, new DefaultDateTypeAdapter(Timestamp.class, enUsFormat, enUsFormat));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Timestamp deserializedTimestamp = createGson().fromJson(jsonObject.get(""), Timestamp.class);

    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_Timestamp_Local() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS a z", Locale.getDefault());

    JsonElement jsonElement = createGson().toJsonTree(timestamp, new DefaultDateTypeAdapter(Timestamp.class, localFormat, localFormat));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Timestamp deserializedTimestamp = createGson().fromJson(jsonObject.get(""), Timestamp.class);

    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testDefaultDateTypeAdapter_javaSQLDate() {
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    JsonElement jsonElement = createGson().toJsonTree(sqlDate);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    java.sql.Date deserializedDate = createGson().fromJson(jsonObject.get(""), java.sql.Date.class);

    assertEquals(sqlDate, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_Null() {
    JsonReader jsonReader = new JsonReader(new StringReader("null"));

    try {
      createGson().fromJson(jsonReader, Date.class);
      fail("Expected JsonIOException for null value");
    } catch (JsonIOException e) {
      // Expected
    }

    try {
      createGson().fromJson(jsonReader, java.sql.Date.class);
      fail("Expected JsonIOException for null value");
    } catch (JsonIOException e) {
      // Expected
    }

    try {
      createGson().fromJson(jsonReader, Timestamp.class);
      fail("Expected JsonIOException for null value");
    } catch (JsonIOException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_InvalidFormat() {
    JsonReader jsonReader = new JsonReader(new StringReader("invalid_date"));

    try {
      createGson().fromJson(jsonReader, Date.class);
      fail("Expected JsonSyntaxException for invalid date format");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      createGson().fromJson(jsonReader, java.sql.Date.class);
      fail("Expected JsonSyntaxException for invalid date format");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      createGson().fromJson(jsonReader, Timestamp.class);
      fail("Expected JsonSyntaxException for invalid date format");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_ISOFormat() {
    String isoDate = "2022-03-20T11:30:45.012+01:00";

    JsonElement jsonElement = createGson().toJsonTree(isoDate, new DefaultDateTypeAdapter(Date.class, ISO8601Utils.getLenientParser(), ISO8601Utils.getLenientParser()));
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Date deserializedDate = createGson().fromJson(jsonObject.get(""), Date.class);

    Date expectedDate = new Date(ISO8601Utils.parse(isoDate, new ParsePosition(0)).getTime());
    assertEquals(expectedDate, deserializedDate);
  }
}