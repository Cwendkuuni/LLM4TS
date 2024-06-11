package com.google.gson.internal.bind.test;

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
  public void testEmptyJsonElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testEmptyJsonArray() throws Exception {
    String jsonString = "[]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return true;
      }

      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginArray();
    assertNull(reader.nextName());
    reader.endArray();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testArrayWithOneElement() throws Exception {
    String jsonString = "[\"foo\"]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive("foo");
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return true;
      }

      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginArray();
    assertEquals("foo", reader.nextString());
    reader.endArray();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testArrayWithSeveralElements() throws Exception {
    String jsonString = "[\"foo\", 1, true]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return true;
      }

      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginArray();
    assertEquals("foo", reader.nextString());
    assertTrue(reader.nextBoolean());
    assertEquals(1, reader.nextInt());
    reader.endArray();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonObjectWithOneNameValuePair() throws Exception {
    String jsonString = "{\"foo\": 1}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return false;
      }

      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals(1, reader.nextInt());
    reader.endObject();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonObjectWithSeveralNameValuePairs() throws Exception {
    String jsonString = "{\"foo\": 1, \"bar\": \"hello\"}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return false;
      }

      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals(1, reader.nextInt());
    assertEquals("hello", reader.nextString());
    reader.endObject();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testNestedJsonObjects() throws Exception {
    String jsonString = "{\"foo\": { \"bar\": 1, \"baz\": [1, 2, 3] } }";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return false;
      }

      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.beginObject();
    assertEquals("bar", reader.nextName());
    assertEquals(1, reader.nextInt());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endObject();
    reader.endObject();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testNestedJsonArrays() throws Exception {
    String jsonString = "[1, [1, [2, 3]], [4, [5]]]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return true;
      }

      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.beginArray();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.beginArray();
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endArray();
    reader.beginArray();
    assertEquals(4, reader.nextInt());
    reader.beginArray();
    assertEquals(5, reader.nextInt());
    reader.endArray();
    reader.endArray();
    reader.endArray();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedJsonReader() throws Exception {
    String jsonString = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public boolean isJsonNull() {
        return false;
      }

      @Override public boolean isJsonArray() {
        return true;
      }

      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return null;
      }

      @Override public String getAsString() {
        return jsonString;
      }
    });

    reader.close();

    reader.beginArray();
  }
}