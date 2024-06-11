import static org.junit.Assert.*;
import com.google.gson.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

 private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

 @Test
 public void testDefaultConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 assertNotNull(adapter);
 }

 @Test
 public void testConstructorWithDatePattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
 assertNotNull(adapter);
 }

 @Test
 public void testConstructorWithDateStyleAndTimeStyle() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 assertNotNull(adapter);
 }

 @Test
 public void testToString() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 String expectedResult = SIMPLE_NAME + "(...";
 String result = adapter.toString();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testWriteToJson() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 Date date = new Date();
 StringWriter stringWriter = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(stringWriter);
 adapter.write(jsonWriter, date);
 String expectedResult = "dd-MM-yyyy HH:mm:ss"; // Replace with the expected date format
 String result = stringWriter.toString();
 assertTrue(result.contains(expectedResult));
 }

 @Test
 public void testReadFromJson_ValidDate() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 String jsonDate = "25-12-2022 12:00:00";
 Date expectedDate = new Date(1671446400000L); // Convert json string to milliseconds and create Date object
 Date date = adapter.read(new JsonReader(new StringReader(jsonDate)));
 assertEquals(expectedDate, date);
 }

 @Test
 public void testReadFromJson_InvalidFormat() throws IOException, ParseException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 String jsonDate = "InvalidDateFormat";
 try {
 Date date = adapter.read(new JsonReader(new StringReader(jsonDate)));
 fail("Expected JsonSyntaxException to be thrown");
 } catch (JsonSyntaxException e) {
 // Expected
 }
 }

 @Test
 public void testReadFromJson_NullValue() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 JsonReader jsonReader = new JsonReader(new StringReader("null"));
 try {
 Date date = adapter.read(jsonReader);
 assertNull(date);
 } catch (IOException e) {
 fail("Expected null value, but received IOException");
 }
 }

 @Test
 public void testReadFromJson_Timestamp() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class, Calendar.SHORT, Calendar.SHORT);
 String jsonTimestamp = "2022-12-25 12:00:00.123456";
 Timestamp expectedTimestamp = new Timestamp(1671446400123L); // Convert json string to milliseconds and create Timestamp object
 Timestamp timestamp = adapter.read(new JsonReader(new StringReader(jsonTimestamp)));
 assertEquals(expectedTimestamp, timestamp);
 }

 @Test
 public void testReadFromJson_SQLDate() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class, Calendar.SHORT, Calendar.SHORT);
 String jsonDate = "2022-12-25";
 java.sql.Date expectedDate = new java.sql.Date(1671446400000L); // Convert json string to milliseconds and create SQLDate object
 java.sql.Date date = adapter.read(new JsonReader(new StringReader(jsonDate)));
 assertEquals(expectedDate, date);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidDateType() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(String.class, Calendar.SHORT, Calendar.SHORT);
 fail("Expected IllegalArgumentException but received null");
 }
}