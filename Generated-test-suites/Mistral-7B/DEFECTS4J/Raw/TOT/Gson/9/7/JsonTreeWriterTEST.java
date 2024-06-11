package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonWriter;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterArray() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();

    JsonElement jsonElement = writer.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(2, jsonArray.size());
    assertEquals("element1", jsonArray.get(0).getAsString());
    assertEquals("element2", jsonArray.get(1).getAsString());

    Gson gson = new Gson();
    String jsonString = gson.toJson(jsonArray);
    assertEquals("[\n  \"element1\",\n  \"element2\"\n]", jsonString);
  }

  @Test
  public void testJsonTreeWriterObject() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value("value1");
    writer.name("name2").value("value2");
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(2, jsonObject.size());
    assertEquals("name1", jsonObject.keySet().iterator().next());
    assertEquals("value1", jsonObject.get("name1").getAsString());
    assertEquals("name2", jsonObject.keySet().iterator().next());
    assertEquals("value2", jsonObject.get("name2").getAsString());

    Gson gson = new Gson();
    String jsonString = gson.toJson(jsonObject);
    assertEquals("{\n  \"name1\": \"value1\",\n  \"name2\": \"value2\"\n}", jsonString);
  }

  @Test
  public void testJsonTreeWriterNestedObjects() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").beginObject();
    writer.name("name2").value("value1");
    writer.endObject();
    writer.name("name3").beginObject();
    writer.name("name4").value("value2");
    writer.endObject();
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonObject obj1 = jsonObject.getAsJsonObject().get("name1");
    JsonObject obj2 = jsonObject.getAsJsonObject().get("name3");

    assertEquals("name2", obj1.keySet().iterator().next());
    assertEquals("value1", obj1.get("name2").getAsString());

    assertEquals("name4", obj2.keySet().iterator().next());
    assertEquals("value2", obj2.get("name4").getAsString());

    Gson gson = new Gson();
    String jsonString = gson.toJson(jsonObject);
    assertEquals("{\n  \"name1\": {\n    \"name2\": \"value1\"\n  },\n  \"name3\": {\n    \"name4\": \"value2\"\n  }\n}", jsonString);
  }

  @Test
  public void testJsonTreeWriterNullValues() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value(null);
    writer.name("name2").value(null);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(2, jsonObject.size());
    assertNotNull(jsonObject.get("name1"));
    assertEquals(JsonNull.INSTANCE, jsonObject.get("name1"));
    assertNotNull(jsonObject.get("name2"));
    assertEquals(JsonNull.INSTANCE, jsonObject.get("name2"));

    Gson gson = new Gson();
    String jsonString = gson.toJson(jsonObject);
    assertEquals("{\n  \"name1\": null,\n  \"name2\": null\n}", jsonString);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonTreeWriterInvalidWriteOrder() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value("value1");
    writer.endObject();
    writer.value("invalid");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterStackUnderflow() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.endArray();
    writer.value("invalid");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNullWriter() throws IOException {
    JsonWriter writer = new JsonTreeWriter();
    writer.beginArray();
    Writer tempWriter = new OutputStreamWriter(new ByteArrayOutputStream());
    writer.setWriter(tempWriter);
    writer.value("invalid");
    writer.close();
  }
}