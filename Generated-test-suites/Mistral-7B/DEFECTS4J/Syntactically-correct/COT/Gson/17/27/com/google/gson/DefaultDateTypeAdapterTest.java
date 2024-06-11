package com.google.gson.test;

import static org.junit.Assert.*;

import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson. TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

 private static final String TEST_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

 @Test
 public void testDefaultDateTypeAdapterConstructionWithDateClass() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals("DefaultDateTypeAdapter(Date.class)", dateTypeAdapter.toString());
 }

 @Test
 public void testDefaultDateTypeAdapterConstructionWithStringDatePattern() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, TEST_DATE_FORMAT);
 assertEquals("DefaultDateTypeAdapter(Date.class, SimpleDateFormat[dd-MM-yyyy HH:mm:ss])", dateTypeAdapter.toString());
 }

 @Test
 public void testDefaultDateTypeAdapterConstructionWithDateStyles() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 assertEquals("DefaultDateTypeAdapter(Date.class, DateFormat[DateFormat.SHORT, DateFormat.SHORT], DateFormat[DateFormat.SHORT, DateFormat.SHORT])", dateTypeAdapter.toString());
 }

 @Test
 public void testDefaultDateTypeAdapterConstructionWithLocaleUs() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals(Locale.US, dateTypeAdapter.enUsFormat.getLocale());
 }

 @Test
 public void testDefaultDateTypeAdapterConstructionWithLocaleLocal() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Locale.FRANCE);
 assertEquals(Locale.FRANCE, dateTypeAdapter.localFormat.getLocale());
 }

 @Test
 public void testDefaultDateTypeAdapterWriteDate() throws IOException {
 Date date = new Date();
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 dateTypeAdapter.write(jsonWriter, date);
 JsonElement jsonElement = new JsonParser().parse(new StringReader(stringWriter.toString()));
 assertEquals("\"\" + enUsFormat.format(date)\"\"", jsonElement.getAsJsonPrimitive().getAsString());
 }

 @Test
 public void testDefaultDateTypeAdapterWriteJavaSqlDate() throws IOException {
 java.sql.Date sqlDate = new java.sql.Date(1517041600000L); // 2018-01-01
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 dateTypeAdapter.write(jsonWriter, sqlDate);
 JsonElement jsonElement = new JsonParser().parse(new StringReader(stringWriter.toString()));
 assertEquals("\"2018-01-01\"", jsonElement.getAsJsonPrimitive().getAsString());
 }

 @Test
 public void testDefaultDateTypeAdapterWriteTimestamp() throws IOException {
 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 dateTypeAdapter.write(jsonWriter, timestamp);
 JsonElement jsonElement = new JsonParser().parse(new StringReader(stringWriter.toString()));
 assertEquals("\"20" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(timestamp) + "\"", jsonElement.getAsJsonPrimitive().getAsString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultDateTypeAdapterWithInvalidDateType() {
 new DefaultDateTypeAdapter(String.class);
 }

 @Test
 public void testDefaultDateTypeAdapterReadDate() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String dateString = "2018-01-01";
 StringWriter stringWriter = new StringWriter();
 stringWriter.write(dateString);
 JsonReader jsonReader = new JsonReader(new StringReader(stringWriter.toString()));
 Date date = dateTypeAdapter.read(jsonReader);
 assertEquals(dateString, dateTypeAdapter.enUsFormat.format(date));
 }

 @Test
 public void testDefaultDateTypeAdapterReadJavaSqlDate() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 String dateString = "2018-01-01";
 StringWriter stringWriter = new StringWriter();
 stringWriter.write(dateString);
 JsonReader jsonReader = new JsonReader(new StringReader(stringWriter.toString()));
 java.sql.Date sqlDate = (java.sql.Date) dateTypeAdapter.read(jsonReader);
 assertEquals(dateString, dateTypeAdapter.localFormat.format(sqlDate));
 }

 @Test
 public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 String timestampString = "2018-01-01T01:23:45.678Z";
 StringWriter stringWriter = new StringWriter();
 stringWriter.write(timestampString);
 JsonReader jsonReader = new JsonReader(new StringReader(stringWriter.toString()));
 Timestamp timestamp = (Timestamp) dateTypeAdapter.read(jsonReader);
 assertEquals(timestampString, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(timestamp));
 }

 @Test
 public void testDefaultDateTypeAdapterReadNull() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 jsonWriter.nullValue();
 JsonReader jsonReader = new JsonReader(new StringReader(stringWriter.toString()));
 Date date = dateTypeAdapter.read(jsonReader);
 assertNull(date);
 }

 @Test(expected = JsonParseException.class)
 public void testDefaultDateTypeAdapterReadInvalidDateFormat() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 StringWriter stringWriter = new StringWriter();
 stringWriter.write("InvalidDateFormat");
 JsonReader jsonReader = new JsonReader(new StringReader(stringWriter.toString()));
 dateTypeAdapter.read(jsonReader);
 }

 @Test
 public void testDefaultDateTypeAdapterReadInvalidJson() throws IOException, SQLException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 jsonWriter.value("InvalidJson");
 StringReader stringReader = new StringReader(stringWriter.toString());
 JsonReader jsonReader = new JsonReader(stringReader);
 jsonReader.setLenient(true);
 assertFalse(dateTypeAdapter.read(jsonReader) instanceof Timestamp);
 }
}