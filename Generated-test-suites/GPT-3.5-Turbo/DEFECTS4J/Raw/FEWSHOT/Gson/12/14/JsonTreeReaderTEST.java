package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        assertEquals(JsonArray.class, reader.peekStack().getClass());
        assertEquals(0, reader.pathIndices[reader.stackSize - 1]);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        reader.endArray();
        assertNotEquals(JsonArray.class, reader.peekStack().getClass());
        assertEquals(1, reader.pathIndices[reader.stackSize - 1]);
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        assertEquals(JsonObject.class, reader.peekStack().getClass());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.endObject();
        assertNotEquals(JsonObject.class, reader.peekStack().getClass());
        assertEquals(1, reader.pathIndices[reader.stackSize - 1]);
    }

    @Test
    public void testHasNext() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        String name = reader.nextName();
        assertEquals("name", name);
        assertEquals(JsonPrimitive.class, reader.peekStack().getClass());
        reader.endObject();
    }

    @Test
    public void testNextString() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("value");

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        String value = reader.nextString();
        assertEquals("value", value);
        assertNotEquals(JsonPrimitive.class, reader.peekStack().getClass());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(true);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        boolean value = reader.nextBoolean();
        assertTrue(value);
        assertNotEquals(JsonPrimitive.class, reader.peekStack().getClass());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonNull jsonNull = JsonNull.INSTANCE;

        JsonTreeReader reader = new JsonTreeReader(jsonNull);
        reader.nextNull();
        assertNotEquals(JsonNull.class, reader.peekStack().getClass());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(3.14159);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        double value = reader.nextDouble();
        assertEquals(3.14159, value, 0.00001);
        assertNotEquals(JsonPrimitive.class, reader.peekStack().getClass());
    }

    @Test
    public void testNextLong() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(1234567890123456789L);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        long value = reader.nextLong();
        assertEquals(1234567890123456789L, value);
        assertNotEquals(JsonPrimitive.class, reader.peekStack().getClass());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(42);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        int value = reader.nextInt();
        assertEquals(42, value);
        assertNotEquals(JsonPrimitive.class, reader.peekStack().getClass());
    }

    @Test
    public void testClose() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(42);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        reader.close();
        assertEquals(JsonTreeReader.SENTINEL_CLOSED, reader.peekStack());
        assertEquals(1, reader.stackSize);
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.skipValue();
        assertEquals(JsonPrimitive.class, reader.peekStack().getClass());
        reader.endObject();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.promoteNameToValue();
        assertEquals("name", reader.nextString());
        reader.endObject();
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        reader.beginObject();
        assertEquals("$.[0].", reader.getPath());
        reader.endObject();
        reader.endArray();
    }
}