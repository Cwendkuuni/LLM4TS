
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {
    private JsonTreeWriter jsonTreeWriter;
    private StringWriter stringWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
        stringWriter = new StringWriter();
        jsonTreeWriter = new JsonTreeWriter(stringWriter);
    }

    @After
    public void tearDown() throws IOException {
        stringWriter.close();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonArray.class, result.getClass());
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonNull.class, result.getClass());
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonObject.class, result.getClass());
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonNull.class, result.getClass());
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("test");
        JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
        assertFalse(jsonObject.isJsonNull());
        assertEquals(1, jsonObject.entrySet().size());
        assertEquals("test", jsonObject.entrySet().iterator().next().getKey());
    }

    @Test
    public void testValueString() {
        jsonTreeWriter.value("test");
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals("test", result.getAsString());
    }

    @Test
    public void testValueBoolean() {
        jsonTreeWriter.value(true);
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        jsonTreeWriter.value(3.14);
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(3.14, result.getAsDouble(), 0);
    }

    @Test
    public void testValueLong() {
        jsonTreeWriter.value(123L);
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(123L, result.getAsLong());
    }

    @Test
    public void testValueNumber() {
        jsonTreeWriter.value(123.45);
        JsonElement result = jsonTreeWriter.get();
        assertFalse(result.isJsonNull());
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(123.45, result.getAsNumber().doubleValue(), 0);
    }

    @Test
    public void testFlush() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.flush();
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeWriter.close();
    }
}
