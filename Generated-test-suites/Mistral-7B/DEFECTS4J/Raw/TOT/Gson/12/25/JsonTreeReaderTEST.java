package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(""));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("[]"));
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testEmptyObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("{}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.endObject();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("[1,2,3]"));
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testNestedArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("[1,[2,3]]"));;
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endArray();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\":\"John\", \"age\": 30}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.endObject();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testNestedObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\":{\"first\": \"John\",\"last\": \"Doe\"}}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("last", reader.nextName());
    assertEquals("Doe", reader.nextString());
    reader.endObject();
    reader.endObject();
    assertNull(reader.peek());
    reader.close();
  }

  @Test
  public void testNullValue() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("null"));
    assertTrue(reader.hasNext());
    assertNull(reader.nextNull());
    assertNull(reader.peek());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader("[]"));
    reader.close();
    reader.nextInt();
  }

  @Test(expected = AssertionError.class)
  public void testInvalidJson() throws IOException {
    JsonReader reader = new JsonTreeReader(new JsonReader(new ByteArrayReader(new ByteArrayInputStream(new byte[]{"{"}))));
    reader.nextInt();
  }
}