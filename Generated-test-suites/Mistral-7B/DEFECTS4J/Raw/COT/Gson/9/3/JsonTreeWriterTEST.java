import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value").endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.add("key", new JsonPrimitive("value"));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.get();
  }

  @Test
  public void testPeek() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value").endObject();

    JsonElement result = writer.peek();

    JsonObject expected = new JsonObject();
    expected.add("key", new JsonPrimitive("value"));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.peek();
  }

  @Test
  public void testPut() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.put(new JsonPrimitive("value"));

    JsonElement result = writer.peek();

    JsonPrimitive expected = new JsonPrimitive("value");

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.put(new JsonPrimitive("value"));
    writer.get();
    writer.put(new JsonPrimitive("value2"));
    writer.put(new JsonPrimitive("value3"));
    writer.put(new JsonPrimitive("value4"));
    writer.put(new JsonPrimitive("value5"));
    writer.put(new JsonPrimitive("value6"));
    writer.put(new JsonPrimitive("value7"));
    writer.put(new JsonPrimitive("value8"));
    writer.put(new JsonPrimitive("value9"));
    writer.put(new JsonPrimitive("value10"));

    writer.put(new JsonPrimitive("value11")); // should throw an exception
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    expected.add(new JsonPrimitive("element2"));

    JsonArray result = (JsonArray) writer.get();

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithObjectOnStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value").endObject();
    writer.beginArray();

    writer.get(); // should throw an exception
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();

    JsonElement result = writer.peek();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    expected.add(new JsonPrimitive("element2"));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithNonArrayOnStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value").endObject();
    writer.beginArray();

    writer.endArray(); // should throw an exception
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").name("key2").value("value2").endObject();

    JsonObject expected = new JsonObject();
    expected.add("key1", new JsonPrimitive("value1"));
    expected.add("key2", new JsonPrimitive("value2"));

    JsonObject result = (JsonObject) writer.get();

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithArrayOnStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();
    writer.beginObject();

    writer.get(); // should throw an exception
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").name("key2").value("value2").endObject();

    JsonElement result = writer.peek();

    JsonObject expected = new JsonObject();
    expected.add("key1", new JsonPrimitive("value1"));
    expected.add("key2", new JsonPrimitive("value2"));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithNonObjectOnStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").endObject();
    writer.beginArray();

    writer.endObject(); // should throw an exception
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.endObject();

    JsonObject expected = new JsonObject();
    expected.add("key1", new JsonPrimitive("value1"));
    expected.add("key2", new JsonPrimitive("value2"));

    JsonObject result = (JsonObject) writer.get();

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithNonObjectOnStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("key");

    writer.get(); // should throw an exception
  }
}