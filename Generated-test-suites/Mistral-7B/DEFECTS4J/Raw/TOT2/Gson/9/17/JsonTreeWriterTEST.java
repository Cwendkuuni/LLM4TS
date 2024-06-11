package com.example.junit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_writeSimpleJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1").value("value1");
    jsonTreeWriter.name("key2").value(123);
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("value1", jsonObject.get("key1").getAsString());
    assertEquals(123, jsonObject.get("key2").getAsInt());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value(123);
    jsonTreeWriter.endArray();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();

    assertEquals("element1", jsonArray.get(0).getAsString());
    assertEquals(123, jsonArray.get(1).getAsInt());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeEmptyJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertTrue(jsonObject.entrySet().isEmpty());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeNullJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1").nullValue();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertTrue(jsonObject.hasNull("key1"));
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeNestedJsonObjects() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1").beginObject();
    jsonTreeWriter.name("key1_1").value("value1_1");
    jsonTreeWriter.name("key1_2").value(123);
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("key2").value("value2");
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    JsonObject key1Object = jsonObject.getAsJsonObject("key1");
    assertEquals("value1_1", key1Object.get("key1_1").getAsString());
    assertEquals(123, key1Object.get("key1_2").getAsInt());
    assertEquals("value2", jsonObject.get("key2").getAsString());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_throwExceptionOnUnsupportedWriteOperations() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.write(new String("test"))); // Write string
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.write(true)); // Write boolean
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.write(123.0)); // Write double
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.write(123L)); // Write long
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.write(new Object())); // Write non-supported types
  }

  @Test
  public void testJsonTreeWriter_writeEmptyJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();

    assertTrue(jsonArray.isEmpty());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeNullJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginArray();
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endArray();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();

    assertTrue(jsonArray.isEmpty());
    assertTrue(jsonArray.hasNull());
    assertNotNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeInvalidNesting() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1").beginArray(); // Key1 should be JsonObject
    jsonTreeWriter.value("element1");
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    assertThrows(IllegalStateException.class, jsonTreeWriter::get);
  }
}