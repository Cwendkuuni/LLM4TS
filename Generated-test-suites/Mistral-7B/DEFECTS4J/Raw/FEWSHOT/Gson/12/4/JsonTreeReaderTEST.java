package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonArray());
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    JsonArray array = (JsonArray) jsonTreeReader.peekStack();
    assertNotNull(array.iterator());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertNull(jsonTreeReader.peekStack());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testArrayWithOneElement() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("foo")));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    JsonArray array = (JsonArray) jsonTreeReader.peekStack();
    assertTrue(array.hasNext());
    JsonElement element = jsonTreeReader.nextElement();
    assertEquals("foo", element.getAsString());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    JsonPrimitive primitive = (JsonPrimitive) jsonTreeReader.nextElement();
    assertEquals("null", jsonTreeReader.nextName());
    popStack(jsonTreeReader);
    assertNull(jsonTreeReader.peekStack());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testArrayWithMultipleElements() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("foo"))
        .add(new JsonPrimitive("bar")));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    JsonArray array = (JsonArray) jsonTreeReader.peekStack();
    assertTrue(array.hasNext());
    JsonElement element1 = jsonTreeReader.nextElement();
    assertEquals("foo", element1.getAsString());
    assertTrue(array.hasNext());
    JsonElement element2 = jsonTreeReader.nextElement();
    assertEquals("bar", element2.getAsString());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    JsonPrimitive primitive = (JsonPrimitive) jsonTreeReader.nextElement();
    assertEquals("null", jsonTreeReader.nextName());
    popStack(jsonTreeReader);
    assertNull(jsonTreeReader.peekStack());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive(123)));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    JsonObject object = (JsonObject) jsonTreeReader.peekStack();
    Iterator<?> iterator = object.entrySet().iterator();
    assertEquals("foo", jsonTreeReader.nextName());
    assertEquals("bar", jsonTreeReader.nextString());
    assertEquals("baz", jsonTreeReader.nextName());
    assertEquals(123, jsonTreeReader.nextLong());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertNull(jsonTreeReader.peekStack());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testObjectWithMultipleProperties() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive(123)).addProperty("qux", new JsonPrimitive(true)));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    JsonObject object = (JsonObject) jsonTreeReader.peekStack();
    Iterator<?> iterator = object.entrySet().iterator();
    assertEquals("foo", jsonTreeReader.nextName());
    assertEquals("bar", jsonTreeReader.nextString());
    assertEquals("baz", jsonTreeReader.nextName());
    assertEquals(123, jsonTreeReader.nextLong());
    assertEquals("qux", jsonTreeReader.nextName());
    assertEquals(true, jsonTreeReader.nextBoolean());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertNull(jsonTreeReader.peekStack());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testStringValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar")));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testBooleanValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(true)));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextName());
    assertEquals(true, jsonTreeReader.nextBoolean());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testNumberValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(42)));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextName());
    assertEquals(42, jsonTreeReader.nextInt());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("foo", JsonNull.INSTANCE));
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextName());
    jsonTreeReader.nextNull();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    popStack(jsonTreeReader);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  private void popStack(JsonTreeReader jsonTreeReader) throws IOException {
    jsonTreeReader.skipValue();
  }

}