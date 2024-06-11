import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

public class JsonTreeReaderTest {

 @Test
 public void testEmptyJson() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);

 Assert.assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testSimpleArray() throws IOException {
 JsonElement jsonElement = new JsonPrimitive("[1, 2, 3]");
 JsonTreeReader reader = new JsonTreeReader(jsonElement);

 Assert.assertTrue(reader.hasNext());
 reader.beginArray();
 Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
 reader.nextInt();
 Assert.assertEquals(1, reader.nextInt());
 reader.nextInt();
 Assert.assertEquals(2, reader.nextInt());
 reader.nextInt();
 Assert.assertEquals(3, reader.nextInt());
 reader.endArray();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testSimpleObject() throws IOException {
 JsonElement jsonElement = new JsonPrimitive("{\"name\":\"John\", \"age\":30}");
 JsonTreeReader reader = new JsonTreeReader(jsonElement);

 Assert.assertTrue(reader.hasNext());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("name", reader.nextString());
 reader.nextName();
 Assert.assertEquals("age", reader.nextString());
 reader.nextInt();
 Assert.assertEquals(30, reader.nextInt());
 reader.endObject();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNestedObject() throws IOException {
 JsonElement jsonElement = new JsonPrimitive("{\"name\": {\"first\":\"John\", \"last\":\"Doe\"}}");
 JsonTreeReader reader = new JsonTreeReader(jsonElement);

 Assert.assertTrue(reader.hasNext());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("name", reader.nextString());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("first", reader.nextString());
 Assert.assertEquals("John", reader.nextString());
 reader.nextName();
 Assert.assertEquals("last", reader.nextString());
 Assert.assertEquals("Doe", reader.nextString());
 reader.endObject();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNestedArray() throws IOException {
 JsonElement jsonElement = new JsonPrimitive("{\"names\":[{\"first\":\"John\", \"last\":\"Doe\"}, {\"first\":\"Jane\", \"last\":\"Doe\"}]}");
 JsonTreeReader reader = new JsonTreeReader(jsonElement);

 Assert.assertTrue(reader.hasNext());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("names", reader.nextString());
 reader.beginArray();
 Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("first", reader.nextString());
 Assert.assertEquals("John", reader.nextString());
 reader.nextName();
 Assert.assertEquals("last", reader.nextString());
 Assert.assertEquals("Doe", reader.nextString());
 reader.endObject();
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.nextName();
 Assert.assertEquals("first", reader.nextString());
 Assert.assertEquals("Jane", reader.nextString());
 reader.nextName();
 Assert.assertEquals("last", reader.nextString());
 Assert.assertEquals("Doe", reader.nextString());
 reader.endObject();
 reader.endArray();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testNullValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(null));

 Assert.assertFalse(reader.hasNext());
 reader.close();
 }

 @Test (expected = IllegalStateException.class)
 public void testClosedReader() throws IOException {JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(1));
 reader.close();
 reader.nextInt();
 }
}