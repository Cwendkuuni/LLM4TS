package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.internal.DoNotInstrument;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonWriter.Feature;

@RunWith(org.robolectric.RobolectricTestRunner.class)
public class JsonTreeWriterTest {

  private static final String TEST_JSON = "{\"name\":\"John\",\"age\":30,\"children\":[1,\"Anna\",[3,\"Bob\",null]]}";

  @Test
  public void testJsonTreeWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = testBeginObject(jsonTreeWriter);
    testName(jsonTreeWriter, "name", "John");
    testValue(jsonTreeWriter, "age", 30);
    testBeginArray(jsonTreeWriter, "children");
    testValue(jsonTreeWriter, "1");
    testValue(jsonTreeWriter, "Anna");
    testBeginArray(jsonTreeWriter);
    testValue(jsonTreeWriter, "3");
    testValue(jsonTreeWriter, "Bob");
    testNullValue(jsonTreeWriter);
    testEndArray(jsonTreeWriter);
    testEndArray(jsonTreeWriter);
    testEndObject(jsonTreeWriter);
    assertEquals(TEST_JSON, jsonTreeWriter.get().toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionEndArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testLenientJsonTreeWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(true);

    JsonElement jsonElement = testBeginObject(jsonTreeWriter);
    testName(jsonTreeWriter, "name", "John");
    testValue(jsonTreeWriter, "age", Double.POSITIVE_INFINITY);
    testBeginArray(jsonTreeWriter, "children");
    testValue(jsonTreeWriter, "1");
    testValue(jsonTreeWriter, "Anna");
    testBeginArray(jsonTreeWriter);
    testValue(jsonTreeWriter, "3");
    testValue(jsonTreeWriter, "Bob");
    testNullValue(jsonTreeWriter);
    testEndArray(jsonTreeWriter);
    testEndArray(jsonTreeWriter);
    testEndObject(jsonTreeWriter);

    assertEquals(TEST_JSON, jsonTreeWriter.get().toString());
  }

  @Test
  public void testJsonTreeWriterWithNullProduct() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = testBeginObject(jsonTreeWriter);
    testBeginArray(jsonTreeWriter, "children");
    testEndArray(jsonTreeWriter);
    testEndObject(jsonTreeWriter);
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testJsonTreeWriterWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.get();
    assertSame(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

  @Test
  public void testJsonTreeWriterWithClosedWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    assertFalse(jsonTreeWriter.canWrite());
  }

  @Test
  public void testJsonTreeWriterWithClosedWriterGet() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    assertSame(JsonPrimitive.get("closed"), jsonTreeWriter.get());
  }

  private JsonElement testBeginObject(JsonTreeWriter jsonTreeWriter) throws IOException {
    jsonTreeWriter.beginObject();
    return jsonTreeWriter.peek();
  }

  private void testName(JsonTreeWriter jsonTreeWriter, String name, String value) throws IOException {
    jsonTreeWriter.name(name);
    testValue(jsonTreeWriter, name, value);
  }

  private void testValue(JsonTreeWriter jsonTreeWriter, String name, int value) throws IOException {
    jsonTreeWriter.value(value);
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
    assertEquals(value, jsonPrimitive.getAsInt());
  }

  private void testValue(JsonTreeWriter jsonTreeWriter, String name, double value) throws IOException {
    jsonTreeWriter.value(value);
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
    assertEquals(value, jsonPrimitive.getAsDouble(), 0.0);
  }

  private void testValue(JsonTreeWriter jsonTreeWriter, String name, long value) throws IOException {
    jsonTreeWriter.value(value);
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
    assertEquals(value, jsonPrimitive.getAsLong());
  }

  private void testValue(JsonTreeWriter jsonTreeWriter, String name, boolean value) throws IOException {
    jsonTreeWriter.value(value);
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
    assertEquals(value, jsonPrimitive.getAsBoolean());
  }

  private void testValue(JsonTreeWriter jsonTreeWriter, String name, Number value) throws IOException {
    jsonTreeWriter.value(value);
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
    assertEquals(value.toString(), jsonPrimitive.getAsString());
  }

  private void testNullValue(JsonTreeWriter jsonTreeWriter) throws IOException {
    jsonTreeWriter.nullValue();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertEquals(JsonNull.INSTANCE.getClass(), jsonElement.getClass());
  }

  private void testBeginArray(JsonTreeWriter jsonTreeWriter) throws IOException {
    jsonTreeWriter.beginArray();
  }

  private void testBeginArray(JsonTreeWriter jsonTreeWriter, String name) throws IOException {
    jsonTreeWriter.name(name);
    jsonTreeWriter.beginArray();
  }

  private void testEndArray(JsonTreeWriter jsonTreeWriter) throws IOException {
    jsonTreeWriter.endArray();
  }

  private void testEndObject(JsonTreeWriter jsonTreeWriter) throws IOException {
    jsonTreeWriter.endObject();
  }

  @DoNotInstrument
  private void writeToStream(JsonTreeWriter jsonTreeWriter, Writer writer) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
    jsonTreeWriter.setWriter(outputWriter);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("children").beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value("Anna");
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(3);
    jsonTreeWriter.value("Bob");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();
    jsonTreeWriter.flush();

    String actualJson = outputStream.toString();
    String expectedJson = "{\"name\":\"John\",\"age\":30,\"children\":[1,\"Anna\",[3,\"Bob\",null]]}";
    assertEquals(expectedJson, actualJson);
  }

  @Test
  public void testJsonTreeWriterWriteToStream() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    Writer writer = new OutputStreamWriter(System.out);
    jsonTreeWriter.setWriter(writer);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("children").beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value("Anna");
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(3);
    jsonTreeWriter.value("Bob");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    jsonTreeWriter.flush();
  }

  @Test
  public void testJsonTreeWriterWriteToStreamWithLenient() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(true);
    Writer writer = new OutputStreamWriter(System.out);
    jsonTreeWriter.setWriter(writer);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.name("age").value(Double.POSITIVE_INFINITY);
    jsonTreeWriter.name("children").beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value("Anna");
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(3);
    jsonTreeWriter.value("Bob");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    jsonTreeWriter.flush();
  }

  @Test
  public void testJsonTreeWriterWithFeature() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(System.out), new GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setFeatures(new Feature[] {JsonWriter.Feature.WRITE_NULL_JSON_NULLS, Feature.QUOTE_NAMED_PLACEHOLDERS})
        .createWriter(new OutputStreamWriter(System.out)));

    JsonTreeWriter jsonTreeWriterWithFeature = new JsonTreeWriter();
    jsonTreeWriterWithFeature.setWriter(jsonWriter);

    jsonTreeWriterWithFeature.beginObject();
    jsonTreeWriterWithFeature.name("name").value("John");
    jsonTreeWriterWithFeature.name("age").value(Double.POSITIVE_INFINITY);
    jsonTreeWriterWithFeature.name("children").beginArray();
    jsonTreeWriterWithFeature.value(1);
    jsonTreeWriterWithFeature.value("Anna");
    jsonTreeWriterWithFeature.beginArray();
    jsonTreeWriterWithFeature.value(3);
    jsonTreeWriterWithFeature.value("Bob");
    jsonTreeWriterWithFeature.nullValue();
    jsonTreeWriterWithFeature.endArray();
    jsonTreeWriterWithFeature.endArray();
    jsonTreeWriterWithFeature.endObject();

    jsonTreeWriterWithFeature.flush();
  }
}