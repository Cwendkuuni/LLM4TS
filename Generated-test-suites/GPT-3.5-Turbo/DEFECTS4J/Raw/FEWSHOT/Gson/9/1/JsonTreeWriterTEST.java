
package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {
    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayWhenPendingNameIsNotNull() {
        jsonTreeWriter.name("array");
        jsonTreeWriter.beginArray();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonArray.class, jsonTreeWriter.get().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenStackIsEmpty() {
        jsonTreeWriter.endArray();
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectWhenPendingNameIsNotNull() {
        jsonTreeWriter.name("object");
        jsonTreeWriter.beginObject();
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonObject.class, jsonTreeWriter.get().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenStackIsEmpty() {
        jsonTreeWriter.endObject();
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenStackIsEmpty() {
        jsonTreeWriter.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenPendingNameIsNotNull() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.name("anotherName");
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        assertNotNull(jsonTreeWriter.get());
        assertEquals("name", ((JsonObject) jsonTreeWriter.get()).entrySet().iterator().next().getKey());
    }

    @Test
    public void testValueWithString() {
        jsonTreeWriter.value("value");
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals("value", ((JsonPrimitive) jsonTreeWriter.get()).getAsString());
    }

    @Test
    public void testValueWithNull() {
        jsonTreeWriter.nullValue();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test
    public void testValueWithBoolean() {
        jsonTreeWriter.value(true);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(true, ((JsonPrimitive) jsonTreeWriter.get()).getAsBoolean());
    }

    @Test
    public void testValueWithDouble() {
        jsonTreeWriter.value(1.0);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(1.0, ((JsonPrimitive) jsonTreeWriter.get()).getAsDouble(), 0.0);

        jsonTreeWriter.value(Double.NaN);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(Double.NaN, ((JsonPrimitive) jsonTreeWriter.get()).getAsDouble(), 0.0);

        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(Double.POSITIVE_INFINITY, ((JsonPrimitive) jsonTreeWriter.get()).getAsDouble(), 0.0);
    }

    @Test
    public void testValueWithLong() {
        jsonTreeWriter.value(1L);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(1L, ((JsonPrimitive) jsonTreeWriter.get()).getAsLong());
    }

    @Test
    public void testValueWithNumber() {
        jsonTreeWriter.value(null);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());

        jsonTreeWriter.value(1);
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.get().getClass());
        assertEquals(1, ((JsonPrimitive) jsonTreeWriter.get()).getAsInt());
    }

    @Test
    public void testFlush() {
        // No need to test this method as it does nothing.
    }

    @Test(expected = IOException.class)
    public void testCloseWhenStackIsNotEmpty() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeWriter.close();
        assertNotNull(jsonTreeWriter.get());
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }
}
