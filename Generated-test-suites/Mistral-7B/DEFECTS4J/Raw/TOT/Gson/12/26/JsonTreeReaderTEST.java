package com.google.gson.internal.bind.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

  @Test
  public void testEmpty() throws IOException {
    JsonElement element = JsonElement.parse("{}");
    JsonTreeReader reader = new JsonTreeReader(element);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    JsonElement element = JsonElement.parse("{\"name\": \"value\"}");
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
    assertNull(reader.nextName());
    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    JsonElement element = JsonElement.parse("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals("1", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    assertEquals("2", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    assertEquals("3", reader.nextString());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getAsInt());
    reader.endDocument();

    reader.close();
  }

  @Test
  public void testComplexObject() throws IOException {
    JsonElement element = JsonElement.parse("{\"name\": {}}");
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertNull(reader.nextName());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertNull(reader.nextName());
    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testComplexArray() throws IOException {
    JsonElement element = JsonElement.parse("[1, {\"name\": \"value\"}, [3, 4]]");
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals("1", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("value", reader.nextString());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    reader.nextString();

    assertNull(reader.nextName());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    reader.nextString(); // skip "value"

    assertEquals("3", reader.nextInt());
    assertEquals("4", reader.nextInt());

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getAsInt());
    reader.endDocument();

    reader.close();
  }

  @Test
  public void testErrorScenarios() throws IOException {
    JsonElement element = JsonElement.parse("{"); // missing end object
    JsonTreeReader reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::nextName);

    element = JsonElement.parse("{{"); // nested objects without commas
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::nextName);

    element = JsonElement.parse("{[1]}"); // array inside object
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::beginArray);

    element = JsonElement.parse("[1, 2]"); // missing comma between array elements
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::nextString);

    element = JsonElement.parse("{[1]}"); // array inside object
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::beginArray);

    element = JsonElement.parse("{1}"); // number as key
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::nextName);

    element = JsonElement.parse("{name: 1}"); // number as value
    reader = new JsonTreeReader(element);
    assertThrows(IllegalStateException.class, reader::nextString);

    reader.close();
  }
}