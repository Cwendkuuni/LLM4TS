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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonTreeReaderTest {
    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        jsonTreeReader = new JsonTreeReader(createJsonElement());
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();

        assertTrue(jsonTreeReader.hasNext());
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.endArray();

        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();

        assertTrue(jsonTreeReader.hasNext());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.endObject();

        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.beginArray();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        jsonTreeReader.beginObject();

        assertEquals("key1", jsonTreeReader.nextName());
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        assertEquals("value1", jsonTreeReader.nextString());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        assertTrue(jsonTreeReader.nextBoolean());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());

        jsonTreeReader.nextName();
        assertFalse(jsonTreeReader.nextBoolean());
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        assertEquals(123.45, jsonTreeReader.nextDouble(), 0.0);
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        assertEquals(12345L, jsonTreeReader.nextLong());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();

        assertEquals(10, jsonTreeReader.nextInt());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();

        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.skipValue();

        assertEquals(JsonToken.NAME, jsonTreeReader.peek());

        jsonTreeReader.nextName();
        jsonTreeReader.skipValue();

        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        jsonTreeReader.getPath();

        assertEquals("$", jsonTreeReader.getPath());

        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.beginArray();
        jsonTreeReader.nextString();
        jsonTreeReader.endArray();
        jsonTreeReader.endObject();
        jsonTreeReader.nextName();
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.endObject();
        jsonTreeReader.endObject();

        assertEquals("$.key1[0].key2", jsonTreeReader.getPath());
    }

    private JsonElement createJsonElement() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", true);
        jsonObject.addProperty("key3", null);
        jsonObject.addProperty("key4", 12345L);
        jsonObject.addProperty("key5", 10);
        jsonObject.addProperty("key6", 123.45);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));
        jsonArray.add(new JsonPrimitive("value2"));
        jsonObject.add("key7", jsonArray);

        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("nestedKey", "nestedValue");
        jsonObject.add("key8", nestedObject);

        return jsonObject;
    }
}