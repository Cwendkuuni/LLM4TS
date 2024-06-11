import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RunWith(JUnit4.class)
public class DefaultDateTypeAdapterTest {

 private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

 @Test
 public void testDefaultConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 }

 @Test
 public void testStringDatePatternConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSS Z");
 }

 @Test
 public void testDateStyleIntConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DEFAULT, DateFormat.DEFAULT);
 }

 @Test
 public void testWriteDateToJsonTest() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 Date date = new Date();
 String json = "";
 try (JsonWriter writer = new JsonWriter(new StringBuilder(json))) {
 adapter.write(writer, date);
 json = writer.toString();
 }
 JsonElement jsonElement = JsonParser.parseString(json);
 Assert.assertEquals("2023-03-16 12:34:56.789 Z", jsonElement.getAsJsonPrimitive().getAsString());
 }

 @Test
 public void testReadJsonDateTest() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 String json = "\"2023-03-16 12:34:56.789 Z\"";
 Date date = adapter.read(new JsonReader(new StringBuilder(json).append("{").append("\"json\"").append(":}").toString()));
 Assert.assertEquals(new Date(1678925096789L), date);
 }

 @Test
 public void testReadJsonTimestampTest() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
 String json = "\"2023-03-16 12:34:56.789 Z\"";
 Timestamp timestamp = adapter.read(new JsonReader(new StringBuilder(json).append("{").append("\"json\"").append(":}").toString()));
 Assert.assertEquals(new Timestamp(1678925096789L), timestamp);
 }

 @Test
 public void testReadJsonSqlDateTest() throws IOException {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 String json = "\"2023-03-16\"";
 java.sql.Date sqlDate = adapter.read(new JsonReader(new StringBuilder(json).append("{").append("\"json\"").append(":}").toString()));
 Assert.assertEquals(new java.sql.Date(1678925000000L), sqlDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDateTypeConstructor() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(String.class);
 }

 @Test
 public void testDeserializeToDateNullString() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 Assert.assertNull(adapter.deserializeToDate(null));
 }

 @Test
 public void testDeserializeToDateEmptyString() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 Assert.assertNull(adapter.deserializeToDate(""));
 }

 @Test
 public void testDeserializeToDateInvalidFormat() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 Assert.assertThrows(JsonSyntaxException.class, () -> adapter.deserializetoDate("invalid format"));
 }

 @Test
 public void testDeserializeToDateFormatUs() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("2023-03-16 12:34:56.789 Z"));
 }

 @Test
 public void testDeserializeToDateFormatLocal() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Locale.FRANCE);
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("16/03/2023 12:34:56.789"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithLongDatePattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "MMMM dd, yyyy HH:mm:ss z");
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("March 16, 2023 12:34:56.789 Z"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithShortDatePattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "MM/dd/yyyy HH:mm:ss z");
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("03/16/2023 12:34:56.789 Z"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithShortTimePattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "MM/dd/yyyy h:mm a z");
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("03/16/2023 12:34 PM Z"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithLongTimePattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "MM/dd/yyyy h:mm:ss a z");
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("03/16/2023 12:34:56 PM Z"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithCustomPattern() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss:SSS");
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("16-03-2023 12:34:56:789"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithMissingColon() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "ddMMyyyy HH:mm:ss"); // missing colon between MM and yyyy
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("16032023 12:34:56.789"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithMissingColon2() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HHmmss.SSS"); // missing colon between MM and yyyy
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("16-03-2023 123456.789"));
 }

 @Test
 public void testDeserializeToDateFormatEnUsWithMissingColon3() {
 DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSSZ"); // missing colon between MM and yyyy
 Assert.assertEquals(new Date(1678925096789L), adapter.deserializetoDate("16-03-2023 12:34:56.789+00:00"));
 }
}