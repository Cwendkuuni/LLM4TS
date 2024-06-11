package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonTreeReaderArray() throws IOException {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive(json);
      }

      @Override
      public JsonArray getAsJsonArray() {
        return new JsonElement() {
          @Override
          public Iterator<JsonElement> iterator() {
            return new Iterator<JsonElement>() {
              int index = 0;

              @Override
              public boolean hasNext() {
                return index < 3;
              }

              @Override
              public JsonElement next() {
                return new JsonPrimitive(String.valueOf(index++));
              }
            };
          }
        };
      }
    });

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(2, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(3, reader.nextInt());

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonTreeReaderObject() throws IOException {
    String json = "{\"foo\": 1, \"bar\": [1, 2, 3], \"baz\": null}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonArray getAsJsonArray() {
        return null;
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {
          {
            put("foo", new JsonPrimitive(1));
            put("bar", new JsonArray() {
              {
                add(new JsonPrimitive(1));
                add(new JsonPrimitive(2));
                add(new JsonPrimitive(3));
              }
            });
            put("baz", JsonNull.INSTANCE);
          }
        };
      }
    });

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals(JsonToken.NAME, reader.peek().getAsString());
    assertEquals("foo", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NAME, reader.peek().getAsString());
    assertEquals("bar", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(2, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(3, reader.nextInt());

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertEquals(JsonToken.NAME, reader.peek().getAsString());
    assertEquals("baz", reader.nextName());
    assertNull(reader.nextNull());

    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertFalse(reader.hasNext());
  }
}