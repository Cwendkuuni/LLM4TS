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
import com.google.gson.JsonToken;

import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testEmpty() {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testBeginEndArrayWithObjects() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonObject()).add(new JsonObject()));
        reader.beginArray();
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.endObject();
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.endObject();
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testEndArrayWithoutBeginArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.endArray();
        fail("Expected 'JsonTreeReader.expected' exception");
        reader.close();
    }

    @Test
    public void testEndObjectWithoutBeginObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.endObject();
        fail("Expected 'JsonTreeReader.expected' exception");
        reader.close();
    }

    @Test
    public void testHasNext() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.nextValue();
        assertTrue(reader.hasNext());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testPeek() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        reader.nextString();
        reader.nextInt();
        reader.endArray();
        reader.nextName();
        reader.nextValue();
        reader.nextBoolean();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextName() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        assertEquals("name1", reader.nextName());
        reader.nextValue();
        assertEquals("value1", reader.nextString());
        reader.nextInt();
        reader.nextBoolean();
        assertEquals("name2", reader.nextName());
        reader.nextValue();
        assertEquals("value2", reader.nextString());
        reader.nextInt();
        reader.nextBoolean();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextBoolean();
        reader.nextName();
        reader.nextValue();
        reader.nextBoolean();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextDouble();
        reader.nextName();
        reader.nextValue();
        reader.nextDouble();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextInt() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextInt();
        reader.nextName();
        reader.nextValue();
        reader.nextInt();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextLong() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextLong();
        reader.nextName();
        reader.nextValue();
        reader.nextLong();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextString() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextString();
        reader.nextName();
        reader.nextValue();
        reader.nextString();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextNull() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextNull();
        reader.nextName();
        reader.nextValue();
        reader.nextNull();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextValue();
        reader.nextName();
        reader.nextValue();
        reader.nextValue();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testNextValueWithArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray().iterator());
        reader.nextValue();
        reader.nextValue();
        reader.nextValue();
        reader.nextValue();
        reader.nextValue();
        reader.nextValue();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testPeekStack() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        assertEquals(reader.peekStack(), reader.nextValue());
        reader.nextName();
        reader.nextValue();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testPopStack() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.nextName();
        reader.nextValue();
        reader.endObject();
        Object top = reader.popStack();
        assertEquals(reader.nextValue(), top);
        Object top2 = reader.popStack();
        assertEquals(reader.nextValue(), top2);
        assertNull(reader.popStack());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testPush() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.push(reader.nextValue());
        reader.nextName();
        reader.nextValue();
        reader.nextValue();
        reader.endObject();
        Object top = reader.popStack();
        assertEquals(reader.nextValue(), top);
        Object top2 = reader.popStack();
        assertEquals(reader.nextValue(), top2);
        assertNull(reader.popStack());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        reader.skipValue();
        reader.nextName();
        reader.nextValue();
        reader.nextValue();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testExpected() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("notJsonObject"));
        try {
            reader.beginObject();
            fail("Expected 'JsonTreeReader.expected' exception");
        } catch (IllegalStateException e) {
            // good
        }
        reader.close();
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().entrySet().iterator());
        reader.beginObject();
        reader.nextName();
        reader.nextValue();
        assertEquals("name1", reader.getPath().substring(1, reader.getPath().length() - 1));
        reader.nextName();
        reader.nextValue();
        assertEquals("name2", reader.getPath().substring(1, reader.getPath().length() - 1));
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }
}