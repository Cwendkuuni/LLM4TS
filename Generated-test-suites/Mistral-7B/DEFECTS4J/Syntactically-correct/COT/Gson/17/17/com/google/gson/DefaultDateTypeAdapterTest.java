import com.google.gson.DateTypeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

 private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

 @Test
 public void testDefaultConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 Assert.assertNotNull(defaultDateTypeAdapter);
 }

 @Test
 public void testStringToDateWithUSFormat() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 Date date = dateTypeAdapter.read(createJsonReaderWithString("2022-01-01T00:00:00.000Z"));
 Assert.assertNotNull(date);
 Assert.assertEquals("Wed Jan 05 00:00:00 EST 2022", dateTypeAdapter.toString(date));
 }

 @Test
 public void testStringToDateWithLocalFormat() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy HH:mm:ss");
 Date date = dateTypeAdapter.read(createJsonReaderWithString("01/01/2022 12:00:00"));
 Assert.assertNotNull(date);
 Assert.assertEquals("01/01/2022 12:00:00", dateTypeAdapter.toString(date));
 }

 @Test
 public void testStringToDateWithISOFormat() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 Date date = dateTypeAdapter.read(createJsonReaderWithString("2022-01-01T00:00:00.000Z"));
 Assert.assertNotNull(date);
 Assert.assertEquals("2022-01-01 00:00:00.000", dateTypeAdapter.toString(date));
 }

 @Test(expected = JsonParseException.class)
 public void testStringToDateWithInvalidFormat() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 dateTypeAdapter.read(createJsonReaderWithString("Invalid format"));
 }

 @Test
 public void testSerialization() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, new Date());
 jsonWriter.flush();
 JsonElement jsonElement = JsonParser.parseString("{\"date\": \"Wed Jan 05 00:00:00 EST 2022\"}");
 Date readDate = dateTypeAdapter.read(jsonElement.getAsJsonObject().get("date").getAsJsonPrimitive().getAsString());
 Assert.assertEquals("Wed Jan 05 00:00:00 EST 2022", dateTypeAdapter.toString(readDate));
 }

 @Test
 public void testSerializationWithTimestamp() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 long timestamp = System.currentTimeMillis();
 dateTypeAdapter.write(jsonWriter, new Timestamp(timestamp));
 jsonWriter.flush();
 JsonElement jsonElement = JsonParser.parseString("{\"timestamp\": \"1970-01-01 01:30:23.756+00:00\"}");
 Timestamp readTimestamp = (Timestamp) dateTypeAdapter.read(jsonElement.getAsJsonObject().get("timestamp").getAsJsonPrimitive().getAsString());
 Assert.assertEquals("1970-01-01 01:30:23.756", dateTypeAdapter.toString(readTimestamp));
 Assert.assertEquals(timestamp, readTimestamp.getTime());
 }

 @Test
 public void testSerializationWithSqlDate() throws SQLException, IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
 dateTypeAdapter.write(jsonWriter, sqlDate);
 jsonWriter.flush();
 JsonElement jsonElement = JsonParser.parseString("{\"sqlDate\": \"2022-01-05\"}");
 java.sql.Date readSqlDate = (java.sql.Date) dateTypeAdapter.read(jsonElement.getAsJsonObject().get("sqlDate").getAsJsonPrimitive().getAsString());
 Assert.assertEquals("2022-01-05", dateTypeAdapter.toString(readSqlDate));
 }

 @Test
 public void testDeserializationWithNull() throws IOException {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 JsonElement jsonElement = JsonParser.parseString("{\"date\": null}");
 Assert.assertNull(dateTypeAdapter.read(jsonElement.getAsJsonObject().get("date").getAsJsonNull()));
 }

 private JsonReader createJsonReaderWithString(String jsonString) {
 return new JsonReader(new StringReader(jsonString));
}

}