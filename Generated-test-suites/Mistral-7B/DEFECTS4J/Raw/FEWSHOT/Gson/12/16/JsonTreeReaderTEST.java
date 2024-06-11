package com.google.gson.internal.bind;

import static com.google.gson.JsonElement.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonElement element = parse("{\"foo\": 1, \"bar\": [1, \"apples\", 3.14], \"baz\": null}");

  @Test
  public void testHasNext() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    assertTrue(reader.hasNext());
    assertTrue(reader.hasNext());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getKind());
    reader.beginObject();
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("foo", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getKind());
    assertEquals(1.0, reader.nextDouble());
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("bar", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getKind());
    assertEquals(1, reader.nextInt());
    assertEquals(JsonToken.STRING, reader.peek().getKind());
    assertEquals("apples", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek().getKind());
    assertEquals(3.14, reader.nextDouble());
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("baz", reader.nextName());
    assertEquals(JsonToken.NULL, reader.peek().getKind());
    assertNull(reader.nextNull());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testBeginArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginArray();
    JsonArray array = (JsonArray) reader.peekStack();
    assertEquals("foo", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getKind());
    assertEquals(JsonToken.END_ARRAY, reader.peek(2).getKind());
    reader.endArray();
    assertEquals(null, reader.peekStack());
    assertEquals(null, reader.peekStack(1));
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    reader.nextName();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
  }

  @Test
  public void testBeginObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginObject();
    JsonObject object = (JsonObject) reader.peekStack();
    assertEquals("foo", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("foo", reader.nextName());
    assertEquals(1.0, reader.nextDouble());
    assertEquals("bar", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getKind());
    reader.endObject();
    assertEquals(null, reader.peekStack());
    assertEquals(null, reader.peekStack(1));
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    reader.nextName();
    assertEquals("baz", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
  }

  @Test
  public void testSkipValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.nextName();
    assertEquals("foo", reader.pathNames[reader.stackSize - 2]);
    reader.skipValue();
    assertEquals("null", reader.pathNames[reader.stackSize - 1]);
    assertEquals(1, reader.pathIndices[reader.stackSize - 1]);
    reader.nextName();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
  }

  @Test
  public void testGetPath() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals("$foo", reader.getPath());
    reader.beginArray();
    assertEquals("$foo[0]", reader.getPath());
    reader.beginObject();
    reader.nextName();
    assertEquals("$foo.bar", reader.getPath());
    reader.nextName();
    assertEquals("$foo.bar[0]", reader.getPath());
    reader.nextName();
    assertEquals("$foo.bar[1]", reader.getPath());
    reader.endObject();
    assertEquals("$foo[1]", reader.getPath());
    reader.nextName();
    assertEquals("$foo.baz", reader.getPath());
    reader.nextNull();
    assertEquals("$foo.baz", reader.getPath());
    reader.endObject();
    reader.nextName();
    assertEquals("$bar", reader.getPath());
    reader.nextInt();
    assertEquals("$bar[0]", reader.getPath());
    reader.nextString();
    assertEquals("$bar[1]", reader.getPath());
    reader.nextDouble();
    assertEquals("$bar[2]", reader.getPath());
    reader.nextName();
    assertEquals("$baz", reader.getPath());
    reader.nextNull();
    reader.endObject();
    reader.nextName();
    assertEquals("null", reader.getPath());
    reader.close();
  }

  @Test
  public void testPromoteNameToValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.promoteNameToValue();
    assertEquals("foo", reader.pathNames[reader.stackSize - 1]);
    assertEquals(1.0, reader.nextDouble());
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("bar", reader.nextName());
    reader.promoteNameToValue();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(1, reader.nextInt());
    assertEquals(JsonToken.STRING, reader.peek().getKind());
    assertEquals("apples", reader.nextString());
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    assertEquals("baz", reader.nextName());
    assertEquals(null, reader.nextNull());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testClose() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.close();
    assertSame(JsonReader.class, reader.getClass());
    assertEquals(0, reader.stackSize);
    assertNull(reader.peekStack());
    reader.nextName();
    assertEquals("foo", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    assertEquals(JsonToken.NAME, reader.peek().getKind());
    reader.nextName();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    reader.nextInt();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(1, reader.pathIndices[reader.stackSize - 1]);
    reader.nextString();
    assertEquals("baz", reader.pathNames[reader.stackSize - 1]);
    reader.nextNull();
    reader.nextName();
    assertEquals("null", reader.pathNames[reader.stackSize - 1]);
    assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    reader.nextName();
    assertEquals("baz", reader.pathNames[reader.stackSize - 1]);
    reader.nextNull();
    reader.endObject();
    assertEquals(null, reader.peekStack());
    assertEquals(null, reader.peekStack(1));
    reader.nextName();
    assertEquals("bar", reader.pathNames[reader.stackSize - 1]);
    assertEquals(2, reader.pathIndices[reader.stackSize - 1]);
    reader.nextInt();
    reader.nextString();
    reader.nextDouble();
    reader.nextName();
    assertEquals("baz", reader.pathNames[reader.stackSize - 1]);
    reader.nextNull();
    reader.endObject();
    reader.nextName();
    assertEquals("null", reader.pathNames[reader.stackSize - 1]);
    reader.close();
    assertSame(JsonReader.class, reader.getClass());
    assertEquals(0, reader.stackSize);
    assertNull(reader.peekStack());
  }

  @Test
  public void testUnexpectedToken() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(parse("{\"foo\": 1}"));
    reader.nextName();
    reader.nextName();
    reader.nextName();
    reader.expect(JsonToken.NAME);
  }

  @Test(expected = AssertionError.class)
  public void testUnexpectedTokenWithErrorMessage() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(parse("{\"foo\": 1}"));
    reader.nextName();
    reader.nextName();
    reader.nextName();
    reader.expect(JsonToken.NUMBER);
  }
}