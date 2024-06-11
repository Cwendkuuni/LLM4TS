
package com.google.gson.internal.bind;

import org.junit.Test;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginArray();
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.isJsonArray());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.isJsonArray());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.beginObject();
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.isJsonObject());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.isJsonObject());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("name"));
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value("value");
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("value"));
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.nullValue();
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("null"));
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(true);
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("true"));
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(1.23);
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("1.23"));
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(123);
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("123"));
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(1.23);
        JsonObject jsonObj = (JsonObject) writer.get();
        assertTrue(jsonObj.has("1.23"));
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.flush();
        // No assertion as flush does not modify the JSON structure
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
        JsonObject jsonObj = (JsonObject) writer.get();
        // Assertion not possible as the writer should throw an IOException after closing
    }
}
