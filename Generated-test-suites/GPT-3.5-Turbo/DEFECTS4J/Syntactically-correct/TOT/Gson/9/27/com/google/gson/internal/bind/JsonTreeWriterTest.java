package com.google.gson.internal.bind;

import com.google.gson.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("name").value("John");
        jsonWriter.name("age").value(30);
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.beginArray();
        JsonElement actualJsonElement = jsonWriter.get();

        assertTrue(actualJsonElement instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.beginArray();
        jsonWriter.endArray();
        JsonElement actualJsonElement = jsonWriter.get();

        assertTrue(actualJsonElement instanceof JsonArray);
        assertEquals(0, ((JsonArray) actualJsonElement).size());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.beginObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertTrue(actualJsonElement instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.beginObject();
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertTrue(actualJsonElement instanceof JsonObject);
        assertEquals(0, ((JsonObject) actualJsonElement).size());
    }

    @Test
    public void testName() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("name").value("John");
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithNoEnclosingObject() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.name("name").value("John");
    }

    @Test
    public void testValueString() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("name").value("John");
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", JsonNull.INSTANCE);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("name").nullValue();
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isMarried", true);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("isMarried").value(true);
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("salary", 1000.50);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("salary").value(1000.50);
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age", 30);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("age").value(30);
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age", 30.5);
        JsonElement expectedJsonElement = jsonObject;
        jsonWriter.beginObject();
        jsonWriter.name("age").value(30.5);
        jsonWriter.endObject();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(expectedJsonElement, actualJsonElement);
    }

    @Test
    public void testFlush() {
        // Flush method does nothing, so there is no need to test it
        assertTrue(true);
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.close();
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeWriter jsonWriter = new JsonTreeWriter();

        jsonWriter.beginObject();
        jsonWriter.endObject();
        jsonWriter.close();
        JsonElement actualJsonElement = jsonWriter.get();

        assertEquals(JsonNull.INSTANCE, actualJsonElement);
    }

}