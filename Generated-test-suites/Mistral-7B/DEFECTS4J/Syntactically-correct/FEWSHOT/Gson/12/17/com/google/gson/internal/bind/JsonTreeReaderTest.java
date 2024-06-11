package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testEmptyJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSimpleJsonObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": 1}"));
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(1.0, reader.nextDouble(), 0.0);
        reader.nextNull();
        assertEquals("", reader.nextName());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSimpleJsonObjectIterative() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": 1}"));
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(1.0, reader.nextDouble(), 0.0);
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonObjectWithArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": [1, 2], \"bar\": 3}"));
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        assertEquals(1.0, reader.nextDouble(), 0.0);
        assertEquals(2.0, reader.nextDouble(), 0.0);
        reader.endArray();
        reader.nextNull();
        assertEquals("bar", reader.nextName());
        assertEquals(3.0, reader.nextDouble(), 0.0);
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonObjectWithEmptyArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": [], \"bar\": 3}"));
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        assertFalse(reader.hasNext());
        reader.endArray();
        reader.nextNull();
        assertEquals("bar", reader.nextName());
        assertEquals(3.0, reader.nextDouble(), 0.0);
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
        reader.beginArray();
        assertEquals(1.0, reader.nextDouble(), 0.0);
        assertEquals(2.0, reader.nextDouble(), 0.0);
        assertEquals(3.0, reader.nextDouble(), 0.0);
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonArrayIterative() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertEquals(1.0, reader.nextDouble(), 0.0);
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(2.0, reader.nextDouble(), 0.0);
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonReaderClosed() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
        reader.close();
        reader.nextName();
    }

    @Test
    public void testJsonReaderSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": null}"));
        reader.skipValue();
        assertEquals("foo", reader.nextName());
        reader.nextNull();
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderPromoteNameToValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": null}"));
        reader.promoteNameToValue();
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderWithEmptyJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(""));
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderWithInvalidJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{"));
        try {
            reader.nextName();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
        reader.close();
    }

    @Test
    public void testJsonReaderWithJsonPrimitive() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("1"));
        assertEquals(1.0, reader.nextDouble(), 0.0);
        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.endDocument();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderWithBooleanPrimitive() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(boolean.class, "true"));
        assertEquals(true, reader.nextBoolean());
        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.endDocument();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderWithStringPrimitive() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("\"foo\""));
        assertEquals("foo", reader.nextString());
        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.endDocument();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testJsonReaderWithNullPrimitive() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("null"));
        reader.nextNull();
        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.endDocument();
        assertFalse(reader.hasNext());
        reader.close();
    }
}