package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {
    @Test
    public void testBeginArray() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        assertEquals(JsonArray.class, writer.peek().getClass());
    }

    @Test
    public void testEndArray() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenEmpty() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenNotArray() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endArray();
    }

    @Test
    public void testBeginObject() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        assertEquals(JsonObject.class, writer.peek().getClass());
    }

    @Test
    public void testEndObject() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenEmpty() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenNotObject() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endObject();
    }

    @Test
    public void testName() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        assertEquals("name", writer.pendingName);
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenEmpty() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenNotObject() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.name("name");
    }

    @Test
    public void testValueString() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("value");
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        assertEquals("value", writer.peek().getAsString());
    }

    @Test
    public void testValueBoolean() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        assertTrue(writer.peek().getAsBoolean());
    }

    @Test
    public void testValueDouble() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1.5);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        assertEquals(1.5, writer.peek().getAsDouble(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithNaN() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithInfinite() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testValueLong() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(100L);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        assertEquals(100L, writer.peek().getAsLong());
    }

    @Test
    public void testValueNull() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        assertEquals(JsonNull.class, writer.peek().getClass());
    }

    @Test
    public void testGet() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("value");
        writer.endObject();
        assertEquals(JsonObject.class, writer.get().getClass());
        assertEquals("value", writer.get().getAsJsonObject().get("name").getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWhenNotEmpty() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.get();
    }

    @Test
    public void testFlush() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.flush();
        // No assertion, just testing that it doesn't throw an exception
    }

    @Test(expected = IOException.class)
    public void testCloseWhenNotEmpty() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.close();
    }

    @Test
    public void testClose() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        assertEquals(1, writer.stack.size()); // The SENTINEL_CLOSED element is added to the stack
    }

    @Test
    public void testCloseTwice() throws Exception {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        writer.close();
        assertEquals(1, writer.stack.size()); // The SENTINEL_CLOSED element is added to the stack only once
    }
}