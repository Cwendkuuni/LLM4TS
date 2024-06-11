package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    assertEquals(JsonArray.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    assertEquals(JsonObject.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
  }

  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.name("name"));
  }

  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IOException.class, () -> jsonTreeWriter.value("value"));
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.nullValue();
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    assertEquals(JsonPrimitive.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(10.5);
    assertEquals(JsonPrimitive.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(100L);
    assertEquals(JsonPrimitive.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(50.0);
    assertEquals(JsonPrimitive.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testFlush() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
  public void testClose() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IOException.class, () -> jsonTreeWriter.close());
  }

}