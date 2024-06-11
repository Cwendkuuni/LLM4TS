import static org.junit.Assert.*;
import static com.google.gson.stream.CoreJsonParser.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testBasicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe").endObject();
    writer.beginArray().value("Apples").value("Oranges").endArray();

    JsonElement tree = writer.get();
    JsonElement name = tree.getAsJsonObject().get("name");
    JsonArray fruits = tree.getAsJsonObject().getAsJsonArray("arr");

    assertEquals("John Doe", name.getAsString());
    assertEquals(2, fruits.size());
    assertEquals("Apples", fruits.get(0).getAsString());
    assertEquals("Oranges", fruits.get(1).getAsString());

    Gson gson = new Gson();
    String json = gson.toJson(tree);
    JsonParser parser = new JsonParser();
    JsonElement jsonTree = parser.parse(json);
    assertEquals(tree, jsonTree);
  }

  @Test
  public void testErrorHandling() {
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      writer.beginArray().name("name").value("John Doe").endArray();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject().endArray();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.close();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value("John Doe").name("name");
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value(42).name("name");
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value("John Doe").endArray();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject().value(42).name("name");
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testSerialization() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject()
        .name("name")
        .value("John Doe")
        .name("age")
        .value(42)
        .name("hobbies")
        .beginArray()
        .value("Reading")
        .value("Coding")
        .endArray()
        .endObject();

    JsonElement tree = writer.get();
    Gson gson = new Gson();
    String json = gson.toJson(tree);

    JsonParser parser = new JsonParser();
    JsonElement jsonTree = parser.parse(json);

    JsonObject object = jsonTree.getAsJsonObject();
    assertEquals("name", object.keySet().iterator().next());
    assertEquals("John Doe", object.get("name").getAsString());
    assertEquals(3, object.size());
    assertEquals("age", object.keySet().iterator().next());
    assertEquals(42, object.get("age").getAsInt());
    JsonArray hobbies = object.getAsJsonObject().get("hobbies").getAsJsonArray();
    assertEquals(2, hobbies.size());
    assertEquals("Reading", hobbies.get(0).getAsString());
    assertEquals("Coding", hobbies.get(1).getAsString());
  }

  @Test
  public void testSerializeNulls() {
    JsonTreeWriter writer = new JsonTreeWriter(false);

    writer.beginObject().name("name").value(null).endObject();
    writer.beginArray().value(null).endArray();

    JsonElement tree = writer.get();
    Gson gson = new Gson();
    String json = gson.toJson(tree);

    JsonParser parser = new JsonParser();
    JsonElement jsonTree = parser.parse(json);

    JsonObject object = tree.getAsJsonObject();
    JsonElement name = object.get("name");
    JsonArray array = object.getAsJsonObject().get("arr");

    assertEquals(2, object.size());
    assertEquals(JsonNull.INSTANCE, name);
    assertEquals(2, array.size());
    assertEquals(JsonNull.INSTANCE, array.get(0));
  }

  @Test
  public void testSerializeNullsWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value(null).endObject();
    writer.beginArray().value(null).endArray();

    JsonElement tree = writer.get();
    Gson gson = new Gson();
    String json = gson.toJson(tree);

    JsonParser parser = new JsonParser();
    JsonElement jsonTree = parser.parse(json);

    JsonObject object = tree.getAsJsonObject();
    JsonElement name = object.get("name");
    JsonArray array = object.getAsJsonObject().get("arr");

    assertEquals(2, object.size());
    assertEquals(JsonNull.INSTANCE, name);
    assertEquals(2, array.size());
    assertEquals(JsonNull.INSTANCE, array.get(0));

    JsonObject lenientObject = tree.getAsJsonObject();
    JsonElement lenientName = lenientObject.get("name");
    JsonArray lenientArray = lenientObject.getAsJsonObject().get("arr");

    assertEquals(2, lenientObject.size());
    assertEquals(JsonNull.INSTANCE, lenientName);
    assertEquals(2, lenientArray.size());
    assertEquals(JsonNull.INSTANCE, lenientArray.get(0));

    JsonParser lenientParser = new JsonParser();
    JsonElement lenientJsonTree = lenientParser.parse(json);
    JsonObject lenientJsonObject = lenientJsonTree.getAsJsonObject();
    assertEquals(lenientName, lenientJsonObject.get("name"));
    assertEquals(lenientArray, lenientJsonObject.getAsJsonObject().get("arr"));
  }
}