package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_defaultConstructor() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    // Test serialization
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    Date date = new Date(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(date);
    assertEquals("2021-11-12T09:40:23.000Z", jsonDate);

    // Test deserialization
    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    Date readDate = dateAdapter.read(jsonReader);
    assertEquals(date, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_stringDatePatternConstructor() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // Test serialization
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    Date date = new Date(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(date);
    assertEquals("2021-11-12T09:40:23.000Z", jsonDate);

    // Test deserialization
    JsonReader jsonReader = new JsonReader(new StringReader("2021-11-12T09:40:23.000Z"));
    Date readDate = dateAdapter.read(jsonReader);
    assertEquals(date, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_intDateStyleAndTimeStyleConstructor() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY);

    // Test serialization
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    Date date = new Date(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(date);
    assertEquals("Fri Nov 12 09:40:23 EST 2021", jsonDate);

    // Test deserialization
    JsonReader jsonReader = new JsonReader(new StringReader("Fri Nov 12 09:40:23 EST 2021"));
    Date readDate = dateAdapter.read(jsonReader);
    assertEquals(date, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_readNullString() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapter_readInvalidDateFormat() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    String invalidJsonDate = "invalid_json_date";
    assertThrows(JsonSyntaxException.class, () -> dateAdapter.read(new JsonReader(new StringReader(invalidJsonDate))));
  }

  @Test
  public void testDefaultDateTypeAdapter_readParsingException() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    String invalidJsonDate = "Wed, 21 Jan 1998 01:23:55 GMT"; // Invalid format: missing 'T' and 'Z'
    assertThrows(JsonParseException.class, () -> dateAdapter.read(new JsonReader(new StringReader(invalidJsonDate))));
  }

  @Test
  public void testDefaultDateTypeAdapter_readCompoundDateFormat() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    String compoundJsonDate = "Wed, 21 Jan 1998 01:23:55 GMT+01:00";
    assertThrows(JsonParseException.class, () -> dateAdapter.read(new JsonReader(new StringReader(compoundJsonDate))));
  }

  @Test
  public void testDefaultDateTypeAdapter_writeInvalidDate() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();

    Date nullDate = null;
    String json = gson.toJson(nullDate);
    assertEquals("null", json);

    Date invalidDate = new Date(-1); // invalid date: negative milliseconds
    json = gson.toJson(invalidDate);
    assertThrows(IllegalArgumentException.class, () -> gson.fromJson(json, Date.class));
  }

  @Test
  public void testDefaultDateTypeAdapter_writeDateTimeFormatAndLocale() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINESE), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINESE));

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    Date date = new Date(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(date);
    assertEquals("2021-11-12T09:40:23.000+05:30", jsonDate); // Chinese locale adds 5.5 hours to the date

    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    Date readDate = dateAdapter.read(jsonReader);
    assertEquals(date, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_writeJavaSqlDate() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, dateAdapter).create();
    java.sql.Date sqlDate = new java.sql.Date(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(sqlDate);
    assertEquals("2021-11-12", jsonDate);

    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    java.sql.Date readDate = dateAdapter.read(jsonReader);
    assertEquals(sqlDate, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_writeTimestamp() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);

    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, dateAdapter).create();
    Timestamp timestamp = new Timestamp(1633059823000L); // Fri Nov 12 09:40:23 EST 2021
    String jsonDate = gson.toJson(timestamp);
    assertEquals("2021-11-12T09:40:23.000Z", jsonDate);

    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    Timestamp readDate = dateAdapter.read(jsonReader);
    assertEquals(timestamp, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_writeDateTimeZone() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    // Write date with timezone
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1633059823000L), ZoneId.of("America/New_York"));
    String jsonDate = gson.toJsonTree(zdt).getAsString();
    assertEquals("2021-11-12T09:40:23.000-05:00", jsonDate);

    // Read date with timezone
    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    ZonedDateTime readDate = dateAdapter.read(jsonReader).toInstant().atZone(ZoneId.systemDefault());
    assertEquals(zdt, readDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_writeInvalidJsonDateTimeZone() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    // Write date with invalid timezone
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateAdapter).create();
    String jsonDate = gson.toJsonTree(new Date(1633059823000L)).getAsString(); // Fri Nov 12 09:40:23 EST 2021
    jsonDate = jsonDate.replace("EST", "EST+12:00"); // Add 12 hours to the timezone

    // Read date with invalid timezone
    JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
    assertThrows(JsonIOException.class, () -> dateAdapter.read(jsonReader));
  }
}