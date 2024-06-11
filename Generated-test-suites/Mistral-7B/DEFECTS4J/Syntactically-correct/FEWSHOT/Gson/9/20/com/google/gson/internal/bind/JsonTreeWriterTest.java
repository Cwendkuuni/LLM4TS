package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonArray jsonArray = (JsonArray) writer.peek();
        assertEquals(0, jsonArray.size());

        writer.value("element1");
        writer.value(1);
        writer.value("element3");
        writer.endArray();

        JsonElement root = writer.get();
        JsonArray jsonArrayRoot = (JsonArray) root;
        assertEquals(3, jsonArrayRoot.size());
        assertEquals("element1", jsonArrayRoot.get(0).getAsString());
        assertEquals(1, jsonArrayRoot.get(1).getAsInt());
        assertEquals("element3", jsonArrayRoot.get(2).getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayWhenStackIsEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonObject jsonObject = (JsonObject) writer.peek();

        writer.name("key1");
        writer.value("value1");
        writer.name("key2");
        writer.value("value2");
        writer.endObject();

        JsonElement root = writer.get();
        JsonObject jsonObjectRoot = (JsonObject) root;
        assertEquals(2, jsonObjectRoot.size());
        assertEquals("value1", jsonObjectRoot.get("key1").getAsString());
        assertEquals("value2", jsonObjectRoot.get("key2").getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectWhenStackIsEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("element1");
        writer.value(1);
        writer.value("element3");
        writer.endArray();

        JsonElement root = writer.get();
        JsonArray jsonArrayRoot = (JsonArray) root;
        assertEquals(3, jsonArrayRoot.size());

        writer.endArray();

        JsonElement topElement = writer.peek();
        assertNull(topElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenStackIsEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endArray();
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key1");
        writer.value("value1");
        writer.name("key2");
        writer.value("value2");
        writer.endObject();

        JsonElement root = writer.get();
        JsonObject jsonObjectRoot = (JsonObject) root;
        assertEquals(2, jsonObjectRoot.size());

        writer.endObject();

        JsonElement topElement = writer.peek();
        assertNull(topElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenStackIsEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endObject();
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key1");
        writer.value("value1");
        writer.name("key2");
        writer.value("value2");
        writer.endObject();

        JsonElement root = writer.get();
        JsonObject jsonObjectRoot = (JsonObject) root;
        assertEquals(2, jsonObjectRoot.size());

        JsonObject jsonObject = (JsonObject) writer.peek();
        assertEquals("key2", jsonObject.name());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenStackIsEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.name("key1");
    }
}