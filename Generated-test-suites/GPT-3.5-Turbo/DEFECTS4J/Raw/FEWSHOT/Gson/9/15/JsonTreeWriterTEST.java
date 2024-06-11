package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element1 = writer.get();
        assertNotNull(element1);
        assertTrue(element1 instanceof JsonNull);

        writer.beginObject();
        writer.name("key1").value("value1");
        writer.endObject();
        JsonElement element2 = writer.get();
        assertNotNull(element2);
        assertTrue(element2 instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) element2;
        assertEquals("value1", jsonObject.get("key1").getAsString());
    }

    @Test
    public void testBeginArray() {

    }

    @Test
    public void testEndArray() {

    }

    @Test
    public void testBeginObject() {

    }

    @Test
    public void testEndObject() {

    }

    @Test
    public void testName() {

    }

    @Test
    public void testValueString() {

    }

    @Test
    public void testNullValue() {

    }

    @Test
    public void testValueBoolean() {

    }

    @Test
    public void testValueDouble() {

    }

    @Test
    public void testValueLong() {

    }

    @Test
    public void testValueNumber() {

    }

    @Test
    public void testFlush() {

    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        writer.beginObject(); // expect IOException
    }
}