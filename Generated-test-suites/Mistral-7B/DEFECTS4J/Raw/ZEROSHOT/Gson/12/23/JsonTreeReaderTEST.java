import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReaderTest {

 private JsonElement createJsonElement(String json) {
 return new JsonParser().parse(json).getAsJson();
 }

 @Test
 public void testEmptyJson() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testBasicJson() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));

 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 assertEquals(30, reader.nextInt());
 assertEquals("city", reader.nextName());
 assertEquals("New York", reader.nextString());
 reader.nextNull(); // empty value for last key
 reader.close();
 }

 @Test
 public void testEmptyArray() throws IOException {
 String json = "[ ]";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
 reader.beginArray();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testArrayWithValues() throws IOException {
 String json = "[1, \"two\", 3.0, true, false, null]";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
 reader.beginArray();
 assertEquals(1, reader.nextInt());
 assertEquals("two", reader.nextString());
 assertEquals(3.0, reader.nextDouble(), 0.0);
 assertEquals(true, reader.nextBoolean());
 assertEquals(false, reader.nextBoolean());
 assertNull(reader.nextNull());
 reader.endArray();
 reader.close();
 }

 @Test
 public void testNestedJson() throws IOException {
 String json = "{\"name\":{\"first\":\"John\",\"last\":\"Doe\"},\"age\":30,\"city\":\"New York\"}";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));

 assertEquals("name", reader.nextName());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.endObject();
 assertEquals("last", reader.nextName());
 assertEquals("Doe", reader.nextString());
 assertEquals("age", reader.nextName());
 assertEquals(30, reader.nextInt());
 assertEquals("city", reader.nextName());
 assertEquals("New York", reader.nextString());
 reader.nextNull(); // empty value for last key
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNestedArrays() throws IOException {
 String json = "{\"name\":[\"John\",\"Jane\"],\"ages\":[30,31],\"booleans\":[true,false],\"nulls\":[null,null]}";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
 reader.beginObject();
 reader.beginArray();
 assertEquals("John", reader.nextString());
 reader.endArray();
 reader.beginArray();
 assertEquals(30, reader.nextInt());
 reader.endArray();
 reader.beginArray();
 assertEquals(true, reader.nextBoolean());
 reader.endArray();
 reader.beginArray();
 assertEquals(false, reader.nextBoolean());
 reader.endArray();
 reader.beginArray();
 reader.endArray(); // empty array
 reader.beginArray();
 reader.endArray(); // empty array
 reader.endObject();
 reader.close();
 }

 @Test
 public void testSkipValue() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"empty\":null}";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
 reader.beginObject();
 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.skipValue();
 reader.nextNull(); // empty value for "city"
 reader.nextNull(); // empty value for "empty"
 reader.endObject();
 reader.close();
 }

 @Test
 public void testPromoteNameToValue() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
 reader.beginObject();
 reader.promoteNameToValue(); // promote "name" to value "John"
 assertEquals("John", reader.nextString());
 reader.nextInt(); // read "age" as int
 reader.nextString(); // read "city" as string
 reader.nextNull(); // empty value for last key
 reader.endObject();
 reader.close();
 }
}