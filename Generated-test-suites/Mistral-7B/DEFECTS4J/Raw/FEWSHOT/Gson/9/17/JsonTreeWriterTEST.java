package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        JsonArray array = writer.get();
        assertNotNull(array);
        assertEquals(0, array.size());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonArray.class, root.getClass());
        JsonArray array = (JsonArray) root;
        assertEquals(0, array.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithoutBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endArray();
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonObject object = writer.get();
        assertNotNull(object);
        assertEquals(0, object.size());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals(0, object.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithoutBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endObject();
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value("value");
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals("value", object.get("key").getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.name("key");
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value("value");
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals("value", object.get("key").getAsString());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value(true);
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals(true, object.get("key").getAsBoolean());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value(1);
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals(1, object.get("key").getAsInt());
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.nullValue();
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertNull(object.get("key"));
    }

    @Test
    public void testValueArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("element1");
        writer.value(1);
        writer.value("element3");
        writer.endArray();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonArray.class, root.getClass());
        JsonArray array = (JsonArray) root;
        assertEquals(3, array.size());
        assertEquals("element1", array.get(0).getAsString());
        assertEquals(1, array.get(1).getAsInt());
        assertEquals("element3", array.get(2).getAsString());
    }

    @Test
    public void testValueObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key1");
        writer.value("value1");
        writer.name("key2");
        writer.value(new JsonPrimitive(2));
        writer.endObject();
        JsonElement root = writer.get();
        assertNotNull(root);
        assertEquals(JsonObject.class, root.getClass());
        JsonObject object = (JsonObject) root;
        assertEquals("value1", object.get("key1").getAsString());
        assertEquals(2, object.get("key2").getAsInt());
    }

    @Test
    public void testValueSentinalClosed() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        writer.value("value");
        writer.endObject();
        writer.close();
        JsonElement root = writer.get();
        assertSame(SENTINEL_CLOSED, root);
    }
}