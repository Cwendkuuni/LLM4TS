import static org.junit.Assert.*;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

public class JsonTreeWriterTest {

 private JsonTreeWriter writer;

 @Test
 public void testJsonTreeWriterInitialization() {
 writer = new JsonTreeWriter();
 assertNotNull(writer);
 }

 @Test
 public void testGetMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.endObject();
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 }

 @Test
 public void testPeekMethod() throws IOException {
 writer.beginArray();
 writer.value(1);
 writer.value(2);
 writer.value(3);
 JsonElement expectedJsonArray = writer.peek();
 JsonArray jsonArray = expectedJsonArray.getAsJsonArray();
 assertEquals(3, jsonArray.size());
 }

 @Test
 public void testPutMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 JsonElement jsonElement = writer.get();
 writer.put(new JsonPrimitive("age"));
 writer.value(30).put(JsonNull.INSTANCE);
 writer.endObject();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertNotNull(jsonObject.get("age"));
 assertEquals(JsonNull.INSTANCE, jsonObject.get("age").isJsonNull());
 }

 @Test(expected = IllegalStateException.class)
 public void testPutMethodWhenStackIsEmpty() throws IOException {
 writer.put(new JsonPrimitive("name"));
 }

 @Test
 public void testBeginArrayMethod() throws IOException {
 writer.beginObject();
 writer.name("array").beginArray();
 writer.value(1);
 writer.value(2);
 writer.value(3);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonArray jsonArray = jsonObject.get("array").getAsJsonArray();
 assertEquals(3, jsonArray.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayMethodWhenStackIsEmpty() throws IOException {
 writer.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayMethodWhenPendingNameIsSet() throws IOException {
 writer.beginObject();
 writer.name("name");
 writer.endArray();
 }

 @Test
 public void testEndArrayMethod() throws IOException {
 writer.beginObject();
 writer.name("array");
 writer.beginArray();
 writer.value(1);
 writer.value(2);
 writer.value(3);
 writer.endArray();
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonArray jsonArray = jsonObject.get("array").getAsJsonArray();
 assertEquals(3, jsonArray.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectMethodWhenStackIsEmpty() throws IOException {
 writer.beginObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectMethodWhenPendingNameIsSet() throws IOException {
 writer.beginObject();
 writer.name("name");
 writer.beginObject();
 }

 @Test
 public void testBeginObjectMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectMethodWhenStackIsEmpty() throws IOException {
 writer.endObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectMethodWhenPendingNameIsSet() throws IOException {
 writer.beginObject();
 writer.name("name");
 writer.endObject();
 writer.name("age");
 }

 @Test
 public void testEndObjectMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.name("age").value(30);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 assertEquals(30, jsonObject.get("age").getAsInt());
 }

 @Test(expected = IllegalStateException.class)
 public void testNameMethodWhenStackIsEmpty() throws IOException {
 writer.name("name");
 }

 @Test(expected = IllegalStateException.class)
 public void testNameMethodWhenPendingNameIsSet() throws IOException {
 writer.beginObject();
 writer.name("name");
 writer.name("age");
 }

 @Test
 public void testNameMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.name("age").value(30);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 assertEquals(30, jsonObject.get("age").getAsInt());
 }

 @Test(expected = IllegalStateException.class)
 public void testValueMethodWhenStackIsEmpty() throws IOException {
 writer.value(1);
 }

 @Test(expected = IllegalStateException.class)
 public void testValueMethodWhenPendingNameIsSet() throws IOException {
 writer.beginObject();
 writer.name("name");
 writer.value(1);
 }

 @Test
 public void testValueStringMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 }

 @Test
 public void testValueBooleanMethod() throws IOException {
 writer.beginObject();
 writer.name("isEmployed").value(true);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("isEmployed", jsonObject.keySet().iterator().next());
 assertEquals(true, jsonObject.get("isEmployed").getAsBoolean());
 }

 @Test
 public void testValueDoubleMethod() throws IOException {
 writer.beginObject();
 writer.name("price").value(12.34);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("price", jsonObject.keySet().iterator().next());
 assertEquals(12.34, jsonObject.get("price").getAsDouble(), 0.001);
 }

 @Test
 public void testValueLongMethod() throws IOException {
 writer.beginObject();
 writer.name("id").value(1234567890L);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("id", jsonObject.keySet().iterator().next());
 assertEquals(1234567890L, jsonObject.get("id").getAsLong());
 }

 @Test
 public void testValueNumberMethod() throws IOException {
 writer.beginObject();
 writer.name("population").value(1234567890);
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("population", jsonObject.keySet().iterator().next());
 assertEquals(1234567890L, jsonObject.get("population").getAsNumber().longValue());
 }

 @Test
 public void testFlushMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.name("age").value(30);
 writer.flush();
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 assertEquals(30, jsonObject.get("age").getAsInt());
 }

 @Test
 public void testCloseMethod() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.name("age").value(30);
 writer.close();
 JsonElement jsonElement = writer.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertEquals("name", jsonObject.keySet().iterator().next());
 assertEquals("John Doe", jsonObject.get("name").getAsString());
 assertEquals(30, jsonObject.get("age").getAsInt());
 assertTrue(jsonObject.get("name").isJsonPrimitive());
 assertTrue(jsonObject.get("age").isJsonPrimitive());
 assertFalse(jsonObject.isJsonNull());
 assertFalse(jsonObject.isJsonArray());
 }

 @Test(expected = AssertionError.class)
 public void testCloseMethodTwice() throws IOException {
 writer.beginObject();
 writer.name("name").value("John Doe");
 writer.name("age").value(30);
 writer.close();
 writer.close();
 }

}