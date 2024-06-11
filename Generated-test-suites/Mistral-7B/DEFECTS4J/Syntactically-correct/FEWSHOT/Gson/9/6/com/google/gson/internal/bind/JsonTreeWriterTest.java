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
  public void testGet_emptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();

    assertNull(result);
  }

  @Test
  public void testGet_objectWithValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("test", "hello");

    assertSame(expected, result);
  }

  @Test
  public void testGet_arrayWithValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello");
    writer.endArray();

    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("hello"));

    assertSame(expected, result);
  }

  @Test
  public void testGet_emptyArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();

    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();

    assertSame(expected, result);
  }

  @Test
  public void testGet_emptyObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();

    assertSame(expected, result);
  }

  @Test
  public void testGet_objectWithEmptyName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();

    assertSame(expected, result);
  }

  @Test
  public void testGet_objectWithMultipleProperties() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test1").value("hello1");
    writer.name("test2").value("hello2");
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("test1", "hello1");
    expected.addProperty("test2", "hello2");

    assertSame(expected, result);
  }

  @Test
  public void testGet_arrayWithMultipleElements() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.endArray();

    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("hello1"));
    expected.add(new JsonPrimitive("hello2"));

    assertSame(expected, result);
  }

  @Test
  public void testGet_nesting() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test").beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.endArray().endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive("hello1"));
    array.add(new JsonPrimitive("hello2"));
    expected.add("test", array);

    assertSame(expected, result);
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test").value("hello");
    writer.endObject();

    writer.close();

    JsonElement result = writer.get();
    JsonPrimitive sentinel = writer.peek();

    assertSame(sentinel, SENTINEL_CLOSED);
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testClose_unclosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test").value("hello");

    writer.get(); // consume the result

    writer.close();

    JsonElement unused = writer.get(); // should not be reached
  }

  @Test
  public void testBeginArray_nullName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();

    assertFalse(writer.hasName());
  }

  @Test
  public void testBeginArray_nonNullName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.name("test");
    writer.beginArray();

    assertTrue(writer.hasName());
    assertSame("test", writer.getName());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArray_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("test");
    writer.value("hello");
    writer.endObject();

    writer.beginArray(); // should fail
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObject_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello");
    writer.endArray();

    writer.beginObject(); // should fail
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObject_nonNullName_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello");
    writer.endArray();

    writer.name("test");
    writer.beginObject(); // should fail
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.endArray();

    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("hello1"));
    expected.add(new JsonPrimitive("hello2"));

    assertSame(expected, result);

    JsonElement topElement = writer.peek();

    assertNull(topElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.name("test").value("hello3");

    writer.endArray(); // should fail
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test1").value("hello1");
    writer.name("test2").value("hello2");
    writer.endObject();

    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("test1", "hello1");
    expected.addProperty("test2", "hello2");

    assertSame(expected, result);

    JsonElement topElement = writer.peek();

    assertNull(topElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.endArray();

    writer.beginObject();
    writer.name("test").value("hello3");

    writer.endObject(); // should fail
  }

  @Test(expected = IllegalStateException.class)
  public void testName_nonEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello1");
    writer.value("hello2");
    writer.endArray();

    writer.name("test");
  }
}