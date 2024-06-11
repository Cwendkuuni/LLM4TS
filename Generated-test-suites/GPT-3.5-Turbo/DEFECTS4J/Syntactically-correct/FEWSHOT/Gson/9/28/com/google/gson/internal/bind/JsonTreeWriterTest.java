
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class JsonTreeWriterTest {
    private JsonTreeWriter writer;
    
    @Before
    public void setUp() {
        writer = new JsonTreeWriter();
    }
    
    @Test
    public void testBeginArray() {
        writer.beginArray();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertTrue(jsonArray.isEmpty());
    }
    
    @Test
    public void testEndArray() {
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertTrue(jsonArray.isEmpty());
    }
    
    @Test
    public void testBeginObject() {
        writer.beginObject();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) element;
        assertTrue(jsonObject.entrySet().isEmpty());
    }
    
    @Test
    public void testEndObject() {
        writer.beginObject();
        writer.endObject();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) element;
        assertTrue(jsonObject.entrySet().isEmpty());
    }
    
    @Test
    public void testName() {
        writer.beginObject();
        writer.name("key");
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) element;
        assertTrue(jsonObject.has("key"));
    }
    
    @Test
    public void testValueString() {
        writer.value("value");
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals("value", jsonArray.get(0).getAsString());
    }
    
    @Test
    public void testValueNull() {
        writer.nullValue();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonNull());
        assertEquals(JsonNull.INSTANCE, jsonArray.get(0));
    }
    
    @Test
    public void testValueBoolean() {
        writer.value(true);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals(true, jsonArray.get(0).getAsBoolean());
    }
    
    @Test
    public void testValueDouble() {
        writer.value(3.14);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals(3.14, jsonArray.get(0).getAsDouble(), 0.0001);
    }
    
    @Test
    public void testValueLong() {
        writer.value(100L);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals(100L, jsonArray.get(0).getAsLong());
    }
    
    @Test
    public void testValueNumber() {
        writer.value(42);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals(42, jsonArray.get(0).getAsInt());
    }
    
    @Test
    public void testFlush() {
        // The flush() method is a no-op in JsonTreeWriter
        // Cannot test its behavior
    }
    
    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        writer.beginArray();
        writer.close();
    }
    
    @Test
    public void testClose() throws IOException {
        writer.close();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).isJsonPrimitive());
        assertEquals("closed", jsonArray.get(0).getAsString());
    }
}
