import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

 @Test
 public void testEmptyJson() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
 assertEquals(JsonToken.END_DOCUMENT, reader.peek());
 reader.close();
 }

 @Test
 public void testBasicJson() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\":\"John\"," +
 "\"age\":30," +
 "\"cars\":[" +
 "{\"color\":\"red\",\"model\":\"Fiesta\"}," +
 "{\"color\":\"blue\",\"model\":\"Focus\"}" +
 "]" +
 "}"));

 assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.beginObject();

 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.nextName(); // skip "age"
 reader.nextInt();
 assertEquals(30, reader.nextInt());

 reader.nextName(); // skip "cars"
 reader.beginArray();
 reader.beginArray();
 assertEquals("color", reader.nextName());
 assertEquals("red", reader.nextString());
 reader.nextName(); // skip "model"
 assertEquals("Fiesta", reader.nextString());
 reader.endArray();

 reader.beginArray();
 reader.beginArray();
 assertEquals("color", reader.nextName());
 assertEquals("blue", reader.nextString());
 reader.nextName(); // skip "model"
 assertEquals("Focus", reader.nextString());
 reader.endArray();
 reader.endArray();

 reader.nextNull();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNestedJson() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\":{" +
 "\"first\":\"John\"," +
 "\"last\":\"Doe\"" +
 "}," +
 "\"age\":30," +
 "\"address\":{" +
 "\"street\":\"123 Main St\"," +
 "\"city\":\"Anytown\"," +
 "\"state\":\"CA\"" +
 "}," +
 "\"phoneNumbers\":[" +
 "{\"type\":\"home\",\"number\":\"555-1234\"}," +
 "{\"type\":\"work\",\"number\":\"555-5678\"}" +
 "]" +
 "}"));

 assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.beginObject();

 assertEquals("name", reader.nextName());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.nextName(); // skip "last"
 reader.endObject();

 reader.nextName(); // skip "age"

 reader.nextName(); // skip "address"
 reader.beginObject();
 reader.nextName(); // skip "street"
 assertEquals("123 Main St", reader.nextString());
 reader.nextName(); // skip "city"
 assertEquals("Anytown", reader.nextString());
 reader.nextName(); // skip "state"
 reader.endObject();

 reader.nextName(); // skip "phoneNumbers"
 reader.beginArray();
 reader.beginArray();
 assertEquals("type", reader.nextName());
 assertEquals("home", reader.nextString());
 reader.nextName(); // skip "number"
 assertEquals("555-1234", reader.nextString());
 reader.endArray();

 reader.beginArray();
 reader.beginArray();
 assertEquals("type", reader.nextName());
 assertEquals("work", reader.nextString());
 reader.nextName(); // skip "number"
 assertEquals("555-5678", reader.nextString());
 reader.endArray();
 reader.endArray();
 reader.nextNull();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonWithNullValues() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\":null," +
 "\"age\":null," +
 "\"address\":{" +
 "\"street\":null," +
 "\"city\":null," +
 "\"state\":null" +
 "}" +
 "}"));

 assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.beginObject();

 reader.nextName(); // skip "name"
 assertNull(reader.nextNull());
 reader.nextName(); // skip "age"
 assertNull(reader.nextNull());

 reader.nextName(); // skip "address"
 reader.beginObject();
 reader.nextName(); // skip "street"
 assertNull(reader.nextNull());
 reader.nextName(); // skip "city"
 assertNull(reader.nextNull());
 reader.nextName(); // skip "state"
 reader.endObject();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonWithEmptyArrays() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\":\"John\"," +
 "\"cars\":[]," +
 "\"age\":30," +
 "\"phoneNumbers\":[]" +
 "}"));

 assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.beginObject();

 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.nextName(); // skip "cars"
 reader.beginArray();
 assertEquals(JsonToken.END_ARRAY, reader.peek());
 reader.endArray();
 reader.nextName(); // skip "age"
 reader.nextInt();
 assertEquals(30, reader.nextInt());

 reader.nextName(); // skip "phoneNumbers"
 reader.beginArray();
 assertEquals(JsonToken.END_ARRAY, reader.peek());
 reader.endArray();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonWithEmptyObjects() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\":{" +
 "}" +
 ",\"age\":30," +
 "\"address\":{" +
 "}" +
 ",\"phoneNumbers\":[" +
 "{" +
 "}" +
 "]" +
 "}"));

 assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.beginObject();

 reader.nextName(); // skip "name"
 reader.endObject();
 reader.nextName(); // skip "age"
 reader.nextInt();
 assertEquals(30, reader.nextInt());

 reader.nextName(); // skip "address"
 reader.endObject();
 reader.nextName(); // skip "phoneNumbers"
 reader.beginArray();
 reader.beginObject();
 reader.endObject();
 reader.endArray();
 reader.nextNull();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonWithMissingKey() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"age\":30," +
 "\"name\":\"John\"" +
 "}"));

 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test(expected = AssertionError.class)
 public void testJsonReaderClosed() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
 reader.close();
 reader.nextString();
 }
}