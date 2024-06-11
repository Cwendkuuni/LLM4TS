package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSingleObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{}"));
    assertEquals("", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testSingleArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[]"));
    reader.close();
  }

  @Test
  public void testObjectWithSingleNameValuePair() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"name\": \"value\"}"));
    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testObjectWithSingleNameArrayValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"name\": [1, 2, 3]}"));;
    assertEquals("name", reader.nextName());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testObjectWithSingleNameObjectValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("{\"name\": {\"foo\": \"bar\"}}"));;
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    reader.endObject();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testArrayWithSingleValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.endArray();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testArrayWithMultipleValues() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("[1, 2, 3]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testArrayWithNestedObjects() throws Exception {
    String json = "[" +
        "  { " +
        "    \"name\": \"foo\"," +
        "    \"value\": {" +
        "      \"inner\": [1, 2]," +
        "      \"outer\": \"bar\"}" +
        "  }," +
        "  { " +
        "    \"name\": \"qux\"," +
        "    \"value\": {" +
        "      \"inner\": [3, 4]," +
        "      \"outer\": \"quux\"" +
        "    }" +
        "  }" +
        "]";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    reader.beginArray();
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    reader.beginObject();
    assertEquals("inner", reader.nextName());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    reader.endArray();
    assertEquals("outer", reader.nextName());
    assertEquals("bar", reader.nextString());
    reader.endObject();
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("qux", reader.nextName());
    reader.beginObject();
    assertEquals("inner", reader.nextName());
    reader.beginArray();
    assertEquals(3, reader.nextInt());
    assertEquals(4, reader.nextInt());
    reader.endArray();
    assertEquals("outer", reader.nextName());
    assertEquals("quux", reader.nextString());
    reader.endObject();
    reader.endObject();
    reader.endArray();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.nextInt();
  }
}