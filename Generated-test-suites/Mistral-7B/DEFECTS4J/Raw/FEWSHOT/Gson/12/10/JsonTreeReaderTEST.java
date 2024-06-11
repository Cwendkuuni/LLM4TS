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
    public void testEmpty() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSingleObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        reader.close();
    }

    @Test
    public void testSingleArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        reader.close();
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        reader.close();
    }

    @Test
    public void testSingleString() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("foo"));
        assertEquals(JsonToken.STRING, reader.peek());
        String result = reader.nextString();
        assertEquals("foo", result);
        reader.nextToken(); // consume next token
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSingleBoolean() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        boolean result = reader.nextBoolean();
        assertTrue(result);
        reader.nextToken(); // consume next token
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSingleNumber() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123));
        assertEquals(JsonToken.NUMBER, reader.peek());
        double result = reader.nextDouble();
        assertEquals(123.0, result, 0.0);
        reader.nextToken(); // consume next token
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSingleInt() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123));
        assertEquals(JsonToken.NUMBER, reader.peek());
        int result = reader.nextInt();
        assertEquals(123, result);
        reader.nextToken(); // consume next token
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSingleNull() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(null));
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
        reader.nextToken(); // consume next token
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testComplexObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonPrimitive("bar"));
            add("baz", new JsonArray() {{
                add(new JsonPrimitive(1));
                add(new JsonPrimitive(2));
                add(new JsonPrimitive(3));
            }});
        }});

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("foo", reader.nextString());
        assertEquals("bar", reader.nextString());
        reader.nextToken(); // consume next token

        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("baz", reader.nextString());
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        JsonArray array = reader.nextArray();
        assertEquals(3, array.size());
        assertEquals(1.0, array.get(0).getAsDouble(), 0.0);
        assertEquals(2.0, array.get(1).getAsDouble(), 0.0);
        assertEquals(3.0, array.get(2).getAsDouble(), 0.0);
        reader.nextToken(); // consume next token

        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testComplexArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonArray() {{
                add(new JsonPrimitive("bar"));
                add(new JsonPrimitive("baz"));
            }});
        }});

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("foo", reader.nextString());
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        JsonArray array = reader.nextArray();
        assertEquals(2, array.size());
        assertEquals("bar", array.get(0).getAsString());
        assertEquals("baz", array.get(1).getAsString());
        reader.nextToken(); // consume next token
        reader.endArray();
        reader.nextToken(); // consume next token
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testComplexNestedObjects() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonObject() {{
                add("bar", new JsonPrimitive("1"));
                add("baz", new JsonObject() {{
                    add("qux", new JsonPrimitive("quux"));
                    add("quuz", new JsonPrimitive("corge"));
                }});
            }});
        }});

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("foo", reader.nextString());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("bar", reader.nextString());
        reader.nextToken(); // consume next token

        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("baz", reader.nextString());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("qux", reader.nextString());
        assertEquals("quux", reader.nextString());
        reader.nextToken(); // consume next token

        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("quuz", reader.nextString());
        assertEquals("corge", reader.nextString());
        reader.nextToken(); // consume next token
        reader.endObject();
        reader.nextToken(); // consume next token
        reader.endObject();
        reader.nextToken(); // consume next token
        reader.endObject();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testComplexNestedArrays() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonArray() {{
                add(new JsonArray() {{
                    add(new JsonPrimitive("bar"));
                    add(new JsonPrimitive("baz"));
                }});
                add(new JsonArray() {{
                    add(new JsonPrimitive("qux"));
                    add(new JsonPrimitive("quuz"));
                }});
            }});
        }});

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("foo", reader.nextString());
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        JsonArray array = reader.nextArray();
        assertEquals(1, array.size());
        JsonArray nestedArray = array.get(0).getAsJsonArray();
        assertEquals(2, nestedArray.size());
        assertEquals("bar", nestedArray.get(0).getAsString());
        assertEquals("baz", nestedArray.get(1).getAsString());
        reader.nextToken(); // consume next token

        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        JsonArray nestedArray2 = reader.nextArray();
        assertEquals(2, nestedArray2.size());
        assertEquals("qux", nestedArray2.get(0).getAsString());
        assertEquals("quuz", nestedArray2.get(1).getAsString());
        reader.nextToken(); // consume next token
        reader.endArray();
        reader.nextToken(); // consume next token
        reader.endObject();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        reader.close();
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonPrimitive("bar"));
        }});
        reader.skipValue();
        reader.nextNull();
        reader.nextToken(); // consume next token
        reader.nextToken(); // consume next token
        reader.endObject();
        reader.close();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonPrimitive("bar"));
        }});
        reader.promoteNameToValue();
        assertEquals("bar", reader.nextString());
        reader.nextToken(); // consume next token
        reader.nextToken(); // consume next token
        reader.endObject();
        reader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.nextString();
    }
}