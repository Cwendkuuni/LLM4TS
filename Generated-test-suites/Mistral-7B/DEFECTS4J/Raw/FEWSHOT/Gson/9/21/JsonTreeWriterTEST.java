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
    public void testGetEmpty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertNull(element);
    }

    @Test
    public void testGetArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        assertNotNull(element);
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testGetObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertNotNull(element);
        assertEquals(0, jsonObject.size());
    }

    @Test
    public void testGetProduct() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("foo");
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testBeginArrayTwice() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginArray();
        writer.close();
        writer.close();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testBeginObjectTwice() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.beginObject();
        writer.close();
        writer.close();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertEquals(0, jsonObject.size());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("value");
        writer.endObject();
        writer.close();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertNotNull(element);
        assertSame("name", jsonObject.keySet().iterator().next().getAsString());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameAfterEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        writer.name("name");
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("foo");
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals(true, jsonPrimitive.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1.0);
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals(1.0, jsonPrimitive.getAsDouble(), 0.0);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1L);
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals(1L, jsonPrimitive.getAsLong());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(new Integer(1));
        writer.close();
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertNotNull(element);
        assertEquals(1, jsonPrimitive.getAsInt());
    }

    @Test
    public void testBeginArrayAddValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        writer.close();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        assertNotNull(element);
        assertEquals(1, jsonArray.size());
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonArray.get(0);
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testBeginArrayAddObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginObject();
        writer.name("name");
        writer.value("value");
        writer.endObject();
        writer.endArray();
        writer.close();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        assertEquals(1, jsonArray.size());
        assertEquals(1, jsonObject.size());
        assertSame("name", jsonObject.keySet().iterator().next().getAsString());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void testBeginObjectAddNameValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("value");
        writer.endObject();
        writer.close();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertNotNull(element);
        assertEquals(1, jsonObject.size());
        assertSame("name", jsonObject.keySet().iterator().next().getAsString());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void testBeginObjectAddArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.beginArray();
        writer.value("foo");
        writer.value("bar");
        writer.endArray();
        writer.endObject();
        writer.close();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertNotNull(element);
        assertEquals(1, jsonObject.size());
        JsonArray jsonArray = (JsonArray) jsonObject.get("name");
        assertEquals(2, jsonArray.size());
        JsonPrimitive jsonPrimitive1 = (JsonPrimitive) jsonArray.get(0);
        assertEquals("foo", jsonPrimitive1.getAsString());
        JsonPrimitive jsonPrimitive2 = (JsonPrimitive) jsonArray.get(1);
        assertEquals("bar", jsonPrimitive2.getAsString());
    }

    @Test
    public void testBeginObjectAddObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.beginObject();
        writer.name("innerName");
        writer.value("innerValue");
        writer.endObject();
        writer.endObject();
        writer.close();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertNotNull(element);
        assertEquals(1, jsonObject.size());
        JsonObject innerJsonObject = (JsonObject) jsonObject.get("name");
        assertNotNull(innerJsonObject);
        assertEquals(1, innerJsonObject.size());
        assertSame("innerName", innerJsonObject.keySet().iterator().next().getAsString());
        assertEquals("innerValue", innerJsonObject.get("innerName").getAsString());
    }
}