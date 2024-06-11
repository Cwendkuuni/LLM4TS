import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyJsonTree() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree = writer.get();
    assertEquals(JsonNull.INSTANCE, jsonTree);
  }

  @Test
  public void testJsonArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("apple");
    writer.value("banana");
    writer.value("orange");
    writer.endArray();
    JsonElement jsonTree = writer.get();
    JsonArray jsonArray = (JsonArray) jsonTree;
    assertEquals(3, jsonArray.size());
    assertEquals("apple", jsonArray.get(0).getAsString());
    assertEquals("banana", jsonArray.get(1).getAsString());
    assertEquals("orange", jsonArray.get(2).getAsString());
  }

  @Test
  public void testJsonObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("Alice");
    writer.name("age").value(30);
    writer.name("city").value("New York");
    writer.endObject();
    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;
    assertEquals("Alice", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertEquals("New York", jsonObject.get("city").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.endObject();
  }

  @Test
  public void testNestingJsonObjectAndJsonArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("first").beginArray();
    writer.value("apple");
    writer.value("banana");
    writer.endArray();
    writer.name("second").value(3);
    writer.name("third").beginObject();
    writer.name("fourth").value("dog");
    writer.name("fifth").beginArray();
    writer.value("cat");
    writer.value("monkey");
    writer.endArray();
    writer.endObject();
    writer.endObject();
    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;
    JsonArray firstArray = (JsonArray) jsonObject.get("first");
    assertEquals(2, firstArray.size());
    assertEquals("apple", firstArray.get(0).getAsString());
    assertEquals("banana", firstArray.get(1).getAsString());
    assertEquals(3, jsonObject.get("second").getAsInt());
    JsonObject thirdObject = (JsonObject) jsonObject.get("third");
    assertEquals("dog", thirdObject.get("fourth").getAsString());
    JsonArray fifthArray = (JsonArray) thirdObject.get("fifth");
    assertEquals(2, fifthArray.size());
    assertEquals("cat", fifthArray.get(0).getAsString());
    assertEquals("monkey", fifthArray.get(1).getAsString());
  }
}