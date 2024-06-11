package com.google.gson.internal.bind;

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

public class JsonTreeReaderTest {

  @Test
  public void testEmptyReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSingleElementReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonPrimitive.createString("foo"));
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("foo", reader.nextString());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEmptyArrayReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonArray.class.getDeclaredConstructor().newInstance());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    assertNull(reader.nextName());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSingleElementArrayReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonArray.class.getDeclaredConstructor().newInstance());
    reader.beginArray();
    JsonElement element = JsonPrimitive.createString("foo");
    reader.nextName();
    reader.next(element);
    reader.endArray();
    assertEquals("foo", reader.nextString());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEmptyObjectReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonObject.class.getDeclaredConstructor().newInstance());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSinglePropertyObjectReader() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("foo", JsonPrimitive.createString("bar"));
    JsonTreeReader reader = new JsonTreeReader(object);
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testArrayWithMultipleElementsReader() throws IOException {
    JsonArray array = new JsonArray();
    array.add(JsonPrimitive.createString("foo"));
    array.add(JsonPrimitive.createNumber(123));
    JsonTreeReader reader = new JsonTreeReader(array.iterator());
    reader.beginArray();
    assertEquals("foo", reader.nextString());
    assertEquals(123, reader.nextInt());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testObjectWithMultiplePropertiesReader() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("foo", JsonPrimitive.createString("bar"));
    object.addProperty("baz", JsonPrimitive.createNumber(456));
    JsonTreeReader reader = new JsonTreeReader(object.entrySet().iterator());
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    assertEquals("baz", reader.nextName());
    assertEquals(456, reader.nextInt());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNestedArrayAndObjectReader() throws IOException {
    JsonObject object = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(JsonPrimitive.createString("foo"));
    array.add(new JsonObject());
    object.add("array", array);
    JsonTreeReader reader = new JsonTreeReader(object.entrySet().iterator());
    reader.beginObject();
    assertEquals("array", reader.nextName());
    reader.beginArray();
    assertEquals("foo", reader.nextString());
    reader.beginObject();
    assertEquals("null", reader.nextName());
    reader.nextNull();
    reader.endObject();
    reader.endArray();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("foo", JsonPrimitive.createString("bar"));
    JsonTreeReader reader = new JsonTreeReader(object);
    reader.promoteNameToValue();
    reader.nextString();
    reader.nextString();
    assertEquals("bar", reader.nextString());
    assertFalse(reader.hasNext());
  }
}