package com.google.gson.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructor() {
    Class<Date> dateType = Date.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDatePattern() {
    Class<Date> dateType = Date.class;
    String datePattern = "yyyy-MM-dd HH:mm:ss";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType, datePattern);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapterWithInvalidDateType() {
    Class<String> dateType = String.class;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType);
    fail("Should throw IllegalArgumentException");
  }

  @Test
  public void testWriteMethod() throws IOException {
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, date);
    String expectedOutput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
    String actualOutput = jsonWriter.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testReadMethodWithValidDate() throws IOException {
    String dateString = "2022-03-15 14:30:59.123Z";
    Date date = new Date();
    JsonReader jsonReader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    date = adapter.read(jsonReader);
    assertEquals(dateString, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date));
  }

  @Test(expected = JsonParseException.class)
  public void testReadMethodWithInvalidDateFormat() throws IOException {
    String dateString = "InvalidDateFormat";
    JsonReader jsonReader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
    fail("Should throw JsonParseException");
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadMethodWithInvalidDate() throws IOException {
    String dateString = "InvalidDate";
    JsonReader jsonReader = new JsonReader(new StringReader(dateString));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
    fail("Should throw JsonSyntaxException");
  }

  @Test
  public void testReadMethodWithTimestamp() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    JsonReader jsonReader = new JsonReader(new StringReader(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(timestamp)));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Date date = adapter.read(jsonReader);
    assertEquals(timestamp, new java.sql.Timestamp(date.getTime()));
  }

  @Test
  public void testReadMethodWithSqlDate() throws IOException, SQLException {
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    JsonReader jsonReader = new JsonReader(new StringReader(new SimpleDateFormat("yyyy-MM-dd").format(sqlDate)));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    Date date = adapter.read(jsonReader);
    assertEquals(sqlDate, new java.sql.Date(date.getTime()));
  }

  @Test
  public void testDeserializeToDateMethodWithValidDateFormat() throws ParseException {
    String dateString = "2022-03-15 14:30:59.123Z";
    Date date = new DefaultDateTypeAdapter(DefaultDateTypeAdapter.class).deserializetoDate(dateString);
    assertEquals(dateString, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date));
  }

  @Test(expected = ParseException.class)
  public void testDeserializeToDateMethodWithInvalidDateFormat() throws ParseException {
    String dateString = "InvalidDateFormat";
    new DefaultDateTypeAdapter(DefaultDateTypeAdapter.class).deserializetoDate(dateString);
    fail("Should throw ParseException");
  }

  @Test
  public void testDeserializeToDateMethodWithISO8601Format() throws ParseException {
    String dateString = "2022-03-15T14:30:59.123Z";
    Date date = new DefaultDateTypeAdapter(DefaultDateTypeAdapter.class).deserializetoDate(dateString);
    assertEquals(dateString, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date));
  }

  @Test
  public void testToStringMethod() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    String expectedOutput = "DefaultDateTypeAdapter(java.text.SimpleDateFormat)";
    String actualOutput = adapter.toString();
    assertEquals(expectedOutput, actualOutput);
  }
}