package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonTreeReaderTest {

    private static JsonElement createElement(String json) {
        JsonReader reader = new JsonReader(new StringReader(json));
        JsonElement element = new JsonTreeReader(element).nextTree();
        reader.close();
        return element;
    }

    @Test
    public void testEmptyJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.beginObject();
        reader.endObject();
        reader.close();
    }

    @Test
    public void testSingleElementJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
        reader.beginObject();
        reader.name("foo");
        reader.value(new JsonPrimitive(true));
        reader.endObject();
        reader.close();

        String expectedPath = "$";
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.nextName();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(true, reader.nextBoolean());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.close();
    }

    @Test
    public void testArrayJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        reader.beginObject();
        reader.name("foo");
        reader.value(new JsonArray());
        reader.endObject();
        reader.close();

        String expectedPath = "$";
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.nextName();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.nextName(); // empty name for the first element
        reader.value(new JsonPrimitive(1));
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.nextName(); // null name for the second element
        reader.value(new JsonPrimitive(2));
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testNestedJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonObject() {{
                add("bar", new JsonPrimitive(1));
                add("baz", new JsonArray() {{
                    add(new JsonPrimitive(2));
                    add(new JsonPrimitive(3));
                }});
            }});
            add("qux", new JsonPrimitive(4));
        }});

        String expectedPath = "$";
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.nextName();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo", reader.getPath());
        reader.nextName();
        assertEquals("bar", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(false, reader.nextBoolean()); // false for tests
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo", reader.getPath());
        reader.nextName();
        assertEquals("baz", reader.nextName());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        assertEquals(expectedPath + ".foo.baz", reader.getPath());
        reader.nextName(); // empty name for the first element
        reader.value(new JsonPrimitive(5));
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertEquals(expectedPath + ".foo.baz", reader.getPath());
        reader.nextName(); // null name for the second element
        reader.value(new JsonPrimitive(6));
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo", reader.getPath());
        reader.nextName();
        assertEquals("qux", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(false, reader.nextBoolean()); // false for tests
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.close();
    }

    @Test
    public void testArrayWithObjectsJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject() {{
            add("foo", new JsonArray() {{
                add(new JsonObject() {{
                    add("bar", new JsonPrimitive(1));
                    add("baz", new JsonPrimitive(2));
                }});
                add(new JsonObject() {{
                    add("bar", new JsonPrimitive(3));
                    add("baz", new JsonPrimitive(4));
                }});
            }});
        }});

        String expectedPath = "$";
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.nextName();
        assertEquals("foo", reader.nextName());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        assertEquals(expectedPath + ".foo", reader.getPath());
        reader.nextName(); // empty name for the first element
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo.[0]", reader.getPath());
        reader.nextName();
        assertEquals("bar", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(false, reader.nextBoolean()); // false for tests
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo.[0]", reader.getPath());
        reader.nextName();
        assertEquals("baz", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(2.0, reader.nextDouble());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo.[0]", reader.getPath());
        reader.nextName(); // null name for the second element
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo.[1]", reader.getPath());
        reader.nextName();
        assertEquals("bar", reader.nextName());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(false, reader.nextBoolean()); // false for tests
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath + ".foo.[1]", reader.getPath());
        reader.nextName();
        assertEquals("baz", reader.nextName());
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(4.0, reader.nextDouble());
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        reader.close();
    }

    @Test
    public void testPushPop() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        Object top = reader.peekStack();
        reader.push(new JsonPrimitive(1));
        Object newTop = reader.peekStack();
        assertEquals(newTop, reader.popStack());
        assertEquals(top, reader.peekStack());
        reader.close();
    }

    @Test
    public void testGetPath() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.nextName();
        reader.value(new JsonPrimitive(1));
        reader.beginArray();
        reader.value(new JsonPrimitive(2));
        reader.value(new JsonPrimitive(3));
        reader.endArray();
        reader.nextName();
        reader.value(new JsonPrimitive(4));
        reader.nextName();
        reader.value(new JsonPrimitive(5));
        reader.endObject();

        List<String> expectedPathList = Arrays.asList(
            "$",
            ".[0]",
            ".[0]",
            ".[1]",
            ".[1]",
            ".[2]",
            ".[2]",
            ".[3]",
            ".[3]",
            ".[4]",
            ".[4]"
        );
        Iterator<String> expectedPathIterator = Collections.list(expectedPathList).iterator();

        String expectedPath = expectedPathIterator.next();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        String actualPath = reader.nextName();
        assertEquals("", actualPath);
        assertEquals(JsonToken.NULL, reader.peek());
        assertEquals(expectedPath + ".[0]", reader.getPath());
        reader.nextName();
        assertEquals("1", reader.nextName());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        assertEquals(expectedPath + ".[0].[0]", reader.getPath());
        reader.nextName();
        assertEquals("2", reader.nextName());
        reader.nextName();
        assertEquals("3", reader.nextName());
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertEquals(expectedPath + ".[0]", reader.getPath());
        reader.nextName();
        assertEquals("4", reader.nextName());
        reader.nextName();
        assertEquals("5", reader.nextName());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertEquals(expectedPath, reader.getPath());
        assertEquals(expectedPathList, Arrays.asList(reader.getPath().split("\\.")));
        reader.close();
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.nextName();
        reader.value(new JsonPrimitive("foo"));
        reader.skipValue();
        reader.nextName();
        assertEquals("", reader.nextName());
        reader.value(new JsonPrimitive(1));
        assertEquals(JsonToken.NUMBER, reader.peek());
        reader.skipValue();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.close();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        reader.nextName();
        reader.value(new JsonObject());
        reader.promoteNameToValue();
        reader.nextName();
        assertEquals("{}", reader.nextName());
        reader.close();

        reader = new JsonTreeReader(new JsonObject());
        reader.nextName();
        reader.value(new JsonArray());
        reader.promoteNameToValue();
        reader.nextName();
        assertEquals("[{}]", reader.nextName());
        reader.close();

        reader = new JsonTreeReader(new JsonObject());
        reader.nextName();
        reader.value(new JsonArray());
        reader.nextName();
        reader.value(new JsonObject());
        reader.promoteNameToValue();
        reader.nextName();
        assertEquals("[{{}}]", reader.nextName());
        reader.close();
    }

    @Test
    public void testUriSafeCharacters() throws IOException {
        JsonElement jsonElement = new JsonObject() {{
            add("foo", new JsonObject() {{
                add("bar", new JsonPrimitive("a%20b"));
            }});
        }};

        JsonTreeReader reader = new JsonTreeReader(jsonElement);
        reader.beginObject();
        reader.nextName();
        reader.nextName();
        assertEquals("a b", reader.nextString());
        reader.endObject();
        reader.close();
    }

    @Test
    public void testReadNullJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.beginObject();
        reader.endObject();
        reader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.nextTree();
    }
}