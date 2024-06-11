package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray(Arrays.asList(1, 2, 3)));
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.nextNull(); // consume null, should not affect stack size
        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray(Arrays.asList(1, 2, 3)));
        reader.beginArray();
        reader.nextNull(); // consume empty array
        reader.endArray();
        reader.nextNull(); // consume null, should not affect stack size
        reader.endArray();
        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.nextNull(); // consume null, should not affect stack size
        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size
        reader.endObject();
        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testHasNext() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(1));
        reader.push(new JsonPrimitive(true));
        reader.nextNull();

        assertEquals(true, reader.hasNext());
        assertEquals(JsonToken.BOOLEAN, reader.peek());

        reader.nextBoolean();
        reader.nextNull();

        assertEquals(true, reader.hasNext());
        assertEquals(JsonToken.NUMBER, reader.peek());

        reader.nextInt();
        reader.nextNull();

        assertEquals(false, reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testPeek() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(1));
        reader.push(new JsonPrimitive(true));
        reader.nextNull();

        assertEquals(JsonToken.BOOLEAN, reader.peek());

        reader.nextBoolean();
        reader.nextNull();

        assertEquals(JsonToken.NUMBER, reader.peek());

        reader.nextInt();
        reader.nextNull();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextName();
        reader.nextNull(); // consume null, should not affect stack size
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonObject());
        reader.push(new JsonPrimitive("foo"));
        reader.push(new JsonObject());
        reader.nextNull();

        assertEquals("foo", reader.nextName());
        assertEquals("foo", reader.pathNames[reader.stackSize - 1]);

        reader.nextNull();
        reader.nextNull();

        reader.push(new JsonPrimitive("bar"));
        reader.nextNull();

        assertEquals("bar", reader.nextName());
        assertEquals("bar", reader.pathNames[reader.stackSize - 1]);

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextString() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive("foo"));
        reader.nextNull();

        assertEquals("foo", reader.nextString());
        assertEquals("foo", reader.pathNames[reader.stackSize - 1]);

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(true));
        reader.nextNull();

        assertTrue(reader.nextBoolean());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextNull() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.nextNull();

        assertNull(reader.peek());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(1.23));
        reader.nextNull();

        assertEquals(1.23, reader.nextDouble(), 0.001);

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextLong() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(123));
        reader.nextNull();

        assertEquals(123L, reader.nextLong());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testNextInt() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive(123));
        reader.nextNull();

        assertEquals(123, reader.nextInt());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.close();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextNull(); // consume empty object
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.push(new JsonPrimitive("foo"));
        reader.nextNull();

        reader.skipValue();

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.nextName(); // consume "foo"
        reader.nextNull(); // consume null, should not affect stack size
        reader.nextNull(); // consume "null"
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        reader.promoteNameToValue();

        assertEquals("foo", reader.nextName());
        assertEquals("foo", reader.pathNames[reader.stackSize - 1]);
        assertEquals(JsonToken.STRING, reader.peek());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.beginObject();
        reader.push(new JsonObject());
        reader.push(new JsonPrimitive("foo"));
        reader.push(new JsonPrimitive(1));
        reader.nextNull();
        reader.endObject();
        reader.endObject();
        reader.nextNull(); // consume null, should not affect stack size

        assertEquals("$..foo[1]", reader.getPath());

        reader.nextNull();
        reader.nextNull();

        assertEquals(0, reader.stackSize);
    }
}