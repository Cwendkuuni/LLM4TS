
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import org.junit.Test;

public class JsonTreeWriterTest {
    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        JsonNull nullValue = JsonNull.INSTANCE;
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        JsonPrimitive primitive = new JsonPrimitive(10);
        
        writer.put(nullValue);
        writer.put(obj);
        writer.put(array);
        writer.put(primitive);
        
        assertEquals(nullValue, writer.get());
        
        writer.beginArray();
        writer.endArray();
        
        assertEquals(obj, writer.get());
        
        writer.beginObject();
        writer.endObject();
        
        assertEquals(array, writer.get());
        
        writer.value("test");
        
        assertEquals(primitive, writer.get());
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        
        JsonElement element = writer.peek();
        assertTrue(element instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.endArray();
        
        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithPendingName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("name");
        writer.beginArray();
        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithEmptyStack() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithNonArrayOnTop() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.endArray();
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        
        JsonElement element = writer.peek();
        assertTrue(element instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.endObject();
        
        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithPendingName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("name");
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithEmptyStack() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithNonObjectOnTop() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.endObject();
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("name");
        
        assertEquals("name", writer.pendingName);
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithEmptyStack() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithNonObjectOnTop() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.name("name");
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value("test");
        
        assertTrue(writer.peek() instanceof JsonPrimitive);
        assertEquals("test", writer.peek().getAsString());
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.nullValue();
        
        assertTrue(writer.peek() instanceof JsonNull);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(true);
        
        assertTrue(writer.peek() instanceof JsonPrimitive);
        assertEquals(true, writer.peek().getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(10.5);
        
        assertTrue(writer.peek() instanceof JsonPrimitive);
        assertEquals(10.5, writer.peek().getAsDouble(), 0.001);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(1000L);
        
        assertTrue(writer.peek() instanceof JsonPrimitive);
        assertEquals(1000L, writer.peek().getAsLong());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(100);
        
        assertTrue(writer.peek() instanceof JsonPrimitive);
        assertEquals(100, writer.peek().getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.flush();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNonEmptyStack() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.close();
    }

    @Test
    public void testCloseWithEmptyStack() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.close();
        
        assertTrue(writer.stack.contains(JsonTreeWriter.SENTINEL_CLOSED));
    }
}
