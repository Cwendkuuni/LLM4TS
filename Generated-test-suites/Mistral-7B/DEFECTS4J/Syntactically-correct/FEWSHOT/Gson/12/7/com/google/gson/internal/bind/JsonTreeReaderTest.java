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
  private static final JsonElement EMPTY_JSON_ARRAY = new JsonArray();
  private static final JsonElement EMPTY_JSON_OBJECT = new JsonObject();

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();

    reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.close();

    reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.beginArray();
    reader.endArray();
    reader.close();

    reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.beginObject();
    reader.endObject();
    reader.close();

    reader = new JsonTreeReader(EMPTY_JSON_ARRAY);
    reader.close();

    reader = new JsonTreeReader(EMPTY_JSON_OBJECT);
    reader.close();
  }

  @Test
  public void testSingleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, 2, 3]"));

    reader.beginArray();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testSingleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"name\": \"John\", \"age\": 30}"));

    reader.beginObject();
    String name = reader.nextName();
    assertEquals("name", name);
    assertEquals("John", reader.nextString());
    String ageStr = reader.nextName();
    assertEquals("age", ageStr);
    assertEquals(30, reader.nextInt());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNestedJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, [2, 3], [4, 5]]"));

    reader.beginArray();
    reader.beginArray();
    assertEquals(2, reader.nextInt());
    reader.beginArray();
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.beginArray();
    assertEquals(4, reader.nextInt());
    assertEquals(5, reader.nextInt());
    reader.endArray();
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testNestedJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{" +
        "\"name\": {" +
        "\"first\": \"John\"," +
        "\"last\": \"Doe\"" +
        "}," +
        "\"age\": 30" +
        "}"));

    reader.beginObject();
    String nameFirst = reader.nextName();
    assertEquals("first", nameFirst);
    assertEquals("John", reader.nextString());

    String nameLast = reader.nextName();
    assertEquals("last", nameLast);
    assertEquals("Doe", reader.nextString());

    String ageStr = reader.nextName();
    assertEquals("age", ageStr);
    assertEquals(30, reader.nextInt());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testReadNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(null));
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testReadBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
    assertTrue(reader.nextBoolean());
    reader.close();

    reader = new JsonTreeReader(new JsonPrimitive(false));
    assertFalse(reader.nextBoolean());
    reader.close();
  }

  @Test
  public void testReadString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("hello"));
    assertEquals("hello", reader.nextString());
    reader.close();
  }

  @Test
  public void testReadNumber() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(1.2));
    assertEquals(1.2, reader.nextDouble(), 0.0);
    reader.close();

    reader = new JsonTreeReader(new JsonPrimitive(123));
    assertEquals(123, reader.nextInt());
    reader.close();
  }

  @Test
  public void testReadArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, 2, 3]"));

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testReadObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{" +
        "\"name\": \"John\"," +
        "\"age\": 30" +
        "}"));

    reader.beginObject();
    String name = reader.nextName();
    assertEquals("name", name);
    assertEquals("John", reader.nextString());

    String ageStr = reader.nextName();
    assertEquals("age", ageStr);
    assertEquals(30, reader.nextInt());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testReadEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[]"));

    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testReadEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{}"));

    reader.beginObject();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, 2]"));
    reader.close();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testReadAfterClose() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, 2]"));
    reader.close();
    reader.nextInt();
  }
}