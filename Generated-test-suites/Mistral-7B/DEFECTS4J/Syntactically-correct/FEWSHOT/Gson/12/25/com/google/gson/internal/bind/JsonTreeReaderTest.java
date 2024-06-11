package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[]"));
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getKind());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[]"));
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testBeginArrayWithContent() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("['foo', 'bar']"));
    reader.beginArray();
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("foo", reader.nextString());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("bar", reader.nextString());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[]"));
    reader.beginArray();
    reader.beginArray();
  }

  @Test
  public void testEndArrayMissingBegin() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[]"));
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = AssertionError.class)
  public void testReadFromUnreadableReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement());
    reader.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{}"));
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{}"));
    reader.beginObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testBeginObjectWithContent() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{'name': 'foo', 'age': 30}"));
    reader.beginObject();
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("foo", reader.nextString());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(30, reader.nextInt());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{}"));
    reader.beginObject();
    reader.beginObject();
  }

  @Test
  public void testEndObjectMissingBegin() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{}"));
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = AssertionError.class)
  public void testReadFromUnreadableReaderObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement());
    reader.beginObject();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{'name': 'foo'}"));
    assertTrue(reader.hasNext());
    reader.nextName();
    assertTrue(reader.hasNext());
    reader.beginArray();
    reader.endArray();
    assertTrue(reader.hasNext());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{'name': 'foo'}"));
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    reader.nextName();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getKind());
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_OBJECT, reader.peek().getKind());
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextName() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{'name': 'foo'}"));
    assertEquals("name", reader.nextName());
    reader.nextName(); // this should be empty
    assertEquals("", reader.nextName());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(true));
    assertTrue(reader.nextBoolean());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(JsonNull.INSTANCE));
    reader.nextNull();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(42));
    assertEquals(42, reader.nextInt());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextLong() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(42L));
    assertEquals(42L, reader.nextLong());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(3.14));
    assertEquals(3.14, reader.nextDouble(), 0.001);
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("foo"));
    assertEquals("foo", reader.nextString());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextStringWithEscapedQuotes() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("\"foo\""));
    assertEquals("foo", reader.nextString());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  @Test
  public void testNextStringWithNullValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement(JsonNull.INSTANCE));
    reader.nextNull();
    reader.nextString(); // this should be an empty string
    assertEquals("", reader.nextString());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getKind());
  }

  private JsonElement createJsonElement(Object value) {
    return new JsonPrimitive(value);
  }

  private JsonElement createJsonElement(String jsonString) {
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    JsonElement result;
    try {
      result = jsonReader.readTree();
    } finally {
      jsonReader.close();
    }
    return result;
  }
}