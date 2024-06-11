import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.gson.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 private static final Date DEFAULT_DATE = new Date();
 private static final Timestamp DEFAULT_TIMESTAMP = new Timestamp(System.currentTimeMillis());
 private static final java.sql.Date DEFAULT_SQL_DATE = new java.sql.Date(DEFAULT_DATE.getTime());

 private final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class))
                                           .registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class))
                                           .registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class))
                                           .create();

 @org.junit.Test
 public void testSerializationOfDate() {
   Date inputDate = new Date();
   String json = gson.toJson(inputDate);
   Date outputDate = gson.fromJson(json, Date.class);
   assertEquals(inputDate, outputDate);
 }

 @org.junit.Test
 public void testSerializationOfTimestamp() {
   Timestamp inputTimestamp = new Timestamp(System.currentTimeMillis());
   String json = gson.toJson(inputTimestamp);
   Timestamp outputTimestamp = gson.fromJson(json, Timestamp.class);
   assertEquals(inputTimestamp, outputTimestamp);
 }

 @org.junit.Test
 public void testSerializationOfSqlDate() {
   java.sql.Date inputSqlDate = new java.sql.Date(System.currentTimeMillis());
   String json = gson.toJson(inputSqlDate);
   java.sql.Date outputSqlDate = gson.fromJson(json, java.sql.Date.class);
   assertEquals(inputSqlDate, outputSqlDate);
 }

 @org.junit.Test
 public void testDeserializationOfDateWithValidString() throws IOException {
   String validDateString = "2022-12-31T12:23:45Z";
   Date inputDate = new Date();
   String json = gson.toJson(inputDate);
   Date outputDate = gson.fromJson(json + "," + validDateString, Date[].class)[1];
   assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(validDateString), outputDate);
 }

 @org.junit.Test
 public void testDeserializationOfTimestampWithValidString() throws IOException {
   String validTimestampString = "2022-12-31T12:23:45.678Z";
   Timestamp inputTimestamp = new Timestamp(System.currentTimeMillis());
   String json = gson.toJson(inputTimestamp);
   Timestamp outputTimestamp = gson.fromJson(json + "," + validTimestampString, Timestamp[].class)[1];
   assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(validTimestampString), outputTimestamp);
 }

 @org.junit.Test
 public void testDeserializationOfSqlDateWithValidString() throws IOException {
   String validSqlDateString = "2022-12-31";
   java.sql.Date inputSqlDate = new java.sql.Date(System.currentTimeMillis());
   String json = gson.toJson(inputSqlDate);
   java.sql.Date outputSqlDate = gson.fromJson(json + "," + validSqlDateString, java.sql.Date[].class)[1];
   assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(validSqlDateString), outputSqlDate);
 }

 @org.junit.Test
 public void testDeserializationOfInvalidString() {
   String invalidString = "abc";
   assertThrows(JsonSyntaxException.class, () -> gson.fromJson(invalidString, Date.class));
 }

 @org.junit.Test
 public void testDeserializationOfNull() throws IOException {
   String json = "null";
   assertNull(gson.fromJson(json, Date.class));
 }

 @org.junit.Test
 public void testDeserializationOfNullWithNullJson() {
   String json = null;
   assertNull(gson.fromJson(json, Date.class));
 }
}