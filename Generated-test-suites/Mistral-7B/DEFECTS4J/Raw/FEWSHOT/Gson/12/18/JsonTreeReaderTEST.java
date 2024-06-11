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

  private static JsonElement createMockJsonElement(JsonToken token, Object value) {
    switch (token) {
      case BEGIN_ARRAY:
        return new JsonArray();
      case BEGIN_OBJECT:
        return new JsonObject();
      case NAME:
        return new JsonPrimitive(token.name());
      case BOOLEAN:
        return new JsonPrimitive(true);
      case NULL:
        return JsonNull.INSTANCE;
      case NUMBER:
        return new JsonPrimitive(value.toString());
      case STRING:
        return new JsonPrimitive(value.toString());
    }
    throw new AssertionError();
  }

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSingleJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSingleJsonArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    reader.nextNull();
    reader.endArray();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonArrayWithTwoElements() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonElement firstElement = new JsonPrimitive(1);
    JsonElement secondElement = new JsonPrimitive("two");
    ((JsonArray) jsonElement).add(firstElement);
    ((JsonArray) jsonElement).add(secondElement);

    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertSame(firstElement.getClass(), reader.peek().getClass());
    reader.nextNull(); // skip the first element

    assertSame(secondElement.getClass(), reader.peek().getClass());
    reader.nextString(); // read the second element

    reader.endArray();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonObjectWithTwoProperties() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonElement firstName = new JsonPrimitive("John");
    JsonElement lastName = new JsonPrimitive("Doe");
    ((JsonObject) jsonElement).addProperty("firstName", firstName);
    ((JsonObject) jsonElement).addProperty("lastName", lastName);

    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals("firstName", reader.nextName());
    assertEquals(firstName.getClass(), reader.peek().getClass());
    reader.nextString(); // read the value

    assertEquals("lastName", reader.nextName());
    assertEquals(lastName.getClass(), reader.peek().getClass());
    reader.nextString(); // read the value

    reader.endObject();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonObjectWithOnePropertyAndOneNullValue() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonElement name = new JsonPrimitive("John");
    ((JsonObject) jsonElement).addProperty("lastName", null);
    ((JsonObject) jsonElement).addProperty("firstName", name);

    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals("firstName", reader.nextName());
    assertSame(name.getClass(), reader.peek().getClass());
    reader.nextString(); // read the value

    reader.nextNull(); // skip the "lastName" property

    reader.endObject();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonObjectWithEmptyName() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonElement value = new JsonPrimitive(true);
    ((JsonObject) jsonElement).addProperty("", value);

    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertNull(reader.nextName());
    assertSame(value.getClass(), reader.peek().getClass());
    reader.nextBoolean(); // read the value

    reader.endObject();
    reader.nextNull();
    assertFalse(reader.hasNext());
  }
}