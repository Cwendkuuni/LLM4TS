import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_writesEmptyJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement json = writer.get();

    assertEquals(JsonObject.class, json.getClass());
    assertNull(json.getAsJsonArray());
    assertNull(json.getAsJsonPrimitive());
  }

  @Test
  public void testJsonTreeWriter_writesJsonObjectWithOneStringKeyAndValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement json = writer.get();

    JsonObject jsonObject = json.getAsJsonObject();
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test
  public void testJsonTreeWriter_writesJsonObjectWithNestedJsonArrayAndJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();

    writer.name("object");
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value("value2");
    writer.endObject();

    writer.endObject();

    JsonElement json = writer.get();

    JsonObject jsonObject = json.getAsJsonObject();
    JsonArray array = jsonObject.getAsJsonArray("array");
    JsonObject object = jsonObject.getAsJsonObject("object");

    assertEquals(2, array.size());
    assertEquals("element1", array.get(0).getAsString());
    assertEquals("element2", array.get(1).getAsString());

    assertEquals(2, object.size());
    assertEquals("value1", object.get("key1").getAsString());
    assertEquals("value2", object.get("key2").getAsString());
  }

  @Test
  public void testJsonTreeWriter_writesEmptyJsonArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement json = writer.get();

    assertEquals(JsonArray.class, json.getClass());
    assertNull(json.getAsJsonArray().size());
  }

  @Test
  public void testJsonTreeWriter_writesJsonArrayWithOneStringElement() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element");
    writer.endArray();
    JsonElement json = writer.get();

    JsonArray jsonArray = json.getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertEquals("element", jsonArray.get(0).getAsString());
  }

  @Test
  public void testJsonTreeWriter_writesJsonArrayWithNestedJsonObjectsAndJsonPrimitives() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value(123);
    writer.endObject();

    writer.beginObject();
    writer.name("key3");
    writer.value("value3");
    writer.endObject();

    writer.endArray();

    JsonElement json = writer.get();

    JsonArray jsonArray = json.getAsJsonArray();
    assertEquals(2, jsonArray.size());

    JsonElement firstElement = jsonArray.get(0);
    JsonObject firstJsonObject = firstElement.getAsJsonObject();
    assertEquals(2, firstJsonObject.size());
    assertEquals("value1", firstJsonObject.get("key1").getAsString());
    assertEquals(123, firstJsonObject.get("key2").getAsInt());

    JsonElement secondElement = jsonArray.get(1);
    JsonObject secondJsonObject = secondElement.getAsJsonObject();
    assertEquals(1, secondJsonObject.size());
    assertEquals("value3", secondJsonObject.get("key3").getAsString());
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenCalledTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.endObject()");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_writesJsonNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
    JsonElement json = writer.get();
    assertEquals(JsonNull.INSTANCE, json);
  }

  @Test
  public void testJsonTreeWriter_writesJsonBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement json = writer.get();
    assertEquals(true, json.getAsJsonPrimitive().isBoolean());
    assertEquals(true, json.getAsJsonPrimitive().getAsBoolean());
  }

  @Test
  public void testJsonTreeWriter_writesJsonNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonElement json = writer.get();
    JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
    assertEquals(42, jsonPrimitive.getAsInt());
    assertEquals(42d, jsonPrimitive.getAsNumber(), 0.0);
  }

  @Test
  public void testJsonTreeWriter_writesJsonPrimitive() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Test");
    JsonElement json = writer.get();
    assertEquals("Test", json.getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testJsonTreeWriter_writesJsonPrimitiveWithQuotes() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("\"Test\"");
    JsonElement json = writer.get();
    assertEquals("\"Test\"", json.getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenTryingToWriteJsonObjectAfterJsonArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.beginObject();
    try {
      fail("Expected IllegalStateException when calling JsonTreeWriter.name() after calling JsonTreeWriter.beginArray()");
    } catch (IllegalStateException expected) {
    }
    writer.endObject();
    writer.endArray();

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.beginObject() and JsonTreeWriter.endArray() with an incorrect order");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenTryingToWriteJsonArrayAfterJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.beginArray();
    try {
      fail("Expected IllegalStateException when calling JsonTreeWriter.beginArray() after calling JsonTreeWriter.name() and JsonTreeWriter.value()");
    } catch (IllegalStateException expected) {
    }
    writer.endArray();
    writer.endObject();

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.beginObject() and JsonTreeWriter.name() and JsonTreeWriter.value() with an incorrect order");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenTryingToWriteJsonObjectAfterJsonArrayWithElements() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value("value1");
    writer.beginObject();
    writer.name("key");
    writer.value("value2");
    try {
      writer.endObject();
      fail("Expected IllegalStateException when calling JsonTreeWriter.endObject() after calling JsonTreeWriter.beginArray() and JsonTreeWriter.value() and JsonTreeWriter.beginObject()");
    } catch (IllegalStateException expected) {
    }
    writer.endArray();

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.beginArray() and JsonTreeWriter.value() and JsonTreeWriter.beginObject() with an incorrect order");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenTryingToWriteJsonObjectAfterJsonArrayWithoutElements() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.endArray();

    try {
      writer.beginObject();
      fail("Expected IllegalStateException when calling JsonTreeWriter.beginObject() after calling JsonTreeWriter.endArray()");
    } catch (IllegalStateException expected) {
    }

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.beginObject() without calling JsonTreeWriter.beginArray() or JsonTreeWriter.value()");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_throwsExceptionWhenTryingToWriteJsonObjectAfterJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1");
    writer.value("value1");

    try {
      writer.beginObject();
      fail("Expected IllegalStateException when calling JsonTreeWriter.beginObject() after calling JsonTreeWriter.name() and JsonTreeWriter.value()");
    } catch (IllegalStateException expected) {
    }

    writer.endObject();

    try {
      writer.get();
      fail("Expected IllegalStateException when calling JsonTreeWriter.get() after calling JsonTreeWriter.beginObject() and JsonTreeWriter.name() and JsonTreeWriter.value() with an incorrect order");
    } catch (IllegalStateException expected) {
    }
  }

  @Test
  public void testJsonTreeWriter_writesJsonPrimitiveWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter(true);
    writer.value(Double.NaN);
    JsonElement json = writer.get();
    JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
    assertEquals(Double.TYPE, jsonPrimitive.getClass().getComponentType());
    assertFalse(jsonPrimitive.isNumber());
    assertFalse(jsonPrimitive.isBoolean());
    assertFalse(jsonPrimitive.isString());
    assertFalse(jsonPrimitive.isNull());
    assertEquals(Double.NaN, jsonPrimitive.getAsDouble(), 0.0);
  }
}