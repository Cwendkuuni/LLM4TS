import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter adapter;

 @Before
 public void setUp() {
 try {
 adapter = new DefaultDateTypeAdapter(Date.class);
 } catch (IllegalArgumentException e) {
 fail("Failed to create DefaultDateTypeAdapter with Date class");
 }
 }

 @Test
 public void testWriteDate() throws IOException {
 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 adapter.write(jsonWriter, new Date(1611245600000L));
 String jsonOutput = writer.getBuffer().toString();
 assertEquals("\"2020-11-25 12:00:00\"", jsonOutput);
 }

 @Test
 public void testReadDateFromValidJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-02-15 16:33:23\"}";
 JsonParser jsonParser = new JsonParser();
 JsonElement jsonElement = jsonParser.parse(new StringReader(jsonInput));
 Date parsedDate = adapter.read(jsonParser.peek(jsonElement.getAsJsonObject()));
 assertEquals(new Date(1613440223000L), parsedDate);
 }

 @Test(expected = JsonParseException.class)
 public void testReadDateFromInvalidJson() throws IOException {
 String jsonInput = "{\"date\":5}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 adapter.read(jsonParser.peek(jsonParser.peek().getAsJsonObject()));
 fail("Expected JsonParseException but received none");
 }

 @Test
 public void testReadDateFromIsoFormatJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-03-18T13:30:50.123Z\"}";
 JsonParser jsonParser = new JsonParser();
 JsonElement jsonElement = jsonParser.parse(new StringReader(jsonInput));
 Date parsedDate = adapter.read(jsonParser.peek(jsonElement.getAsJsonObject()));
 assertEquals(new Date(1615941650123L), parsedDate);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadDateFromInvalidIsoFormatJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-03-18T13:30:50.123ZX\"}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 fail("Expected JsonSyntaxException but received none");
 }

 @Test
 public void testWriteTimestamp() throws IOException {
 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 adapter = new DefaultDateTypeAdapter(Timestamp.class);
 adapter.write(jsonWriter, new Timestamp(1613440223000L));
 String jsonOutput = writer.getBuffer().toString();
 assertEquals("\"2021-02-15 16:33:23.000\"", jsonOutput);
 }

 @Test
 public void testReadTimestampFromValidJson() throws IOException {
 String jsonInput = "{\"timestamp\":\"2021-03-18 13:30:50.123\"}";
 JsonParser jsonParser = new JsonParser();
 JsonElement jsonElement = jsonParser.parse(new StringReader(jsonInput));
 Timestamp parsedTimestamp = (Timestamp) adapter.read(jsonParser.peek(jsonElement.getAsJsonObject()));
 assertEquals(new Timestamp(1615941650123L), parsedTimestamp);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadTimestampFromInvalidJson() throws IOException {
 String jsonInput = "{\"timestamp\":5}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 fail("Expected JsonSyntaxException but received none");
 }

 @Test
 public void testReadTimestampFromIsoFormatJson() throws IOException {
 String jsonInput = "{\"timestamp\":\"2021-03-18T13:30:50.123Z\"}";
 JsonParser jsonParser = new JsonParser();
 JsonElement jsonElement = jsonParser.parse(new StringReader(jsonInput));
 Timestamp parsedTimestamp = (Timestamp) adapter.read(jsonParser.peek(jsonElement.getAsJsonObject()));
 assertEquals(new Timestamp(1615941650123L), parsedTimestamp);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadTimestampFromInvalidIsoFormatJson() throws IOException {
 String jsonInput = "{\"timestamp\":\"2021-03-18T13:30:50.123ZX\"}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 fail("Expected JsonSyntaxException but received none");
 }

 @Test
 public void testWriteSqlDate() throws IOException {
 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 adapter.write(jsonWriter, new java.sql.Date(1613440223000L));
 String jsonOutput = writer.getBuffer().toString();
 assertEquals("\"2021-02-15\"", jsonOutput);
 }

 @Test
 public void testReadSqlDateFromValidJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-03-18\"}";
 JsonParser jsonParser = new JsonParser();
 JsonElement jsonElement = jsonParser.parse(new StringReader(jsonInput));
 java.sql.Date parsedDate = (java.sql.Date) adapter.read(jsonParser.peek(jsonElement.getAsJsonObject()));
 assertEquals(new java.sql.Date(1615941600000L), parsedDate);
 }

 @Test
 public void testReadSqlDateFromInvalidJson() throws IOException {
 String jsonInput = "{\"date\":5}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 fail("Expected JsonParseException but received none");
 }

 @Test
 public void testReadSqlDateFromValidIsoFormatJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-03-18\"}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 jsonParser.nextToken();
 jsonParser.nextToken();
 jsonParser.nextToken();
 java.sql.Date parsedDate = (java.sql.Date) adapter.read(jsonParser.peek(jsonParser.peek().getAsJsonObject()));
 assertEquals(new java.sql.Date(1615941600000L), parsedDate);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadSqlDateFromInvalidIsoFormatJson() throws IOException {
 String jsonInput = "{\"date\":\"2021-03-18T13:30:50.123\"}";
 JsonParser jsonParser = new JsonParser();
 jsonParser.parse(new StringReader(jsonInput));
 fail("Expected JsonSyntaxException but received none");
 }
}