package com.google.gson.internal.bind;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGetReturnsSentinalWhenClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertEquals(JsonPrimitive.class, writer.get().getClass());
    assertEquals("closed", writer.get().getAsString());
  }

  @Test
  public void testGetReturnsLastElementAdded() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("foo"));
    expected.add(new JsonPrimitive("bar"));
    assertEquals(expected, result);
  }

  @Test
  public void testGetReturnsSentinalWhenEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals("closed", result.getAsString());
  }

  @Test
  public void testBeginArrayAddsElementToStackAndReturnsWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray array = writer.beginArray();
    JsonElement arrayElement = new JsonPrimitive("foo");
    writer.put(arrayElement);
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(arrayElement);
    assertEquals(expected, result);
  }

  @Test
  public void testBeginArrayDoesNotAddElementToStackIfClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertThrows(IllegalStateException.class, () -> writer.beginArray());
  }

  @Test
  public void testBeginArrayAddsToTopOfStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    JsonArray array = writer.beginArray();
    JsonElement result = writer.get();
    JsonObject expectedObject = new JsonObject();
    JsonArray expectedArray = new JsonArray();
    expectedObject.add("array", expectedArray);
    assertEquals(expectedObject, result);
  }

  @Test
  public void testBeginArrayAddsElementToArrayInTopOfStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject obj = writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("array", new JsonArray().add(new JsonPrimitive("foo")));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObjectAddsObjectToStackAndReturnsWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    JsonElement key = new JsonPrimitive("foo");
    JsonPrimitive value = new JsonPrimitive("bar");
    writer.name(key.getAsString());
    writer.value(value);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.add(key, value);
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObjectDoesNotAddElementToStackIfClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertThrows(IllegalStateException.class, () -> writer.beginObject());
  }

  @Test
  public void testBeginObjectAddsObjectToTopOfStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object1 = writer.beginObject();
    JsonObject object2 = writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject().add(new JsonPrimitive("key"), new JsonPrimitive("value"));
    assertEquals(expected, result);
  }

  @Test
  public void testNameAddsNameToPendingName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", null);
    assertEquals(expected, result);
  }

  @Test
  public void testNameDoesNotAddNameToPendingNameIfClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertThrows(IllegalStateException.class, () -> writer.name("name"));
  }

  @Test
  public void testNameDoesNotAddNameToPendingNameIfInArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertThrows(IllegalStateException.class, () -> writer.name("name"));
  }

  @Test
  public void testNameDoesNotAddNameToPendingNameIfInObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertThrows(IllegalStateException.class, () -> writer.name("name"));
  }

  @Test
  public void testValueAddsElementToLastObjectInStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", "value");
    assertEquals(expected, result);
  }

  @Test
  public void testValueDoesNotAddElementToLastObjectInStackIfClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertThrows(IllegalStateException.class, () -> writer.value("value"));
  }

  @Test
  public void testValueDoesNotAddElementToLastObjectInStackIfNotInObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertThrows(IllegalStateException.class, () -> writer.value("value"));
  }

  @Test
  public void testValueAddsElementToLastArrayInStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value1");
    writer.value("value2");
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray().add(new JsonPrimitive("value1")).add(new JsonPrimitive("value2"));
    assertEquals(expected, result);
  }

  @Test
  public void testCloseDoesNotAddElementToStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals("closed", result.getAsString());
  }

  @Test
  public void testCloseThrowsIfNotEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    assertThrows(IllegalStateException.class, () -> writer.close());
  }

  @Test
  public void testCloseDoesNotThrowIfEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals("closed", result.getAsString());
  }

  @Test
  public void testToStringReturnsJsonTreeAsString() throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(os);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
    String result = os.toString();
    String expected = "{\"name\":\"value\"}";
    assertEquals(expected, result);
  }
}