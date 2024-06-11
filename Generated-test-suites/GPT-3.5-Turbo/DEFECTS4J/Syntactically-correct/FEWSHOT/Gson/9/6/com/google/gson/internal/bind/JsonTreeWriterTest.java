package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {
    
    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement element = writer.get();
        assertEquals(JsonArray.class, element.getClass());
        assertEquals(1, element.getAsJsonArray().size());
    }
    
    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        assertEquals(JsonArray.class, element.getClass());
        assertEquals(0, element.getAsJsonArray().size());
    }
    
    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonElement element = writer.get();
        assertEquals(JsonObject.class, element.getClass());
        assertEquals(1, element.getAsJsonObject().entrySet().size());
    }
    
    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement element = writer.get();
        assertEquals(JsonObject.class, element.getClass());
        assertEquals(0, element.getAsJsonObject().entrySet().size());
    }
    
    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("value");
        writer.endObject();
        JsonElement element = writer.get();
        assertEquals(JsonObject.class, element.getClass());
        assertEquals("value", element.getAsJsonObject().get("name").getAsString());
    }
    
    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("value");
        JsonElement element = writer.get();
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals("value", element.getAsString());
    }
    
    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement element = writer.get();
        assertEquals(JsonNull.class, element.getClass());
    }
    
    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement element = writer.get();
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals(true, element.getAsBoolean());
    }
    
    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonElement element = writer.get();
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals(3.14, element.getAsDouble(), 0.00001);
    }
    
    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(12345L);
        JsonElement element = writer.get();
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals(12345L, element.getAsLong());
    }
    
    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(123.45);
        JsonElement element = writer.get();
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals(123.45, element.getAsDouble(), 0.00001);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIncompleteDocument() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.flush();
        writer.close();
    }
}