package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSingleObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertEquals("", reader.nextName());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSingleArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testSingleString() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("hello"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("hello", reader.nextString());
    reader.close();
  }

  @Test
  public void testSingleNumber() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(42));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(42.0, reader.nextDouble());
    reader.close();
  }

  @Test
  public void testSingleBoolean() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BOOLEAN, reader.peek().getAsInt());
    assertEquals(true, reader.nextBoolean());
    reader.close();
  }

  @Test
  public void testSingleNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NULL, reader.peek().getAsInt());
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testComplexJson() throws Exception {
    String json = "{\"name\":\"John\",\"age\":30,\"city\":[\"New York\",\"Los Angeles\",\"Chicago\"]}";
    JsonTreeReader reader = new JsonTreeReader(new JsonObject(new JsonParser().parse(json)));

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30.0, reader.nextDouble());

    assertEquals("city", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertEquals("", reader.nextName());
    assertEquals("New York", reader.nextString());

    assertEquals("", reader.nextName());
    assertEquals("Los Angeles", reader.nextString());

    assertEquals("", reader.nextName());
    assertEquals("Chicago", reader.nextString());

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    reader.endObject();
    reader.close();
  }
}