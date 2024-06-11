
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key");
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertTrue(jsonObject.has("key"));
    }

    @Test(expected = IllegalStateException.class)
    public void testValueWithIllegalState() {
        jsonTreeWriter.value("value");
    }

    @Test
    public void testValue() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value("value");
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
        assertEquals("value", jsonPrimitive.getAsString());
    }

    @Test
    public void testNullValue() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.nullValue();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0) instanceof JsonNull);
    }

    @Test
    public void testValueBoolean() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(true);
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        assertTrue(jsonArray.get(0).getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(3.14);
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        assertEquals(3.14, jsonArray.get(0).getAsDouble(), 0.00001);
    }

    @Test
    public void testValueLong() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(123456789L);
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        assertEquals(123456789L, jsonArray.get(0).getAsLong());
    }

    @Test
    public void testValueNumber() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(3.14);
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.size());
        assertEquals(3.14, jsonArray.get(0).getAsDouble(), 0.00001);
    }

    @Test
    public void testFlush() throws IOException {
        // No test case for this method as it does not have any logic to test
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        jsonTreeWriter.close();
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
        
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonNull);
    }
}
