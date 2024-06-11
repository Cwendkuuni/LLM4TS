import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;

public class JsonTreeReaderTest {

 @Test
 public void testBeginArray() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonArray());
 reader.beginArray();
 Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
 reader.endArray();
 Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
 reader.close();
 }

 @Test
 public void testBeginObject() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonObject());
 reader.beginObject();
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
 reader.endObject();
 Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
 reader.close();
 }

 @Test
 public void testHasNext() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonObject());
 reader.beginObject();
 reader.nextName(); // "name1"
 reader.nextString(); // "value1"
 Assert.assertTrue(reader.hasNext());
 reader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testNoNext() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonObject());
 reader.beginObject();
 reader.nextName(); // "name1"
 reader.nextString(); // "value1"
 reader.nextName(); // "name2"
 reader.nextString(); // "value2"
 reader.nextName(); // "name3"
 reader.nextString(); // "value3"
 reader.nextName(); // should throw an exception
 reader.close();
 }

 @Test
 public void testPeek() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonObject());
 reader.beginObject();
 reader.nextName(); // "name1"
 reader.nextString(); // "value1"
 reader.beginArray();
 reader.nextName(); // "name2"
 reader.nextString(); // "value2"
 reader.endArray();
 reader.endObject();
 reader.nextName(); // "name3"
 reader.nextString(); // "value3"
 reader.nextName(); // "name4"
 reader.nextString(); // "value4"
 reader.nextNull();
 reader.close();

 Assert.assertEquals(JsonToken.NAME, reader.peek(0)); // name1
 Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek(1));
 Assert.assertEquals(JsonToken.NAME, reader.peek(2)); // name2
 Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek(3));
 Assert.assertEquals(JsonToken.NAME, reader.peek(4)); // name3
 Assert.assertEquals(JsonToken.NAME, reader.peek(5)); // name4
 Assert.assertEquals(JsonToken.NULL, reader.peek(6));
 }

 @Test(expected = AssertionError.class)
 public void testCloseReader() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonArray());
 reader.close();
 reader.nextToken(); // should throw an exception
 }

 @Test(expected = AssertionError.class)
 public void testCloseWhileOpen() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(createJsonArray());
 reader.close();
 reader.nextToken(); // should throw an exception
 reader.beginArray();
 reader.nextName();
 reader.nextString();
 reader.endArray();
 reader.nextName();
 reader.nextString();
 reader.nextName();
 reader.nextString();
 reader.nextName();
 reader.nextString();
 reader.nextNull();
 reader.close();
 reader.nextToken(); // should throw an exception
 }

 private JsonElement createJsonArray() {
 return new JsonArray();
 }

 private JsonElement createJsonObject() {
 return new JsonObject();
 }

 private JsonElement createJsonObjectWithNameAndValue() {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "value");
 return jsonObject;
 }

 private JsonElement createJsonObjectWithNameAndArray() {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "name2");
 jsonObject.add("name2", new JsonArray());
 return jsonObject;
 }

 private JsonElement createJsonObjectWithNameAndNestedObject() {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "name2");
 jsonObject.add("name2", new JsonObject());
 return jsonObject;
 }

 private JsonElement createJsonObjectWithNameAndNestedArray() {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "name2");
 jsonObject.add("name2", new JsonArray().add("value1").add("value2"));
 return jsonObject;
 }

}