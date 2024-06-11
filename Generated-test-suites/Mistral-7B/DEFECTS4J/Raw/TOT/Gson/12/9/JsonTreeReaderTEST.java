package com.google.gson.internal.bind; // or the package where the JsonTreeReader class is located

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader(""));
    assertEquals("", reader.getPath());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": \"John\"}"));
    assertEquals("$", reader.getPath());
    assertTrue(reader.hasNext());
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("name", reader.nextName()); // assert that the name was consumed
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("[]"));
    assertEquals("$", reader.getPath());
    assertFalse(reader.hasNext());
    reader.close();

    reader = new JsonTreeReader(JsonParser.parseReader("[1, 2, 3]"));
    assertEquals("$", reader.getPath());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getType());
    reader.beginArray();
    assertEquals(JsonToken.NUMBER, reader.peek().getType());
    assertEquals(1, reader.nextInt());
    assertEquals(JsonToken.COMMA, reader.peek().getType());
    reader.skipValue();
    assertEquals(JsonToken.NUMBER, reader.peek().getType());
    assertEquals(2, reader.nextInt());
    assertEquals(JsonToken.COMMA, reader.peek().getType());
    reader.skipValue();
    assertEquals(JsonToken.NUMBER, reader.peek().getType());
    assertEquals(3, reader.nextInt());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getType());
    reader.endArray();
    assertEquals("", reader.getPath());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testComplexObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}"));
    assertEquals("$", reader.getPath());
    assertTrue(reader.hasNext());
    assertEquals("name", reader.nextName());
    assertFalse(reader.hasNext()); // assert that the name was consumed

    reader.promoteNameToValue();

    assertTrue(reader.hasNext());
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("first", reader.nextName()); // assert that the name was consumed

    reader.promoteNameToValue();

    assertTrue(reader.hasNext());
    assertEquals("last", reader.nextName());
    assertEquals("Doe", reader.nextString());
    assertEquals("last", reader.nextName()); // assert that the name was consumed

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{}"));
    reader.close();
    reader.nextString();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
  }
}