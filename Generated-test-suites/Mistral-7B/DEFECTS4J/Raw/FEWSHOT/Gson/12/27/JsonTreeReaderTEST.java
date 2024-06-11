package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBasicUse() throws IOException {
        String jsonString = "{\"foo\": [1, 2, 3], \"bar\": [{\"baz\": 4}, {}], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(1, reader.nextInt());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(2, reader.nextInt());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(3, reader.nextInt());
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("bar", reader.nextName());
        reader.beginArray();
        reader.beginObject();
        assertEquals("baz", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(4, reader.nextInt());
        reader.endObject();
        reader.beginObject();
        reader.endObject();
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testSkipValue() throws IOException {
        String jsonString = "{\"foo\": [1, 2, 3], \"bar\": [{\"baz\": 4}, {}], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        reader.skipValue();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        reader.nextInt();
        reader.nextInt();
        reader.nextInt();
        reader.endArray();
        reader.nextName();
        reader.beginArray();
        reader.beginObject();
        reader.nextName();
        reader.nextInt();
        reader.endObject();
        reader.beginObject();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        String jsonString = "{\"foo\": [1, 2], \"bar\": [{\"baz\": 4}, {}], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        reader.promoteNameToValue();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertEquals(1, reader.nextInt());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginObject();
        reader.nextName();
        assertEquals(4, reader.nextInt());
        reader.endObject();
        reader.beginObject();
        reader.endObject();
        reader.nextNull();
        reader.endArray();
        reader.nextName();
        reader.beginObject();
        reader.endObject();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testEmptyJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse("{}").getAsJson());
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testStringValues() throws IOException {
        String jsonString = "{\"foo\": \"abc\", \"bar\": [\"def\", \"ghi\"], \"qux\": \"jkl\"}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("abc", reader.nextString());
        reader.endObject();
        reader.beginArray();
        reader.beginObject();
        assertEquals("bar", reader.nextName());
        reader.beginArray();
        reader.nextString();
        reader.nextString();
        reader.endArray();
        reader.endObject();
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.endObject();
        reader.endArray();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testBooleanValues() throws IOException {
        String jsonString = "{\"foo\": true, \"bar\": [false, true], \"qux\": false}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertTrue(reader.nextBoolean());
        reader.endObject();
        reader.beginArray();
        reader.beginObject();
        assertEquals("bar", reader.nextName());
        reader.beginArray();
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertFalse(reader.nextBoolean());
        reader.beginObject();
        reader.nextName();
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertTrue(reader.nextBoolean());
        reader.endObject();
        reader.endArray();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testNullValues() throws IOException {
        String jsonString = "{\"foo\": null, \"bar\": [null], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.nextNull();
        reader.endObject();
        reader.beginArray();
        reader.beginObject();
        reader.nextName();
        reader.nextNull();
        reader.endObject();
        reader.endArray();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testNestedObjects() throws IOException {
        String jsonString = "{\"foo\": {\"bar\": 1, \"baz\": [2, 3]}, \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginObject();
        assertEquals("bar", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(1, reader.nextInt());
        reader.endObject();
        reader.beginArray();
        reader.beginObject();
        reader.nextName();
        reader.nextName();
        reader.endObject();
        reader.endArray();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testNestedArrays() throws IOException {
        String jsonString = "{\"foo\": [1, [2, 3]], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        reader.nextInt();
        reader.beginArray();
        reader.nextInt();
        reader.nextInt();
        reader.endArray();
        reader.endArray();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testDeepNestedObjects() throws IOException {
        String jsonString = "{\"foo\": { \"bar\": { \"baz\": { \"qux\": 1, \"quuz\": [2, 3] }, \"corge\": null }, \"quux\": null }, \"quarrel\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        reader.nextName();
        reader.beginObject();
        reader.nextName();
        reader.beginObject();
        reader.nextName();
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(1, reader.nextInt());
        reader.nextName();
        reader.beginArray();
        reader.nextInt();
        reader.nextInt();
        reader.endArray();
        reader.endObject();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testEmptyArrays() throws IOException {
        String jsonString = "{\"foo\": [], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("foo", reader.nextName());
        reader.beginArray();
        reader.endArray();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testEmptyObjects() throws IOException {
        String jsonString = "{\"foo\": {}, \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        reader.nextName();
        reader.endObject();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testDeepNestedArrays() throws IOException {
        String jsonString = "{\"foo\": [1, [2, [3, [4, [5, []]]]]], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        reader.nextName();
        reader.beginArray();
        reader.nextInt();
        reader.beginArray();
        reader.nextInt();
        reader.beginArray();
        reader.nextInt();
        reader.nextArray();
        reader.endArray();
        reader.endArray();
        reader.endArray();
        reader.nextNull();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterClosed() throws IOException {
        String jsonString = "{\"foo\": [1, 2], \"qux\": null}";
        JsonTreeReader reader = new JsonTreeReader(new JsonParser().parse(jsonString).getAsJson());
        reader.close();
        reader.nextInt();
    }
}