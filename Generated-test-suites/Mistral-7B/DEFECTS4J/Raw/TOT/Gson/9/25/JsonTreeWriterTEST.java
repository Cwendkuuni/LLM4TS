import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testBeginAndEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(0, ((JsonArray) result).size());
  }

  @Test
  public void testBeginAndEndArrayWithValues() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2.5);
    writer.value(true);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(3, jsonArray.size());
    assertEquals(JsonPrimitive.class, jsonArray.get(0).getClass());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(JsonPrimitive.class, jsonArray.get(1).getClass());
    assertEquals(2.5, jsonArray.get(1).getAsDouble(), 0.001);
    assertEquals(JsonPrimitive.class, jsonArray.get(2).getClass());
    assertEquals(true, jsonArray.get(2).getAsBoolean());
  }

  @Test
  public void testBeginAndEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(1, jsonObject.size());
    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test
  public void testBeginAndEndObjectWithNestedArraysAndObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("obj1").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.name("obj2").beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").beginArray();
    writer.value(3);
    writer.value(new JsonPrimitive("foo"));
    writer.endArray();
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonElement obj1 = jsonObject.get("obj1");
    JsonArray array1 = (JsonArray) obj1;
    assertEquals(2, array1.size());
    JsonElement obj2 = jsonObject.get("obj2");
    JsonObject nestedObject = (JsonObject) obj2;
    assertEquals(2, nestedObject.size());
    assertEquals("key1", nestedObject.keySet().iterator().next());
    assertEquals("value1", nestedObject.get("key1").getAsString());
    assertEquals("key2", nestedObject.keySet().iterator().next());
    JsonArray array2 = (JsonArray) nestedObject.get("key2");
    assertEquals(2, array2.size());
    assertEquals(1, array2.get(0).getAsInt());
    assertEquals(3, array2.get(1).getAsInt());
    assertEquals("foo", array2.get(1).getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.value(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWriterTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.close();
  }
}