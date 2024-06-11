package com.google.gson.tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_USLocale() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);

    Date originalDate = new Date(1516041620000L); // Fri, 21 Sep 2018 12:00:00 GMT

    String jsonDateString = "2018-09-21T12:00:00Z";
    JsonElement jsonDate = JsonParser.parseString(jsonDateString);

    Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonDate.getAsString()))).getTime();

    assertEquals(originalDate.getTime(), deserializedDate.getTime());
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidFormat() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);

    String jsonInvalidDateString = "xxxx-xx-xxTxx:xx:xxZ"; // Invalid date format

    JsonReader jsonReader = new JsonReader(new StringReader(jsonInvalidDateString));

    dateAdapter.read(jsonReader);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_NullValue() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);

    JsonReader jsonReader = new JsonReader(new StringReader("null"));

    dateAdapter.read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapter_CustomDateFormat() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");

    Date originalDate = new Date(1516041620000L); // Fri, 21 Sep 2018 12:00:00 GMT

    String jsonDateString = "21-09-2018 12:00:00";
    JsonElement jsonDate = JsonParser.parseString(jsonDateString);

    Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonDate.getAsString()))).getTime();

    assertEquals(originalDate.getTime(), deserializedDate.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapter_CustomDateStyles() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY, Date.MINUTE, Date.SECOND);

    Date originalDate = new Date(1516041620000L); // Fri, 21 Sep 2018 12:00:00 GMT

    String jsonDateString = "21-September-18 12:00";
    JsonElement jsonDate = JsonParser.parseString(jsonDateString);

    Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonDate.getAsString()))).getTime();

    assertEquals(originalDate.getTime(), deserializedDate.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapter_Timestamp() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Timestamp.class);

    Date originalDate = new Date(1516041620000L); // Fri, 21 Sep 2018 12:00:00 GMT
    Timestamp originalTimestamp = new Timestamp(originalDate.getTime());

    String jsonTimestampString = "2018-09-21T12:00:00Z";
    JsonElement jsonTimestamp = JsonParser.parseString(jsonTimestampString);

    Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonTimestamp.getAsString()))).getTime();

    Timestamp deserializedTimestamp = new Timestamp(deserializedDate.getTime());

    assertEquals(originalTimestamp.getTime(), deserializedTimestamp.getTime());
  }

  @Test
  public void testDefaultDateTypeAdapter_SQLDate() {
    TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    Date originalDate = new Date(1516044800000L); // Sat, 22 Sep 2018 00:00:00 GMT
    java.sql.Date originalSQlDate = new java.sql.Date(originalDate.getTime());

    String jsonSQlDateString = "2018-09-22";
    JsonElement jsonSQlDate = JsonParser.parseString(jsonSQlDateString);

    Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonSQlDate.getAsString()))).getTime();

    java.sql.Date deserializedSQlDate = new java.sql.Date(deserializedDate.getTime());

    assertEquals(originalSQlDate, deserializedSQlDate);
  }
}