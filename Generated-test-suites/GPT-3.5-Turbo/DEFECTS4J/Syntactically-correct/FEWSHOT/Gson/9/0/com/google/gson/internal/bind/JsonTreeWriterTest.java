
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class JsonTreeWriterTest {
  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonNull);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameOutsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test
  public void testValueWithString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals("value", element.getAsString());
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonNull);
  }

  @Test
  public void testValueWithBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    assertTrue(element.getAsBoolean());
  }

  @Test
  public void testValueWithDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(3.14, element.getAsDouble(), 0.0001);
  }

  @Test
  public void testValueWithLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100L);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(100L, element.getAsLong());
  }

  @Test
  public void testValueWithNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1234.5678);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(1234.5678, element.getAsDouble(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Long.MAX_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
    // Nothing to assert, just making sure it doesn't throw an error
  }

  @Test(expected = IOException.class)
  public void testCloseWithoutCompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }

  @Test
  public void testCloseWithCompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.close();

    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
  }
}
