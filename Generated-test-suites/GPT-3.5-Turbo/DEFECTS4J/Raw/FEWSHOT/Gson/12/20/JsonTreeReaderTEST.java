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

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Before
    public void setup() {
        reader = new JsonTreeReader(new JsonObject());
    }

    @Test
    public void testBeginArray() throws IOException {
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        assertTrue(reader.hasNext());
    }

    @Test
    public void testEndArray() throws IOException {
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testBeginObject() throws IOException {
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertTrue(reader.hasNext());
    }

    @Test
    public void testEndObject() throws IOException {
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        assertEquals("", reader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        reader.promoteNameToValue();
        assertEquals("", reader.nextString());
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        reader.promoteNameToValue();
        assertTrue(reader.nextBoolean());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        reader.promoteNameToValue();
        reader.nextNull();
        assertEquals(JsonToken.NULL, reader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        reader.promoteNameToValue();
        assertEquals(0.0, reader.nextDouble(), 0.0);
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        reader.promoteNameToValue();
        assertEquals(0L, reader.nextLong());
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        reader.promoteNameToValue();
        assertEquals(0, reader.nextInt());
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testClose() throws IOException {
        reader.close();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        reader.skipValue();
        assertNull(reader.nextName());
    }

    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", reader.toString());
    }

    @Test
    public void testGetPath() {
        assertEquals("$", reader.getPath());
    }
}