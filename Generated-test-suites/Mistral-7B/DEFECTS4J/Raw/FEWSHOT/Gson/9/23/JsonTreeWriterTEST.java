package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    writer.endObject();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertEquals(JsonElement.class, element.getClass());
    assertEquals(JsonObject.class, element.getClass().getComponentType());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("foo", jsonObject.name("foo").getAsString());
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testPut() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.put(new JsonPrimitive(1));
    JsonElement element1 = writer.get();
    assertEquals(JsonPrimitive.class, element1.getClass());
    assertEquals(1, element1.getAsInt());

    writer.put(new JsonPrimitive(true));
    JsonElement element2 = writer.get();
    assertEquals(JsonPrimitive.class, element2.getClass());
    assertEquals(true, element2.getAsBoolean());

    writer.put(new JsonPrimitive(3.14));
    JsonElement element3 = writer.get();
    assertEquals(JsonPrimitive.class, element3.getClass());
    assertEquals(3.14, element3.getAsDouble(), 0.001);

    writer.put(new JsonPrimitive('c'));
    JsonElement element4 = writer.get();
    assertEquals(JsonPrimitive.class, element4.getClass());
    assertEquals('c', element4.getAsCharacter());

    writer.put(new JsonObject());
    JsonElement element5 = writer.get();
    assertEquals(JsonObject.class, element5.getClass());

    JsonObject jsonObject1 = new JsonObject();
    jsonObject1.addProperty("foo", "bar");
    writer.put(jsonObject1);
    JsonElement element6 = writer.get();
    assertEquals(JsonObject.class, element6.getClass());
    JsonObject jsonObject2 = (JsonObject) element6;
    assertEquals("bar", jsonObject2.get("foo").getAsString());

    writer.put(new JsonArray());
    JsonElement element7 = writer.get();
    assertEquals(JsonArray.class, element7.getClass());

    JsonArray jsonArray = (JsonArray) element7;
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive("foo"));
    JsonElement element8 = writer.get();
    assertEquals(JsonArray.class, element8.getClass());
    JsonArray jsonArray2 = (JsonArray) element8;
    assertEquals(2, jsonArray2.size());
    assertEquals(1, jsonArray2.get(0).getAsInt());
    assertEquals("foo", jsonArray2.get(1).getAsString());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();

    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.beginArray();
    writer.value(1);
    writer.endArray();
    writer.value(2);
    writer.endArray();

    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(1, jsonArray.size());
    JsonArray jsonArray1 = (JsonArray) jsonArray.get(0);
    assertEquals(1, jsonArray1.size());
    assertEquals(1, jsonArray1.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("foo").value("bar");
    writer.name("baz").value(123);
    writer.endObject();

    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("bar", jsonObject.get("foo").getAsString());
    assertEquals(123, jsonObject.get("baz").getAsInt());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("foo").value("bar");
    writer.name("baz").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.endObject();

    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("bar", jsonObject.get("foo").getAsString());
    JsonArray jsonArray = (JsonArray) jsonObject.get("baz");
    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("foo").name("bar").value("baz");
    writer.endObject();

    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    JsonObject jsonObject1 = (JsonObject) jsonObject.get("foo");
    assertEquals("baz", jsonObject1.get("bar").getAsString());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value("foo");

    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals("foo", element.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(true);

    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals(true, element.getAsBoolean());
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(1);

    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals(1, element.getAsInt());
  }

  @Test
  public void testValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(Long.MAX_VALUE);

    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals(Long.MAX_VALUE, element.getAsLong());
  }

  @Test
  public void testValueNumberDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(Double.NaN);

    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals(Double.NaN, element.getAsDouble(), 0.0);
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.nullValue();

    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.close();

    assertThrows(IOException.class, () -> writer.get());
  }

  @Test
  public void testBeginObjectWithName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject("foo");
    writer.value("bar");
    writer.endObject();

    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("bar", jsonObject.get("bar").getAsString());
  }
}