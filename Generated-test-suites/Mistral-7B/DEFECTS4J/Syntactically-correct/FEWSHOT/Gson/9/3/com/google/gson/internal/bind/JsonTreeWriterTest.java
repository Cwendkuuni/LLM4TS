package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArrayAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonArray jsonArray = writer.get();
        writer.endArray();
        assertEquals(1, jsonArray.size());
        assertNull(writer.product);
    }

    @Test
    public void testBeginArrayAndAddValueAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        JsonArray jsonArray = writer.get();
        writer.endArray();
        assertEquals(1, jsonArray.size());
        assertEquals("foo", jsonArray.get(0).getAsString());
        assertNull(writer.product);
    }

    @Test
    public void testBeginArrayAndAddMultipleValuesAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.value("bar");
        writer.value("baz");
        JsonArray jsonArray = writer.get();
        writer.endArray();
        assertEquals(3, jsonArray.size());
        assertEquals("foo", jsonArray.get(0).getAsString());
        assertEquals("bar", jsonArray.get(1).getAsString());
        assertEquals("baz", jsonArray.get(2).getAsString());
        assertNull(writer.product);
    }

    @Test
    public void testBeginObjectAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonObject jsonObject = writer.get();
        writer.endObject();
        assertEquals(0, jsonObject.size());
        assertNull(writer.product);
    }

    @Test
    public void testBeginObjectAndAddKeyValuePairAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value("value");
        JsonObject jsonObject = writer.get();
        writer.endObject();
        assertEquals(1, jsonObject.size());
        assertTrue(jsonObject.has("key"));
        assertEquals("value", jsonObject.get("key").getAsString());
        assertNull(writer.product);
    }

    @Test
    public void testBeginObjectAndAddMultipleKeyValuePairsAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key1");
        writer.value("value1");
        writer.name("key2");
        writer.value("value2");
        JsonObject jsonObject = writer.get();
        writer.endObject();
        assertEquals(2, jsonObject.size());
        assertTrue(jsonObject.has("key1"));
        assertEquals("value1", jsonObject.get("key1").getAsString());
        assertTrue(jsonObject.has("key2"));
        assertEquals("value2", jsonObject.get("key2").getAsString());
        assertNull(writer.product);
    }

    @Test
    public void testCloseWriter() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        assertNotNull(writer.product);
        assertEquals(JsonPrimitive.class, writer.product.getClass());
        assertEquals("closed", writer.product.getAsString());
    }
}