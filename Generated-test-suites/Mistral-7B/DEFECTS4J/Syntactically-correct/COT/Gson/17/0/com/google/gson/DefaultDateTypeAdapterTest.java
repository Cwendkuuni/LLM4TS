package com.google.gson.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
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
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.DefaultDateTypeAdapter; // Assuming the class is in the same package for testing purpose

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testStringConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("dd/MM/yyyy");
    assertNotNull(adapter);
    assertEquals("dd/MM/yyyy", adapter.enUsFormat.toPattern());
    assertEquals("dd/MM/yyyy", adapter.localFormat.toPattern());
  }

  @Test
  public void testIntConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    assertNotNull(adapter);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateTypeConstructor() {
    new DefaultDateTypeAdapter(String.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
  }

  @Test
  public void testWrite() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    String json = gson.toJson(date, adapter);
    Date newDate = gson.fromJson(json, Date.class);
    assertEquals(date, newDate);
  }

  @Test
  public void testReadDate() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-03-01";
    JsonElement jsonElement = new JsonParser().parse(new StringReader(dateString));
    Date date = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    assertEquals(expectedDate.getTime(), date.getTime());
  }

  @Test
  public void testReadTimestamp() throws IOException, JsonSyntaxException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    String timestampString = "2022-03-01 12:00:01";
    JsonElement jsonElement = new JsonParser().parse(new StringReader(timestampString));
    Timestamp timestamp = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
    Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis() - (15 * 60 * 1000)); // 15 minutes ago
    assertEquals(expectedTimestamp.getTime(), timestamp.getTime());
  }

  @Test
  public void testReadSqlDate() throws IOException, SQLException, JsonSyntaxException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    String sqlDateString = "2022-03-01";
    JsonElement jsonElement = new JsonParser().parse(new StringReader(sqlDateString));
    java.sql.Date sqlDate = (java.sql.Date) adapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
    java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(sqlDateString);
    assertEquals(utilDate.getTime(), sqlDate.getTime());
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidDate() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidDateString = "invalidDate";
    JsonReader jsonReader = new JsonReader(new StringReader(invalidDateString));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidDateFormat() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidDateFormatString = "InvalidFormat:MM/dd/yyyy";
    JsonElement jsonElement = new JsonParser().parse(new StringReader(invalidDateFormatString));
    adapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
  }

  @Test
  public void testDeserializeToDate() throws ParseException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    String dateFormatAsString = "2022-03-01";
    Date date = adapter.deserializeToDate(dateFormatAsString);
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormatAsString);
    assertEquals(expectedDate.getTime(), date.getTime());
  }

  @Test(expected = ParseException.class)
  public void testDeserializeToDateInvalidFormat() throws ParseException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    String invalidDateFormat = "InvalidFormat:MM/dd/yyyy";
    adapter.deserializeToDate(invalidDateFormat);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateInvalidJson() throws JsonSyntaxException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    String invalidJson = "{\"invalidJson\":\"not a date\"}";
    adapter.deserializeToDate(invalidJson);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.LONG, DateFormat.LONG);
    String toString = adapter.toString();
    assertTrue(toString.contains("DefaultDateTypeAdapter(Date)"));
  }
}