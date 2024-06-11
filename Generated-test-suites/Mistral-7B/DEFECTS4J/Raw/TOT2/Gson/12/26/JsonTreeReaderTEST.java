import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonTreeReader createReader(JsonElement jsonElement) {
 return new JsonTreeReader(jsonElement);
 }

 @Test
 public void testBeginArray() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }
 });
 reader.beginArray();
 reader.endArray();
 assertTrue(reader.hasNext());
 reader.beginArray();
 assertFalse(reader.hasNext());
 reader.endArray();
 }

 @Test
 public void testBeginObject() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonObject getAsJsonObject() {
 return new JsonObject();
 }
 });
 reader.beginObject();
 reader.endObject();
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertFalse(reader.hasNext());
 reader.endObject();
 }

 @Test
 public void testPeek() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }
 });
 reader.beginArray();
 reader.beginObject();
 reader.endArray();
 reader.endObject();
 reader.beginArray();
 reader.beginObject();
 JsonElement element = reader.peek();
 assertTrue(element.isJsonObject());
 reader.endObject();
 reader.endArray();
 reader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekAfterClose() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }
 });
 reader.close();
 reader.peek();
 }

 @Test
 public void testEmptyArray() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray();
 }
 });
 reader.beginArray();
 reader.endArray();
 assertTrue(reader.hasNext());
 reader.nextNull();
 assertFalse(reader.hasNext());
 }

 @Test
 public void testSingleElementArray() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray().add(new JsonPrimitive("Hello"));
 }
 });
 reader.beginArray();
 String value = reader.nextString();
 assertEquals("Hello", value);
 assertFalse(reader.hasNext());
 reader.endArray();
 }

 @Test
 public void testMultipleElementsArray() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonArray getAsJsonArray() {
 return new JsonArray().add(new JsonPrimitive("Hello")).add(new JsonPrimitive(1));
 }
 });
 reader.beginArray();
 String firstValue = reader.nextString();
 int secondValue = reader.nextInt();
 assertEquals("Hello", firstValue);
 assertEquals(1, secondValue);
 assertFalse(reader.hasNext());
 reader.endArray();
 }

 @Test
 public void testEmptyObject() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonObject getAsJsonObject() {
 return new JsonObject();
 }
 });
 reader.beginObject();
 reader.endObject();
 assertTrue(reader.hasNext());
 reader.nextNull();
 assertFalse(reader.hasNext());
 }

 @Test
 public void testSinglePropertyObject() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonObject getAsJsonObject() {
 return new JsonObject().addProperty("name", new JsonPrimitive("Hello"));
 }
 });
 reader.beginObject();
 String value = reader.nextName();
 assertEquals("name", value);
 assertEquals("Hello", reader.nextString());
 assertFalse(reader.hasNext());
 reader.endObject();
 }

 @Test
 public void testMultiplePropertiesObject() throws IOException {
 JsonTreeReader reader = createReader(new JsonElement() {
 @Override public JsonObject getAsJsonObject() {
 return new JsonObject().addProperty("name", new JsonPrimitive("Hello"))
 .addProperty("age", new JsonPrimitive(1));
 }
 });
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 assertEquals("Hello", reader.nextString());
 int age = reader.nextInt();
 assertEquals(1, age);
 assertFalse(reader.hasNext());
 reader.endObject();
 }
}