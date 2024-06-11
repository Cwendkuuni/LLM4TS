package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonWriter.OutputType;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_empty() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    assertNull(result.getAsJsonObject().entrySet().iterator().next());
  }

  @Test
  public void testJsonTreeWriter_string() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("name", jsonObject.entrySet().iterator().next().getKey());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test
  public void testJsonTreeWriter_boolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("isMale").value(true);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.get("isMale").isJsonPrimitive());
    assertEquals(JsonPrimitive.class, jsonObject.get("isMale").getClass());
    assertTrue(jsonObject.get("isMale").isBoolean());
    assertEquals(true, jsonObject.get("isMale").getAsBoolean());
  }

  @Test
  public void testJsonTreeWriter_number() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("age").value(42);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.get("age").isJsonPrimitive());
    assertEquals(JsonPrimitive.class, jsonObject.get("age").getClass());
    assertTrue(jsonObject.get("age").isNumber());
    assertEquals(42, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testJsonTreeWriter_array() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(3, jsonArray.size());
    List<Integer> expectedList = Arrays.asList(1, 2, 3);
    assertEquals(expectedList, jsonArrayToList(jsonArray));
  }

  @Test
  public void testJsonTreeWriter_array_empty() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(0, jsonArray.size());
  }

  @Test
  public void testJsonTreeWriter_array_null() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.nullValue();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(1, jsonArray.size());
    JsonNull jsonNull = jsonArray.get(0).getAsJsonNull();
    assertSame(JsonNull.class, jsonNull.getClass());
  }

  @Test
  public void testJsonTreeWriter_object() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(42);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("name", jsonObject.entrySet().iterator().next().getKey());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals("age", jsonObject.entrySet().iterator().next().getKey());
    assertEquals(42, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testJsonTreeWriter_object_empty() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals(0, jsonObject.entrySet().size());
  }

  @Test
  public void testJsonTreeWriter_object_null() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").nullValue();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals(0, jsonObject.entrySet().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_beginArrayAfterBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_beginObjectAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endObjectBeforeBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endArrayBeforeBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_nameAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_valueAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("John Doe");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_valueAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.value("John Doe");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_closeWriterTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.close();
  }

  @Test
  public void testJsonTreeWriter_writeToString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(42);
    writer.endObject();
    writer.setOutputType(OutputType.STRING);
    writer.writeTo(new OutputStreamWriter(new ByteArrayOutputStream()));
    writer.flush();
    String jsonString = writer.get().getAsString();
    assertEquals("{\"name\":\"John Doe\",\"age\":42}", jsonString);
  }

  @Test
  public void testJsonTreeWriter_writeToJsonString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(42);
    writer.endObject();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonString = gson.toJson(writer.get());
    assertEquals("{\n  \"name\": \"John Doe\",\n  \"age\": 42\n}", jsonString);
  }

  @Test
  public void testJsonTreeWriter_writeToJsonElement() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(42);
    writer.endObject();
    JsonElement jsonElement = JsonParser.parseString("{}");
    writer.setOutputType(OutputType.JSON);
    writer.writeTo(jsonElement);
    writer.flush();
    assertEquals(writer.get().getAsJsonObject(), jsonElement);
  }

  @Test
  public void testJsonTreeWriter_writeToJsonArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    JsonElement jsonElement = JsonParser.parseString("[]");
    writer.setOutputType(OutputType.JSON);
    writer.writeTo(jsonElement);
    writer.flush();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(writer.get().getAsJsonArray(), jsonArray);
  }

  @Test
  public void testJsonTreeWriter_writeToOutputStream() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(42);
    writer.endObject();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    writer.writeTo(new OutputStreamWriter(byteArrayOutputStream));
    writer.flush();
    String jsonString = new String(byteArrayOutputStream.toByteArray());
    assertEquals("{\"name\":\"John Doe\",\"age\":42}", jsonString);
  }

  private List<T> jsonArrayToList(JsonArray jsonArray) {
    List<T> list = Collections.emptyList();
    if (jsonArray != null) {
      list = new ArrayList<T>(jsonArray.size());
      for (int i = 0; i < jsonArray.size(); i++) {
        JsonElement jsonElement = jsonArray.get(i);
        T value = JsonParser.parse(jsonElement.getAsString()).getAs(clazzOf(jsonElement.getClass()));
        list.add(value);
      }
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  private static <T> Class<T> clazzOf(Class<?> clazz) {
    return (Class<T>) clazz;
  }
}