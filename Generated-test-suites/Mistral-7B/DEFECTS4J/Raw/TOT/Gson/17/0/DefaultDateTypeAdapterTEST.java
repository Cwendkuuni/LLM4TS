package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
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

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonToken;

/**
 * Unit tests for {@link DefaultDateTypeAdapter}.
 */
public class DefaultDateTypeAdapterTest {

  private static final String TEST_DATE_1 = "2021-12-31 23:59:59";
  private static final String TEST_DATE_2 = "2021-12-31T23:59:59Z";
  private static final String TEST_DATE_3 = "December 31, 2021 11:59:59 PM";

  private static final SimpleDateFormat US_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
  private static final SimpleDateFormat LOCAL_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss a", Locale.US);

  private Gson createGson() {
    return new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class)).create();
  }

  @Test
  public void testWriteReadDate() throws IOException, ParseException {
    Date date = new Date();
    String json = createGson().toJson(date);
    Date expectedDate = new Date();

    Date readDate = createGson().fromJson(json, Date.class);
    assertEquals(expectedDate, readDate);
  }

  @Test
  public void testWriteReadTimestamp() throws IOException, ParseException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String json = createGson().toJson(timestamp);
    Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

    Timestamp readTimestamp = createGson().fromJson(json, Timestamp.class);
    assertSame(expectedTimestamp, readTimestamp);
  }

  @Test
  public void testWriteReadSqlDate() throws IOException, ParseException {
    Date sqlDate = new Date(0); // java.sql.Date has a zero time component
    String json = createGson().toJson(sqlDate);
    Date expectedSqlDate = new Date(0);

    Date readSqlDate = createGson().fromJson(json, java.sql.Date.class);
    assertSame(expectedSqlDate, readSqlDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateType() {
    new GsonBuilder().registerTypeAdapter(Integer.class, new DefaultDateTypeAdapter<>(Integer.class)).create();
  }

  @Test
  public void testDeserializeLocalFormat() throws IOException, ParseException {
    String json = "\"December 31, 2021 11:59:59 PM\"";
    JsonReader reader = new JsonReader(new StringReader(json));

    Date date = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)).read(reader);
    assertEquals(LOCAL_FORMAT.parse(TEST_DATE_3), date);
  }

  @Test
  public void testDeserializeEnUsFormat() throws IOException, ParseException {
    String json = "\"2021-12-31T23:59:59Z\"";
    JsonReader reader = new JsonReader(new StringReader(json));

    Date date = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss'Z'").read(reader);
    assertEquals(US_FORMAT.parse(TEST_DATE_2), date);
  }

  @Test
  public void testDeserializeISO8601Format() throws IOException, ParseException {
    String json = "\"2021-12-31T23:59:59.000Z\"";
    JsonReader reader = new JsonReader(new StringReader(json));

    Date date = new DefaultDateTypeAdapter(Date.class).read(reader);
    assertEquals(ISO8601Utils.parse(TEST_DATE_1, new ParsePosition(0)), date);
  }

  @Test
  public void testNullValue() throws IOException {
    String json = "null";
    JsonReader reader = new JsonReader(new StringReader(json));

    Date date = new DefaultDateTypeAdapter(Date.class).read(reader);
    assertNull(date);
  }

  @Test(expected = JsonParseException.class)
  public void testInvalidDateFormat() throws IOException, ParseException {
    String json = "\"Invalid date\"";
    JsonReader reader = new JsonReader(new StringReader(json));

    new DefaultDateTypeAdapter(Date.class).read(reader);
  }
}