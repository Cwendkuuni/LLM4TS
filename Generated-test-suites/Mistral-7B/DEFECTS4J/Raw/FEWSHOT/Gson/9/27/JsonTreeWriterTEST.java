package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertNull(jsonElement);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonArray.class, jsonElement.getClass());
    assertEquals(0, ((JsonArray) jsonElement).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenObjectIsOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginArray();
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonArray.class, jsonElement.getClass());
    assertEquals(0, ((JsonArray) jsonElement).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenObjectIsNotOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonObject.class, jsonElement.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenArrayIsOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonObject.class, jsonElement.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenArrayIsNotOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("name"));
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenNoObjectIsOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("name");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("name"));
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("isAdmin");
    jsonTreeWriter.value(true);
    jsonTreeWriter
        .endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("isAdmin"));
    assertEquals(true, jsonObject.get("isAdmin").getAsBoolean());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("age");
    jsonTreeWriter.value(27);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("age"));
    assertEquals(27, jsonObject.get("age").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testNullValueWhenObjectIsNotOnStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.nullValue();
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("isAdmin");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("isAdmin"));
    assertNull(jsonObject.get("isAdmin"));
  }

  @Test
  public void testValueArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("values");
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("Apple");
    jsonTreeWriter.value("Banana");
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.hasKey("values"));
    JsonArray jsonArray = (JsonArray) jsonObject.get("values");
    assertEquals(2, jsonArray.size());
    assertEquals("Apple", jsonArray.get(0).getAsString());
    assertEquals("Banana", jsonArray.get(1).getAsString());
  }
}