package com.google.gson.internal.bind;

import static com.google.gson.JsonElement.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testEmpty() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("[]"));
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSingleElementArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("['foo']"));
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testArrayWithMultipleElements() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("['foo', 'bar']"));
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals("bar", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testArrayWithNullElement() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("['foo', null, 'bar']"));
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertTrue(reader.hasNext());
        assertNull(reader.nextNull());
        assertTrue(reader.hasNext());
        assertEquals("bar", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testArrayWithAnotherArrayAsElement() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("['foo', ['bar', 'baz']]"));
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertTrue(reader.hasNext());
        JsonTreeReader innerReader = new JsonTreeReader(reader.beginArray());
        assertTrue(innerReader.hasNext());
        assertEquals("bar", innerReader.nextString());
        assertTrue(innerReader.hasNext());
        assertEquals("baz", innerReader.nextString());
        assertFalse(innerReader.hasNext());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": \"foo\"}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithEmptyArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": []}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertFalse(reader.hasNext());
        reader.beginArray();
        assertNull(reader.nextValue());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithEmptyObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": {}}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertTrue(reader.peek() == JsonToken.BEGIN_OBJECT);
        reader.beginObject();
        assertFalse(reader.hasNext());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithNullValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": null}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertNull(reader.nextNull());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithBooleanValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": true}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals(true, reader.nextBoolean());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithNumberValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": 123}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals(123, reader.nextInt());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithStringValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": \"foo\"}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithArrayValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": [1, 2, 3]}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        JsonTreeReader innerReader = new JsonTreeReader(reader.beginArray());
        assertTrue(innerReader.hasNext());
        assertEquals(1, innerReader.nextInt());
        assertTrue(innerReader.hasNext());
        assertEquals(2, innerReader.nextInt());
        assertTrue(innerReader.hasNext());
        assertEquals(3, innerReader.nextInt());
        assertFalse(innerReader.hasNext());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithObjectValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": {\"foo\": 1}}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        JsonTreeReader innerReader = new JsonTreeReader(reader.beginObject());
        assertTrue(innerReader.hasNext());
        assertEquals("foo", innerReader.nextName());
        assertTrue(innerReader.hasNext());
        assertEquals(1, innerReader.nextInt());
        assertFalse(innerReader.hasNext());
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithMultipleValues() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{" +
                "\"name\": \"foo\"," +
                "\"age\": 25," +
                "\"isStudent\": true," +
                "\"hobbies\": [\"reading\", \"watching TV\"]" +
                "}"));
        assertTrue(reader.hasNext());
        assertEquals("name", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals("foo", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals("age", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals(25, reader.nextInt());
        assertTrue(reader.hasNext());
        assertEquals("isStudent", reader.nextName());
        assertTrue(reader.hasNext());
        assertEquals(true, reader.nextBoolean());
        assertTrue(reader.hasNext());
        assertEquals("hobbies", reader.nextName());
        assertTrue(reader.hasNext());
        JsonTreeReader innerReader = new JsonTreeReader(reader.beginArray());
        assertTrue(innerReader.hasNext());
        assertEquals("reading", innerReader.nextString());
        assertTrue(innerReader.hasNext());
        assertEquals("watching TV", innerReader.nextString());
        assertFalse(innerReader.hasNext());
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": \"foo\", \"age\": 25}"));
        reader.skipValue();
        assertEquals("age", reader.nextName());
        assertEquals(25, reader.nextInt());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"name\": \"foo\"}"));
        reader.promoteNameToValue();
        assertEquals("foo", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(parse("{\"foo\": [1, 2, 3], \"bar\": {}}"));
        assertEquals("$", reader.getPath());

        reader.beginArray();
        assertEquals("[0]", reader.getPath());
        reader.nextInt();
        assertEquals("[1]", reader.getPath());
        reader.endArray();

        reader.beginObject();
        assertEquals(".$", reader.getPath());
        reader.nextName();
        assertEquals("foo.$[0]", reader.getPath());
        reader.nextInt();
        reader.endObject();

        reader.beginObject();
        reader.nextName();
        assertEquals("bar.$", reader.getPath());
        reader.endObject();
    }
}