import com.google.gson.*;
import org.junit.Test;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {

 private static final String TEST_DATE_STR_1 = "2022-03-01T12:34:56Z";
 private static final String TEST_DATE_STR_2 = "01-03-2022 12:34:56.123";
 private static final String TEST_DATE_STR_3 = "Jan 1, 2022 12:34:56 PM";
 private static final String TEST_DATE_STR_4 = "2022-12-31T23:59:59.999Z";
 private static final String TEST_DATE_STR_5 = "Thu, 01 Jan 2021 00:00:01 GMT";
 private static final String TEST_DATE_STR_6 = "Invalid date format";

 @Test
 public void testDefaultDateTypeAdapter_DefaultConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
 }

 @Test
 public void testDefaultDateTypeAdapter_StringConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss.SSS");
 }

 @Test
 public void testDefaultDateTypeAdapter_IntConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);
 }

 @Test
 public void testDefaultDateTypeAdapter_IntIntConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
 }

 @Test
 public void testWriteDate() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 Date date = new Date();
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 adapter.write(writer, date);
 writer.flush();
 }

 @Test
 public void testWriteTimestamp() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter(Timestamp.class);
 adapter.write(writer, timestamp);
 writer.flush();
 }

 @Test
 public void testWriteSqlDate() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
 DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 adapter.write(writer, sqlDate);
 writer.flush();
 }

 @Test
 public void testReadDate_ValidFormat_EnUsFormat() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_1));
 Date expectedDate = new Date();
 Date actualDate = adapter.read(reader);
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testReadDate_ValidFormat_LocalFormat() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSS");
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_2));
 Date expectedDate = new Date();
 Date actualDate = adapter.read(reader);
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testReadDate_ValidFormat_ISO8601Format() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_1));
 adapter.setLenient(true);
 Date expectedDate = new Date();
 Date actualDate = adapter.read(reader);
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testReadDate_NullValue() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader("null"));
 Date actualDate = adapter.read(reader);
 assertNull(actualDate);
 }

 @Test(expected = JsonParseException.class)
 public void testReadDate_InvalidFormat() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_6));
 adapter.read(reader);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadDate_InvalidFormatWithLenientFalse() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_6));
 adapter.setLenient(false);
 adapter.read(reader);
 }

 @Test
 public void testReadTimestamp() throws IOException, ParseException {
 DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter(Timestamp.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_1));
 Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
 Timestamp actualTimestamp = adapter.read(reader);
 assertEquals(expectedTimestamp, actualTimestamp);
 }

 @Test
 public void testReadSqlDate() throws IOException, ParseException {
 DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 JsonReader reader = new JsonReader(new StringReader(TEST_DATE_STR_1));
 java.sql.Date expectedSqlDate = new java.sql.Date(System.currentTimeMillis());
 java.sql.Date actualSqlDate = adapter.read(reader);
 assertEquals(expectedSqlDate, actualSqlDate);
 }

 @Test
 public void testReadNullValue() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 JsonReader reader = new JsonReader(new StringReader("null"));
 Date actualDate = adapter.read(reader);
 assertNull(actualDate);
 }
}