package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {
    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonElement jsonElement = new JsonObject();
        jsonElement.getAsJsonObject().addProperty("name", "John");
        jsonElement.getAsJsonObject().addProperty("age", 30);
        jsonElement.getAsJsonObject().addProperty("city", "New York");
        jsonTreeReader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        assertEquals("name", jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        assertEquals("John", jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginObject();
        assertTrue(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextNull();
        assertEquals("null", jsonTreeReader.getPath());
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.nextDouble();
        assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(30, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(30, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        assertNull(jsonTreeReader.getPath());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.skipValue();
        assertEquals("null", jsonTreeReader.getPath());
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        assertEquals("$", jsonTreeReader.getPath());
        jsonTreeReader.nextName();
        assertEquals("$.name", jsonTreeReader.getPath());
    }
}