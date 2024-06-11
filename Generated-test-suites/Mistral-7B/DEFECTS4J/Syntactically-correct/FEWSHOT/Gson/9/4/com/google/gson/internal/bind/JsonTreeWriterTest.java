package com.google.gson.internal.bind;

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

public class JsonTreeWriterTest {

  @Test
  public void testGetReturnsEmptyJsonElementOnEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testGetReturnsLastJsonElementAddedToStack() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.endArray();

    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("foo"));
    expected.add(new JsonPrimitive("bar"));

    assertEquals(expected, result);
  }

  @Test
  public void testGetReturnsLastJsonObjectAddedToStackWithNameValuePair() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected, result);
  }

  @Test
  public void testGetReturnsLastJsonObjectAddedToStackWithMultipleNameValuePairs() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test
  public void testBeginArrayAddsJsonArrayToStack() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();

    JsonElement array = writer.peek();

    assertTrue(array instanceof JsonArray);
  }

  @Test
  public void testEndArrayRemovesJsonArrayFromStack() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.endArray();

    JsonElement array = writer.peek();

    assertNull(array);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayThrowsExceptionWhenTopElementIsNotJsonArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endArray();

    writer.endArray();
  }

  @Test
  public void testBeginObjectAddsJsonObjectToStack() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();

    JsonElement object = writer.peek();

    assertTrue(object instanceof JsonObject);
  }

  @Test
  public void testEndObjectRemovesJsonObjectFromStack() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();

    JsonElement object = writer.peek();

    assertNull(object);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectThrowsExceptionWhenTopElementIsNotJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value("foo");
    writer.endObject();
  }

  @Test
  public void testNameSetsPendingNameForNextJsonObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();

    assertEquals("name", writer.pendingName);
  }

  @Test
  public void testNameDoesNotSetPendingNameForJsonArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.name("name");
    writer.value("John Doe");
    writer.endArray();

    assertNull(writer.pendingName);
  }
}