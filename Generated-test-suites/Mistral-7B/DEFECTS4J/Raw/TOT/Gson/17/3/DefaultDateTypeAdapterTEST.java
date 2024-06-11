package com.google.gson.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

 private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

 @Test
 public void testDefaultDateTypeAdapter_defaultConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter();
 Class<? extends Date> dateType = Date.class;
 DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
 DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
 assertEquals(dateType, defaultDateTypeAdapter.dateType);
 assertEquals(enUsFormat, defaultDateTypeAdapter.enUsFormat);
 assertEquals(localFormat, defaultDateTypeAdapter.localFormat);
 }

 @Test
 public void testDefaultDateTypeAdapter_stringDatePatternConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 Class<? extends Date> dateType = Date.class;
 DateFormat enUsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
 DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 assertEquals(dateType, defaultDateTypeAdapter.dateType);
 assertEquals(enUsFormat, defaultDateTypeAdapter.enUsFormat);
 assertEquals(localFormat, defaultDateTypeAdapter.localFormat);
 }

 @Test
 public void testDefaultDateTypeAdapter_intStylesConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(DateFormat.ABBREVIATED, DateFormat.SHORT);
 Class<? extends Date> dateType = Date.class;
 DateFormat enUsFormat = DateFormat.getDateInstance(DateFormat.ABBREVIATED, Locale.US);
 DateFormat localFormat = DateFormat.getDateInstance(DateFormat.ABBREVIATED, Locale.US);
 assertEquals(dateType, defaultDateTypeAdapter.dateType);
 assertEquals(enUsFormat, defaultDateTypeAdapter.enUsFormat);
 assertEquals(localFormat, defaultDateTypeAdapter.localFormat);
 }

 @Test
 public void testDefaultDateTypeAdapter_intDateStyleIntTimeStyleConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(DateFormat.LONG, DateFormat.LONG);
 Class<? extends Date> dateType = Date.class;
 DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.US);
 DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
 assertEquals(dateType, defaultDateTypeAdapter.dateType);
 assertEquals(enUsFormat, defaultDateTypeAdapter.enUsFormat);
 assertEquals(localFormat, defaultDateTypeAdapter.localFormat);
 }

 @Test
 public void testDefaultDateTypeAdapter_writeNullValue() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 Date date = null;
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OfOutputStream(outputStream));
 defaultDateTypeAdapter.write(jsonWriter, date);
 JsonElement jsonElement = new JsonParser().parse(new ByteArrayInputStream(outputStream.toByteArray()));
 JsonNull jsonNull = JsonNull.INSTANCE;
 assertEquals(jsonNull, jsonElement);
 }

 @Test
 public void testDefaultDateTypeAdapter_writeValidDate() throws IOException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 Date date = new Date();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OfOutputStream(outputStream));
 defaultDateTypeAdapter.write(jsonWriter, date);
 String jsonString = outputStream.toString();
 assertEquals("\"2023-03-29T14:37:37.234-05:00\"", jsonString.trim());
 }

 @Test
 public void testDefaultDateTypeAdapter_readNullValue() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream("null".getBytes()));
 try {
 Date date = defaultDateTypeAdapter.read(jsonReader);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDefaultDateTypeAdapter_readValidDate() throws IOException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream("\"2023-03-29T14:37:37.234-05:00\"".getBytes()));
 Date date = defaultDateTypeAdapter.read(jsonReader);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDefaultDateTypeAdapter_readValidTimestamp() throws IOException, SQLException, ParseException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream("\"2023-03-29T14:37:37.234-05:00\"".getBytes()));
 Timestamp timestamp = defaultDateTypeAdapter.read(jsonReader);
 assertEquals(new Timestamp(1679442157234L), timestamp);
 }

 @Test
 public void testDefaultDateTypeAdapter_readValidSqlDate() throws IOException, SQLException, ParseException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream("\"2023-03-29\"".getBytes()));
 java.sql.Date sqlDate = defaultDateTypeAdapter.read(jsonReader);
 assertEquals(new java.sql.Date(1679442157000L), sqlDate);
 }

 @Test(expected = AssertionError.class)
 public void testDefaultDateTypeAdapter_readInvalidDate() throws IOException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream("invalid".getBytes()));
 try {
 defaultDateTypeAdapter.read(jsonReader);
 fail();
 } catch (JsonSyntaxException ignored) {
 // expected
 } catch (AssertionError e) {
 throw new AssertionError("Unexpected assertion error: " + e.getMessage());
 }
 }

 @Test
 public void testDefaultDateTypeAdapter_deserializeToDate_validDateFormat() throws ParseException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String dateString = "2023-03-29T14:37:37.234-05:00";
 Date date = defaultDateTypeAdapter.deserializeToDate(dateString);
 assertEquals(new Date(), date);
 }

 @Test(expected = ParseException.class)
 public void testDefaultDateTypeAdapter_deserializeToDate_invalidDateFormat() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String dateString = "invalid";
 try {
 defaultDateTypeAdapter.deserializeToDate(dateString);
 fail();
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testDefaultDateTypeAdapter_deserializeToDate_validISO8601DateFormat() throws ParseException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String dateString = "2023-03-29T14:37:37.234Z";
 Date date = defaultDateTypeAdapter.deserializeToDate(dateString);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDefaultDateTypeAdapter_deserializeToDate_validISO8601TimestampFormat() throws ParseException, SQLException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 String dateString = "2023-03-29T14:37:37.234Z";
 Timestamp timestamp = defaultDateTypeAdapter.deserializeToDate(dateString);
 assertEquals(new Timestamp(1679442157234L), timestamp);
 }

 @Test
 public void testDefaultDateTypeAdapter_deserializeToDate_validISOSqlDateFormat() throws ParseException, SQLException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 String dateString = "2023-03-29";
 java.sql.Date sqlDate = defaultDateTypeAdapter.deserializeToDate(dateString);
 assertEquals(new java.sql.Date(1679442157000L), sqlDate);
 }

 @Test(expected = JsonIOException.class)
 public void testDefaultDateTypeAdapter_deserializeToDate_invalidJsonFormat() throws IOException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String jsonString = "[1]";
 try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter jsonWriter = new JsonWriter(new JsonWriter.OfOutputStream(outputStream))) {
 jsonWriter.beginArray();
 jsonWriter.value(1);
 jsonWriter.endArray();
 JsonElement jsonElement = new JsonParser().parse(new ByteArrayInputStream(outputStream.toByteArray()));
 jsonWriter.close();
 Date date = defaultDateTypeAdapter.deserializeToDate(jsonElement.getAsString());
 fail();
 } catch (JsonIOException e) {
 // expected
 }
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDefaultDateTypeAdapter_deserializeToDate_parseException() throws IOException, ParseException {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 String dateString = "2023-03-29T14:37:37.234000";
 try {
 Date date = defaultDateTypeAdapter.deserializeToDate(dateString);
 fail();
 } catch (ParseException e) {
 // expected
 } catch (JsonSyntaxException e) {
 throw new JsonSyntaxException("Unexpected JsonSyntaxException: " + e.getMessage());
 }
 }
}