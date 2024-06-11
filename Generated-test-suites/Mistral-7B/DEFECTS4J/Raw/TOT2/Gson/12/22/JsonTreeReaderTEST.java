import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonTreeReaderTest {

 @Test
 public void testEmptyJsonElement() {
 JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
 Assertions.assertNotNull(reader);
 Assertions.assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testSimpleJsonObject() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject());
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testSimpleJsonArray() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray());
 reader.beginArray();
 reader.nextNull();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonObjectWithNullValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", null));
 reader.beginObject();
 reader.nextName();
 reader.nextNull();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithBooleanValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", true));
 reader.beginObject();
 reader.nextName();
 reader.nextBoolean();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithStringValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", "John Doe"));
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithNumberValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", 5));
 reader.beginObject();
 reader.nextName();
 reader.nextInt();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithArrayProperty() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().add("array", new JsonArray()));
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.beginArray();
 reader.nextNull();
 reader.endArray();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithNestedObjectProperty() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().add("object", new JsonObject().addProperty("name", "John Doe")));
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.endObject();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonObjectWithNestedArrayProperty() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject().add("array", new JsonArray().add(JsonNull.INSTANCE)));
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.beginArray();
 reader.nextNull();
 reader.endArray();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testJsonArrayWithNullValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(JsonNull.INSTANCE));
 reader.beginArray();
 reader.nextNull();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonArrayWithBooleanValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(true));
 reader.beginArray();
 reader.nextBoolean();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonArrayWithStringValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add("John Doe"));
 reader.beginArray();
 reader.nextString();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonArrayWithNumberValue() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(5));
 reader.beginArray();
 reader.nextInt();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonArrayWithNestedObject() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonObject().addProperty("name", "John Doe")));
 reader.beginArray();
 reader.nextNull();
 reader.beginObject();
 reader.nextName();
 reader.nextString();
 reader.endObject();
 reader.endArray();
 reader.close();
 }

 @Test
 public void testJsonArrayWithNestedArray() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonArray().add(JsonNull.INSTANCE)));
 reader.beginArray();
 reader.nextNull();
 reader.beginArray();
 reader.nextNull();
 reader.endArray();
 reader.endArray();
 reader.close();
 }

 @Test (expected = IllegalStateException.class)
 public void testClosedJsonTreeReader() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonObject());
 reader.close();
 reader.nextInt();
 }

 @Test (expected = IllegalStateException.class)
 public void testJsonTreeReaderWithInvalidInput() throws IOException {
 JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("Hello World"));
 reader.nextInt();
 }
}