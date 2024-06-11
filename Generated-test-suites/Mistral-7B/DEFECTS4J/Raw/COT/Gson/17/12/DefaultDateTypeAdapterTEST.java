package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructor1() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(Date.class)", adapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructor2() {
    String datePattern = "dd-MM-yyyy HH:mm:ss";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
    assertEquals("DefaultDateTypeAdapter(Date.class, " + datePattern + ")", adapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructor3() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.SHORT;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(int.class, dateStyle, timeStyle);
    assertEquals("DefaultDateTypeAdapter(int, " + DateFormat.SHORT + ", " + DateFormat.SHORT + ")", adapter.toString());
  }

  @Test
  public void testDefaultDateTypeAdapterConstructor4() {
    int dateStyle = DateFormat.LONG;
    int timeStyle = DateFormat.LONG;
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(int.class, dateStyle, timeStyle);
    assertEquals("DefaultDateTypeAdapter(int, " + DateFormat.LONG + ", " + DateFormat.LONG + ")", adapter.toString());
  }

  @Test
  public void testWriteDate() throws IOException {
    Gson gson = new Gson();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(jsonWriter, date);
    jsonWriter.flush();
    assertEquals("Expected written date to be valid", date.toString(), System.out.getBytes().toString());
  }

  @Test
  public void testWriteDateNull() throws IOException {
    Gson gson = new Gson();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(jsonWriter, null);
    jsonWriter.flush();
    assertNull("Expected null to be written", System.out.getBytes());
  }

  @Test
  public void testReadDateEmptyString() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(""));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertNull("Expected null when reading an empty string", adapter.read(jsonReader));
  }

  @Test
  public void testReadDateValidString() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31 23:59:59"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(jsonReader);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    assertEquals("Expected read date to be valid", formatter.format(date), "2022-12-31 23:59:59");
  }

  @Test(expected = JsonParseException.class)
  public void testReadDateInvalidStringNoFormat() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("abc"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadDateInvalidStringFormat() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-32 23:59:59"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
  }

  @Test(expected = JsonIOException.class)
  public void testReadDateIOException() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(""));
    jsonReader.setLenient(false);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
  }

  @Test
  public void testReadDateDate() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31 23:59:59"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.read(jsonReader);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    assertEquals("Expected read date to be valid", formatter.format(date), "2022-12-31 23:59:59");
  }

  @Test
  public void testReadDateTimestamp() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31 23:59:59.123"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = (Timestamp) adapter.read(jsonReader);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    assertEquals("Expected read Timestamp to be valid", formatter.format(timestamp.toDate()), "2022-12-31 23:59:59.123");
  }

  @Test
  public void testReadDateSqlDate() throws IOException, SQLException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = (java.sql.Date) adapter.read(jsonReader);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    assertEquals("Expected read sqlDate to be valid", formatter.format(sqlDate), "2022-12-31");
  }

  @Test(expected = JsonParseException.class)
  public void testReadDateInvalidJson() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("[1,2,3]"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(jsonReader);
  }

  @Test(expected = ClassCastException.class)
  public void testReadDateInvalidClass() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31 23:59:59"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(String.class);
    adapter.read(jsonReader);
  }

  @Test(expected = AssertionError.class)
  public void testReadDateUnexpectedDateType() throws IOException {
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("2022-12-31 23:59:59"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(int.class);
    adapter.read(jsonReader);
  }
}