package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testEmptyWriter() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertNull(element);
    }

    @Test
    public void testBeginAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testBeginAndEndArrayWithValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        JsonElement element = writer.get();
        JsonArray jsonArray = (JsonArray) element;
        assertEquals(1, jsonArray.size());
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonArray.get(0);
        assertEquals("foo", jsonPrimitive.getAsString());
    }

    @Test
    public void testBeginAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("John Doe");
        writer.endObject();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertEquals(1, jsonObject.size());
        JsonPrimitive namePrimitive = (JsonPrimitive) jsonObject.get("name");
        assertEquals("John Doe", namePrimitive.getAsString());
    }

    @Test
    public void testBeginAndEndObjectWithNestedArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("John Doe");
        writer.name("age").beginArray();
        writer.value(25);
        writer.value(30);
        writer.endArray();
        writer.endObject();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertEquals(3, jsonObject.size());
        JsonPrimitive namePrimitive = (JsonPrimitive) jsonObject.get("name");
        assertEquals("John Doe", namePrimitive.getAsString());
        JsonArray ageArray = (JsonArray) jsonObject.get("age");
        assertEquals(2, ageArray.size());
        JsonPrimitive agePrimitive1 = (JsonPrimitive) ageArray.get(0);
        assertEquals(25, agePrimitive1.getAsInt());
        JsonPrimitive agePrimitive2 = (JsonPrimitive) ageArray.get(1);
        assertEquals(30, agePrimitive2.getAsInt());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("John Doe");
        writer.endObject();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertEquals(1, jsonObject.size());
        JsonPrimitive namePrimitive = (JsonPrimitive) jsonObject.get("name");
        assertEquals("John Doe", namePrimitive.getAsString());
    }

    @Test
    public void testNameWithExistingObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("John Doe");
        writer.name("name").beginArray();
        writer.value("Alice");
        writer.value("Bob");
        writer.endArray();
        writer.endObject();
        JsonElement element = writer.get();
        JsonObject jsonObject = (JsonObject) element;
        assertEquals(2, jsonObject.size());
        JsonPrimitive namePrimitive1 = (JsonPrimitive) jsonObject.get("name");
        assertEquals("John Doe", namePrimitive1.getAsString());
        JsonArray nameArray = (JsonArray) jsonObject.get("name");
        assertEquals(2, nameArray.size());
        JsonPrimitive namePrimitive2 = (JsonPrimitive) nameArray.get(0);
        assertEquals("Alice", namePrimitive2.getAsString());
        JsonPrimitive namePrimitive3 = (JsonPrimitive) nameArray.get(1);
        assertEquals("Bob", namePrimitive3.getAsString());
    }

    @Test
    public void testDupName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("John Doe");
        writer.name("name").beginArray();
        writer.value("Alice");
        writer.value("Bob");
        writer.endArray();
        assertThrows(IllegalStateException.class, () -> {
            writer.name("name").value("David");
        });
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(null);
        JsonElement element = writer.get();
        JsonNull jsonNull = (JsonNull) element;
        assertSame(JsonNull.INSTANCE, jsonNull);
    }

    @Test
    public void testValueBooleanTrue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isBoolean());
        assertEquals(true, jsonPrimitive.getAsBoolean());
    }

    @Test
    public void testValueBooleanFalse() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(false);
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isBoolean());
        assertEquals(false, jsonPrimitive.getAsBoolean());
    }

    @Test
    public void testValueNumberInt() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(123);
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isNumber());
        assertEquals(123, jsonPrimitive.getAsInt());
    }

    @Test
    public void testValueNumberLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Long.MAX_VALUE);
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isNumber());
        assertEquals(Long.MAX_VALUE, jsonPrimitive.getAsLong());
    }

    @Test
    public void testValueNumberDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.NaN);
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isNumber());
        assertEquals(Double.NaN, jsonPrimitive.getAsDouble());
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("foo");
        JsonElement element = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertEquals(true, jsonPrimitive.isString());
        assertEquals("foo", jsonPrimitive.getAsString());
    }
}