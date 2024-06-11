package com.google.gson.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Tests for {@link DefaultDateTypeAdapter}.
 */
public class DefaultDateTypeAdapterTest {

  /**
   * Test constructor with Date.class.
   */
  @Test
  public void testDefaultDateTypeAdapter_Date() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test serialization and deserialization with US English format
    Date date = new Date();
    JsonElement json = new Gson().toJsonTree(date, adapter.getType());
    assertNotNull(json);
    Date deserializedDate = adapter.read(new JsonReader(new StringReader(json.toString()))).getAsDate();
    assertEquals(date, deserializedDate);

    // Test serialization and deserialization with local format
    Date localDate = Calendar.getInstance().getTime();
    JsonElement localJson = new Gson().toJsonTree(localDate, adapter.getType());
    assertNotNull(localJson);
    Date deserializedLocalDate = adapter.read(new JsonReader(new StringReader(localJson.toString()))).getAsDate();
    assertEquals(localDate, deserializedLocalDate);

    // Test serialization and deserialization with ISO8601 format
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    Date isoDate = isoFormat.parse("2019-12-31T23:59:59.999+01:00");
    JsonElement isoJson = new Gson().toJsonTree(isoDate, adapter.getType());
    assertNotNull(isoJson);
    Date deserializedIsoDate = adapter.read(new JsonReader(new StringReader(isoJson.toString()))).getAsDate();
    assertEquals(isoDate, deserializedIsoDate);
  }

  /**
   * Test constructor with Timestamp.class.
   */
  @Test
  public void testDefaultDateTypeAdapter_Timestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    // Test serialization and deserialization with US English format
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    JsonElement json = new Gson().toJsonTree(timestamp, adapter.getType());
    assertNotNull(json);
    Timestamp deserializedTimestamp = adapter.read(new JsonReader(new StringReader(json.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(timestamp.getTime(), deserializedTimestamp.getAsLong());

    // Test serialization and deserialization with local format
    SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    Timestamp localTimestamp = new Timestamp(localFormat.parse("2019-12-31 23:59:59.999").getTime().getTime());
    JsonElement localJson = new Gson().toJsonTree(localTimestamp, adapter.getType());
    assertNotNull(localJson);
    Timestamp deserializedLocalTimestamp = adapter.read(new JsonReader(new StringReader(localJson.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(localTimestamp.getTime(), deserializedLocalTimestamp.getAsLong());

    // Test serialization and deserialization with ISO8601 format
    Timestamp isoTimestamp = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    JsonElement isoJson = new Gson().toJsonTree(isoTimestamp, adapter.getType());
    assertNotNull(isoJson);
    Timestamp deserializedIsoTimestamp = new Gson().fromJson(isoJson.toString(), Timestamp.class);
    assertEquals(isoTimestamp, deserializedIsoTimestamp);
  }

  /**
   * Test constructor with java.sql.Date.class.
   */
  @Test
  public void testDefaultDateTypeAdapter_java_sql_Date() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    // Test serialization and deserialization with US English format
    java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
    JsonElement json = new Gson().toJsonTree(date, adapter.getType());
    assertNotNull(json);
    java.sql.Date deserializedDate = adapter.read(new JsonReader(new StringReader(json.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(date.getTime(), deserializedDate.getAsLong());

    // Test serialization and deserialization with local format
    SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    java.sql.Date localDate = new java.sql.Date(localFormat.parse("2019-12-31").getTime().getTime());
    JsonElement localJson = new Gson().toJsonTree(localDate, adapter.getType());
    assertNotNull(localJson);
    java.sql.Date deserializedLocalDate = adapter.read(new JsonReader(new StringReader(localJson.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(localDate.getTime(), deserializedLocalDate.getAsLong());

    // Test serialization and deserialization with ISO8601 format
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
    java.sql.Date isoDate = new java.sql.Date(isoFormat.parse("2019-12-31").getTime().getTime());
    JsonElement isoJson = new Gson().toJsonTree(isoDate, adapter.getType());
    assertNotNull(isoJson);
    java.sql.Date deserializedIsoDate = adapter.read(new JsonReader(new StringReader(isoJson.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(isoDate.getTime(), deserializedIsoDate.getAsLong());
  }

  /**
   * Test constructor with DateStyle and TimeStyle.
   */
  @Test
  public void testDefaultDateTypeAdapter_DateStyle_TimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);

    // Test serialization and deserialization with US English format
    Date date = new Date();
    JsonElement json = new Gson().toJsonTree(date, adapter.getType());
    assertNotNull(json);
    Date deserializedDate = adapter.read(new JsonReader(new StringReader(json.toString()))).getAsDate();
    assertEquals(date, deserializedDate);

    // Test serialization and deserialization with local format
    Calendar calendar = Calendar.getInstance();
    Date localDate = calendar.getTime();
    JsonElement localJson = new Gson().toJsonTree(localDate, adapter.getType());
    assertNotNull(localJson);
    Date deserializedLocalDate = adapter.read(new JsonReader(new StringReader(localJson.toString()))).getAsDate();
    assertEquals(localDate, deserializedLocalDate);

    // Test serialization and deserialization with ISO8601 format
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date isoDate = isoFormat.parse("2019-12-31 23:59:59");
    JsonElement isoJson = new Gson().toJsonTree(isoDate, adapter.getType());
    assertNotNull(isoJson);
    Date deserializedIsoDate = adapter.read(new JsonReader(new StringReader(isoJson.toString()))).getAsDate();
    assertEquals(isoDate, deserializedIsoDate);
  }

  /**
   * Test serialization and deserialization edge cases.
   */
  @Test
  public void testDefaultDateTypeAdapter_EdgeCases() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test null value
    JsonElement nullJson = new JsonParser().parse("null").getAsJson();
    nullJson = new Gson().toJsonTree(null, adapter.getType());
    assertNotNull(nullJson);
    assertEquals("null", nullJson.toString());
    assertNull(adapter.read(new JsonReader(new StringReader(nullJson.toString()))).getValue());

    // Test minimum and maximum Java Date values
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date minDate = new Date(Long.MIN_VALUE);
    Date maxDate = new Date(Long.MAX_VALUE);
    JsonElement minJson = new Gson().toJsonTree(minDate, adapter.getType());
    JsonElement maxJson = new Gson().toJsonTree(maxDate, adapter.getType());
    assertEquals(minDate.toString(), minJson.toString());
    assertEquals(maxDate.toString(), maxJson.toString());
    Date minDeserializedDate = adapter.read(new JsonReader(new StringReader(minJson.toString()))).getAsDate();
    Date maxDeserializedDate = adapter.read(new JsonReader(new StringReader(maxJson.toString()))).getAsDate();
    assertEquals(minDate, minDeserializedDate);
    assertEquals(maxDate, maxDeserializedDate);

    // Test minimum and maximum Timestamp values
    Date minTimestamp = new Date(Long.MIN_VALUE - 1000L);
    Date maxTimestamp = new Date(Long.MAX_VALUE);
    SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    JsonElement minTimestampJson = new Gson().toJsonTree(new Timestamp(minTimestamp.getTime()), adapter.getType());
    JsonElement maxTimestampJson = new Gson().toJsonTree(new Timestamp(maxTimestamp.getTime()), adapter.getType());
    assertEquals(timestampFormat.format(new Date(minTimestamp.getTime())), minTimestampJson.getAsJson().get("$date").getAsJsonPrimitive().getAsString());
    assertEquals(timestampFormat.format(new Date(maxTimestamp.getTime())), maxTimestampJson.getAsJson().get("$date").getAsJsonPrimitive().getAsString());
    Timestamp minDeserializedTimestamp = new Gson().fromJson(minTimestampJson.toString(), Timestamp.class);
    Timestamp maxDeserializedTimestamp = new Gson().fromJson(maxTimestampJson.toString(), Timestamp.class);
    assertEquals(minTimestamp, minDeserializedTimestamp);
    assertEquals(maxTimestamp, maxDeserializedTimestamp);

    // Test minimum and maximum java.sql.Date values
    Date minSqlDate = new java.sql.Date(Long.MIN_VALUE);
    Date maxSqlDate = new java.sql.Date(Long.MAX_VALUE);
    SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    JsonElement minSqlDateJson = new Gson().toJsonTree(minSqlDate, adapter.getType());
    JsonElement maxSqlDateJson = new Gson().toJsonTree(maxSqlDate, adapter.getType());
    assertEquals(sqlDateFormat.format(minSqlDate), minSqlDateJson.getAsJson().get("$date").getAsJsonPrimitive().getAsString());
    assertEquals(sqlDateFormat.format(maxSqlDate), maxSqlDateJson.getAsJson().get("$date").getAsJsonPrimitive().getAsString());
    java.sql.Date minDeserializedSqlDate = adapter.read(new JsonReader(new StringReader(minSqlDateJson.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    java.sql.Date maxDeserializedSqlDate = adapter.read(new JsonReader(new StringReader(maxSqlDateJson.toString()))).getAsJson().getAsJsonObject().get("$date").getAsJsonPrimitive().getAsLong();
    assertEquals(minSqlDate.getTime(), minDeserializedSqlDate.getAsLong());
    assertEquals(maxSqlDate.getTime(), maxDeserializedSqlDate.getAsLong());
  }

  /**
   * Test deserialization with invalid string format.
   */
  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidStringFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidString = "InvalidFormat";
    JsonElement json = new Gson().toJsonTree(invalidString, adapter.getType());
    adapter.read(new JsonReader(new StringReader(json.toString())));
  }
}