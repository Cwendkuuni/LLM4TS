import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 @Test
 public void testDefaultDateTypeAdapter() {
 // Test constructor with Date type
 DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

 // Test writing and reading Date
 Date date = new Date();
 String json = "\"2022-12-31 12:00:00\""; // a valid date string

 // Write
 JsonWriter writer = new JsonWriter(System.out);
 dateAdapter.write(writer, date);
 writer.flush();
 Assert.assertEquals(json, writer.toString());

 // Read
 JsonReader reader = new JsonReader(new StringReader(json));
 Date readDate = dateAdapter.read(reader);
 Assert.assertEquals(date, readDate);

 // Test writing and reading Timestamp
 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 json = "\"2022-12-31T12:00:00.000Z\""; // a valid timestamp string with ISO8601 format

 // Write
 writer = new JsonWriter(System.out);
 dateAdapter.write(writer, timestamp);
 writer.flush();
 Assert.assertEquals(json, writer.toString());

 // Read
 reader = new JsonReader(new StringReader(json));
 readDate = dateAdapter.read(reader);
 Assert.assertEquals(timestamp, readDate);

 // Test writing and reading java.sql.Date
 java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
 json = "\"2022-12-31\""; // a valid date string without time component

 // Write
 writer = new JsonWriter(System.out);
 dateAdapter.write(writer, sqlDate);
 writer.flush();
 Assert.assertEquals(json, writer.toString());

 // Read
 reader = new JsonReader(new StringReader(json));
 readDate = dateAdapter.read(reader);
 Assert.assertEquals(sqlDate, readDate);
 }

 @Test
 public void testDefaultDateTypeAdapter_InvalidString() {
 // Test deserialization with an invalid string
 DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
 String json = "{\"invalidString\":\"invalidDate\"}";

 JsonParser parser = new JsonParser();
 JsonElement jsonElement = parser.parse(json);
 JsonReader reader = new JsonReader(new StringReader(jsonElement.getAsJsonObject().get("invalidString").getAsString()));

 try {
 dateAdapter.read(reader);
 Assert.fail();
 } catch (JsonSyntaxException e) {
 // expected
 }
 }

 @Test
 public void testDefaultDateTypeAdapter_NullValue() {
 // Test deserialization with a null value
 DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
 String json = "{\"date\":null}";

 JsonParser parser = new JsonParser();
 JsonElement jsonElement = parser.parse(json);
 JsonReader reader = new JsonReader(new StringReader(jsonElement.getAsJsonObject().get("date").isJsonNull() ? "" : jsonElement.getAsJsonObject().get("date").getAsString()));

 try {
 dateAdapter.read(reader);
 } catch (IOException e) {
 // expected, since read() returns null when the value is null
 }
 }

 @Test
 public void testDefaultDateTypeAdapter_CustomDateFormat() {
 // Test writing and reading Date using custom date format
 DefaultDateTypeAdapter customDateAdapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
 Date date = new Date();
 String json = "\"31-12-2022 12:00:00\"";

 // Write
 JsonWriter writer = new JsonWriter(System.out);
 customDateDateAdapter.write(writer, date);
 writer.flush();
 Assert.assertEquals(json, writer.toString());

 // Read
 JsonReader reader = new JsonReader(new StringReader(json));
 Date readDate = customDateDateAdapter.read(reader);
 Assert.assertEquals(date, readDate);
 }

 @Test
 public void testDefaultDateTypeAdapter_CustomDateFormatInvalid() {
 // Test deserialization with an invalid string using custom date format
 DefaultDateTypeAdapter customDateAdapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
 String json = "\"31-12-2022 12:00:00invalid\"";

 JsonParser parser = new JsonParser();
 JsonElement jsonElement = parser.parse(json);
 JsonReader reader = new JsonReader(new StringReader(jsonElement.getAsJsonObject().get("date").getAsString()));

 try {
 customDateDateAdapter.read(reader);
 Assert.fail();
 } catch (JsonSyntaxException e) {
 // expected
 }
 }
}

class StringReader {
 private String buf;
 private int pos;

 StringReader(String s) {
 buf = s;
 }

 @Override
 public int read() throws IOException {
 if (pos < buf.length()) {
 return buf.charAt(pos++) & 0xff;
 }
 return -1;
 }
}