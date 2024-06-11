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

  private JsonElement createJsonElement(String json) {
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonReader.peek());
    return jsonTreeReader.nextElement();
  }

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{}"));
    reader.beginObject();
    assertNull(reader.nextName());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[]"));
    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testJsonObjectWithArrayProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": [1, 2, 3]}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertSame(1, reader.nextInt());
    assertSame(1, reader.nextInt());
    assertSame(2, reader.nextInt());
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithObjectProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": {\"bar\": 1}}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.beginObject();
    assertEquals("bar", reader.nextName());
    assertSame(1, reader.nextInt());
    reader.endObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithStringProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": \"bar\"}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithBooleanProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": true}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertTrue(reader.nextBoolean());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithNumberProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": 1.5}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals(1.5, reader.nextDouble(), 0.0);
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithNullProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": null}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.nextNull();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.close();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderEmptyStack() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[]"));
    reader.popStack();
    reader.nextInt();
  }

  @Test
  public void testJsonTreeReaderHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": [1, 2, 3]}"));
    assertTrue(reader.hasNext());
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertSame(1, reader.nextInt());
    reader.endArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderPeek() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": [1, 2, 3]}"));
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertEquals(1, reader.peek().getAsInt());
    assertSame(1, reader.nextInt());
    reader.endArray();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderUnexpectedJsonToken() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{}"));
    reader.beginArray();
    reader.nextInt();
  }

  @Test
  public void testJsonTreeReaderPromoteNameToValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": 1}"));
    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testJsonTreeReaderGetPath() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"foo\": [1, 2]}"));
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endObject();
    reader.close();
    assertEquals("foo[1]", reader.getPath());
  }
}