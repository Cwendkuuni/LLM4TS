package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter dateAdapter;
 private Gson gson;

 @Before
 public void setUp() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class);
 gson = new GsonBuilder().setLenient().create();
 }

 @Test
 public void testDefaultConstructor() {
 dateAdapter = new DefaultDateTypeAdapter();
 assertEquals(Date.class, dateAdapter.dateType);
 assertNull(dateAdapter.enUsFormat);
 assertNull(dateAdapter.localFormat);
 }

 @Test
 public void testConstructorWithDateType() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 }

 @Test
 public void testConstructorWithDateFormat() {
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
 dateAdapter = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 assertSame(dateFormat, dateAdapter.enUsFormat);
 assertSame(dateFormat, dateAdapter.localFormat);
 }

 @Test
 public void testConstructorWithStyle() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE_TYPE, Date.TIME_TYPE);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 }

 @Test
 public void testConstructorWithDateStyleAndTimeStyle() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class, Date.ABBREVIATED_MONTH, Date.SHORT);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 }

 @Test
 public void testWriteNullValue() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 dateAdapter.write(writer, null);
 writer.flush();
 assertNull(writer.peek());
 }

 @Test
 public void testWriteValidDate() throws IOException {
 Date date = new Date();
 JsonWriter writer = new JsonWriter(System.out);
 dateAdapter.write(writer, date);
 writer.flush();
 assertEquals("2022-12-12 12:12:12", writer.toString());
 }

 @Test
 public void testWriteInvalidDateFormat() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 Date date = new Date();
 dateAdapter = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy HH:mm:ss");
 dateAdapter.write(writer, date);
 writer.flush();
 assertThrows(JsonSyntaxException.class, () -> writer.toString());
 }

 @Test
 public void testReadNullValue() throws IOException {
 JsonElement jsonElement = JsonParser.parseString("null");
 Date date = dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
 assertNull(date);
 }

 @Test
 public void testReadValidDate() throws IOException {
 String jsonString = "\"2022-12-12 12:12:12\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 Date date = dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
 assertEquals("Mon Dec 12 12:12:12 CST 2022", new SimpleDateFormat("E d MMM d h:mm:ss a z 'GMT' z", Locale.US).format(date));
 }

 @Test
 public void testReadInvalidDateFormat() throws IOException {
 String jsonString = "\"dd/MM/yyyy HH:mm:ss\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 assertThrows(JsonSyntaxException.class, () -> dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))));
 }

 @Test
 public void testReadInvalidDateFormatWithCustomParser() throws IOException, SQLException, ParseException {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 dateAdapter = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat) {
 @Override
 Date deserializeToDate(String s) {
 try {
 return super.deserializeToDate(s);
 } catch (ParseException e) {
 throw new SQLException(e.getMessage());
 }
 }
 };
 String jsonString = "\"dd/MM/yyyy HH:mm:ss\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 assertThrows(SQLException.class, () -> dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))));
 }

 @Test
 public void testReadValidDateWithCustomParser() throws IOException, ParseException {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 dateAdapter = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat) {
 @Override
 Date deserializeToDate(String s) {
 try {
 return dateFormat.parse(s);
 } catch (ParseException e) {
 throw new JsonSyntaxException(s, e);
 }
 }
 };
 String jsonString = "\"dd/MM/yyyy HH:mm:ss\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 Date date = dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getTime();
 assertEquals("Mon Dec 12 12:12:12 CST 2022", new SimpleDateFormat("E d MMM d h:mm:ss a z 'GMT' z", Locale.US).format(date));
 }

 @Test
 public void testReadValidTimestamp() throws IOException {
 String jsonString = "\"2022-12-12 12:12:12.123\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 Timestamp timestamp = (Timestamp) dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getValue();
 assertEquals("Mon Dec 12 12:12:12 GMT 2022", new SimpleDateFormat("E d MMM d h:mm:ss z yyyy", Locale.US).format(timestamp));
 }

 @Test
 public void testReadValidSQlDate() throws IOException, SQLException, ParseException {
 String jsonString = "\"2022-12-12\"";
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 java.sql.Date sqlDate = (java.sql.Date) dateAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString()))).getValue();
 assertEquals("Mon Dec 12 00:00:00 CST 2022", new SimpleDateFormat("E d MMM d 'yyyy'", Locale.US).format(sqlDate));
 }

 @Test
 public void testReadInvalidJsonSyntaxException() throws IOException {
 assertThrows(JsonSyntaxException.class, () -> dateAdapter.read(new JsonReader(new StringReader(""))));
 }

 @Test
 public void testReadInvalidJsonToken() throws IOException {
 assertThrows(IllegalArgumentException.class, () -> dateAdapter.read(new JsonReader(new StringReader("[]"))));
 }

 @Test
 public void testToString() {
 assertEquals("DefaultDateTypeAdapter[enUsFormat=DateFormat{format=\"Mon dd, yyyy hh:mm:ss a z\", lenient=false, timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], useUTC=false], localFormat=DateFormat{format=\"Mon dd, yyyy hh:mm:ss a z\", lenient=false, timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000, saved_default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], default_timezone=sun.util.calendar.ZoneInfo[id=America/Chicago, offset=1800000, dbtime=1449648000000], useUTC=false}]", dateAdapter.toString());
 }
}