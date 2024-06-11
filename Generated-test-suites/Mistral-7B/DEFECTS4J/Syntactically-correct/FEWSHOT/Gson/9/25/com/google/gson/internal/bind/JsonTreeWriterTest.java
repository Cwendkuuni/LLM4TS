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
    public void testJsonTreeWriter_Empty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement result = writer.get();
        assertNull(result);
    }

    @Test
    public void testJsonTreeWriter_SingleValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("Hello");
        JsonElement result = writer.get();
        assertEquals(new JsonPrimitive("Hello"), result);
    }

    @Test
    public void testJsonTreeWriter_SingleObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("Alice");
        writer.endObject();
        JsonElement result = writer.get();
        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("Alice", jsonObject.get("name").getAsString());
    }

    @Test
    public void testJsonTreeWriter_ArrayWithSingleValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("Hello");
        writer.endArray();
        JsonElement result = writer.get();
        JsonArray jsonArray = result.getAsJsonArray();
        assertEquals(new JsonPrimitive("Hello"), jsonArray.get(0));
    }

    @Test
    public void testJsonTreeWriter_ArrayWithMultipleValues() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("Hello");
        writer.value("World");
        writer.endArray();
        JsonElement result = writer.get();
        JsonArray jsonArray = result.getAsJsonArray();
        assertEquals(2, jsonArray.size());
        assertEquals(new JsonPrimitive("Hello"), jsonArray.get(0));
        assertEquals(new JsonPrimitive("World"), jsonArray.get(1));
    }

    @Test
    public void testJsonTreeWriter_NestedObjects() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("user").beginObject();
        writer.name("name").value("Alice");
        writer.name("age").value(30);
        writer.endObject();
        writer.name("address").beginObject();
        writer.name("street").value("123 Main St");
        writer.name("city").value("Anytown");
        writer.endObject();
        writer.endObject();
        JsonElement result = writer.get();
        JsonObject jsonObject = result.getAsJsonObject();
        JsonObject userObject = jsonObject.get("user").getAsJsonObject();
        JsonObject nameObject = userObject.get("name").getAsJsonObject();
        JsonPrimitive namePrimitive = nameObject.get("$value").getAsJsonPrimitive();
        assertEquals("Alice", namePrimitive.getAsString());
        JsonObject ageObject = userObject.get("age").getAsJsonObject();
        JsonPrimitive agePrimitive = ageObject.get("$value").getAsJsonPrimitive();
        assertEquals(30, agePrimitive.getAsInt());
        JsonObject addressObject = jsonObject.get("address").getAsJsonObject();
        JsonObject streetObject = addressObject.get("street").getAsJsonObject();
        JsonPrimitive streetPrimitive = streetObject.get("$value").getAsJsonPrimitive();
        assertEquals("123 Main St", streetPrimitive.getAsString());
    }

    @Test
    public void testJsonTreeWriter_NestedArrays() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginArray();
        writer.value("Hello");
        writer.value("World");
        writer.endArray();
        writer.value("Goodbye");
        writer.endArray();
        JsonElement result = writer.get();
        JsonArray jsonArray = result.getAsJsonArray();
        JsonArray innerArray = jsonArray.get(0).getAsJsonArray();
        assertEquals(2, innerArray.size());
        assertEquals(new JsonPrimitive("Hello"), innerArray.get(0));
        assertEquals(new JsonPrimitive("World"), innerArray.get(1));
        assertEquals(new JsonPrimitive("Goodbye"), jsonArray.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_EndArrayBeforeBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endArray();
        writer.value("Hello");
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_EndObjectBeforeBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endObject();
        writer.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_WriteValueAfterEndDocument() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        writer.value("Hello");
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_WriteObjectAfterArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.close();
        writer.beginObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_WriteArrayAfterObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
        writer.beginArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_WriteArrayInObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.beginArray();
        writer.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_InvalidState_WriteObjectInArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginObject();
        writer.close();
    }
}