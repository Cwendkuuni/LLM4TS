import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonTreeReader reader;

 @Test
 public void testEmptyJsonElement() {
 JsonElement jsonElement = JsonNull.INSTANCE;
 reader = new JsonTreeReader(jsonElement);

 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testEmptyJsonArray() {
 reader = new JsonTreeReader(new JsonArray());

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testEmptyJsonObject() {
 reader = new JsonTreeReader(new JsonObject());

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithOneElement() {
 JsonElement jsonElement = new JsonPrimitive("hello");
 reader = new JsonTreeReader(new JsonArray().add(jsonElement));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertEquals("hello", reader.nextString());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithMultipleElements() {
 reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive(1)).add(new JsonPrimitive(2)));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertEquals(1, reader.nextInt());
 assertTrue(reader.hasNext());
 assertEquals(2, reader.nextInt());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithNullElement() {
 reader = new JsonTreeReader(new JsonArray().add(JsonNull.INSTANCE));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertNull(reader.next());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithBooleanElement() {
 reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive(true)).add(new JsonPrimitive(false)));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertTrue(reader.nextBoolean());
 assertTrue(reader.hasNext());
 assertFalse(reader.nextBoolean());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithNumberElement() {
 reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive(1.5)).add(new JsonPrimitive(3)));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertEquals(1.5, reader.nextDouble(), 0.001);
 assertTrue(reader.hasNext());
 assertEquals(3, reader.nextInt());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonArrayWithStringElement() {
 reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("hello")).add(new JsonPrimitive("world")));

 assertTrue(reader.hasNext());
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertEquals("hello", reader.nextString());
 assertTrue(reader.hasNext());
 assertEquals("world", reader.nextString());
 reader.endArray();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithOneKeyValuePair() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key", new JsonPrimitive("value")));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals("value", reader.nextString());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithMultipleKeyValuePair() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key1", new JsonPrimitive(1)).addProperty("key2", new JsonPrimitive("hello world")));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key1", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals(1, reader.nextInt());
 assertTrue(reader.hasNext());
 assertEquals("key2", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals("hello world", reader.nextString());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithNullValue() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key", JsonNull.INSTANCE));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key", reader.nextName());
 assertTrue(reader.hasNext());
 assertNull(reader.next());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithBooleanValue() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key", new JsonPrimitive(true)).addProperty("key2", new JsonPrimitive(false)));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key", reader.nextName());
 assertTrue(reader.hasNext());
 assertTrue(reader.nextBoolean());
 assertTrue(reader.hasNext());
 assertEquals("key2", reader.nextName());
 assertTrue(reader.hasNext());
 assertFalse(reader.nextBoolean());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithNumberValue() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key1", new JsonPrimitive(1.5)).addProperty("key2", new JsonPrimitive(3)));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key1", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals(1.5, reader.nextDouble(), 0.001);
 assertTrue(reader.hasNext());
 assertEquals("key2", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals(3, reader.nextInt());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testJsonObjectWithStringValue() {
 reader = new JsonTreeReader(new JsonObject().addProperty("key1", new JsonPrimitive("hello")).addProperty("key2", new JsonPrimitive("world")));

 assertTrue(reader.hasNext());
 reader.beginObject();
 assertTrue(reader.hasNext());
 assertEquals("key1", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals("hello", reader.nextString());
 assertTrue(reader.hasNext());
 assertEquals("key2", reader.nextName());
 assertTrue(reader.hasNext());
 assertEquals("world", reader.nextString());
 reader.endObject();
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeReaderClosed() {
 reader = new JsonTreeReader(new JsonObject());
 reader.close();
 reader.nextInt();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeReaderHasNextAfterClosed() {
 reader = new JsonTreeReader(new JsonObject());
 reader.close();
 reader.hasNext();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeReaderPeekAfterClosed() {
 reader = new JsonTreeReader(new JsonObject());
 reader.close();
 reader.peek();
 }

}