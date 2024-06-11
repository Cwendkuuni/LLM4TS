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
    public void testBeginArrayEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement root = writer.get();
        assertTrue(root.isJsonArray());
        JsonArray jsonArray = (JsonArray) root;
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testBeginArrayAddValueEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        JsonElement root = writer.get();
        assertTrue(root.isJsonArray());
        JsonArray jsonArray = (JsonArray) root;
        assertEquals(1, jsonArray.size());
        JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testBeginArrayAddMultipleValuesEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.value("bar");
        writer.value(123);
        writer.endArray();
        JsonElement root = writer.get();
        assertTrue(root.isJsonArray());
        JsonArray jsonArray = (JsonArray) root;
        assertEquals(3, jsonArray.size());
        JsonPrimitive jsonPrimitive1 = jsonArray.get(0).getAsJsonPrimitive();
        assertEquals("foo", jsonPrimitive1.getAsString());
        JsonPrimitive jsonPrimitive2 = jsonArray.get(1).getAsJsonPrimitive();
        assertEquals("bar", jsonPrimitive2.getAsString());
        JsonPrimitive jsonPrimitive3 = jsonArray.get(2).getAsJsonPrimitive();
        assertEquals(123, jsonPrimitive3.getAsLong());
    }

    @Test
    public void testBeginObjectEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement root = writer.get();
        assertTrue(root.isJsonObject());
        JsonObject jsonObject = (JsonObject) root;
        assertEquals(0, jsonObject.size());
    }

    @Test
    public void testBeginObjectAddNameValueEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("foo");
        writer.endObject();
        JsonElement root = writer.get();
        assertTrue(root.isJsonObject());
        JsonObject jsonObject = (JsonObject) root;
        assertEquals(1, jsonObject.size());
        JsonPrimitive jsonPrimitive = jsonObject.get("name").getAsJsonPrimitive();
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testBeginObjectAddMultipleNameValueEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name1");
        writer.value("foo1");
        writer.name("name2");
        writer.value("foo2");
        writer.endObject();
        JsonElement root = writer.get();
        assertTrue(root.isJsonObject());
        JsonObject jsonObject = (JsonObject) root;
        assertEquals(2, jsonObject.size());
        JsonPrimitive jsonPrimitive1 = jsonObject.get("name1").getAsJsonPrimitive();
        assertEquals("foo1", jsonPrimitive1.getAsString());
        JsonPrimitive jsonPrimitive2 = jsonObject.get("name2").getAsJsonPrimitive();
        assertEquals("foo2", jsonPrimitive2.getAsString());
    }
}