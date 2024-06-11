package com.google.gson.internal.bind.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderObject() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {{
      setValue(new JsonObject() {{
        addProperty("name", new JsonPrimitive("John"));
        addProperty("age", new JsonPrimitive(30));
      }});
    }});

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.skipValue(); // move past "John"
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.skipValue(); // move past age
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderArray() throws IOException {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {{
      setValue(new JsonArray() {{
        add(new JsonPrimitive(1));
        add(new JsonPrimitive(2));
        add(new JsonPrimitive(3));
      }});
    }});

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals("1", reader.getPath());
    assertEquals(1, reader.nextInt());

    assertEquals("2", reader.getPath());
    reader.skipValue(); // move past 1

    assertEquals("3", reader.getPath());
    assertEquals(3, reader.nextInt());

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderNull() throws IOException {
    String json = "null";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {{
      setValue(JsonNull.INSTANCE);
    }});

    assertEquals(JsonToken.NULL, reader.peek());
    reader.nextNull();

    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.nextString(); // this should throw an exception
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWrongToken() throws IOException {
    String json = "{\"name\": \"John\"}"; // missing closing brace
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {{
      setValue(new JsonObject() {{
        addProperty("name", new JsonPrimitive("John"));
      }});
    }});

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.skipValue(); // move past "John"

    // This should throw an exception
    reader.nextBoolean();
  }
}