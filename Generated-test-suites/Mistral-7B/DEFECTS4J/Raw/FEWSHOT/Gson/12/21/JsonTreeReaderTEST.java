package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {
    private static JsonElement createJsonElement(String json) {
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        JsonElement element = new JsonTreeReader(element).nextTree();
        jsonReader.close();
        return element;
    }

    @Test
    public void testEmptyJson() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSingleString() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("hello"));
        assertEquals("hello", reader.nextString());
        assertNull(reader.nextName());
        reader.close();
    }

    @Test
    public void testSingleNumber() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123));
        assertEquals(123, reader.nextInt());
        assertNull(reader.nextName());
        reader.close();
    }

    @Test
    public void testSingleBoolean() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
        assertTrue(reader.nextBoolean());
        assertNull(reader.nextName());
        reader.close();
    }

    @Test
    public void testSingleArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        assertTrue(reader.hasNext());
        reader.beginArray();
        reader.endArray();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testSingleObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        assertTrue(reader.hasNext());
        reader.beginObject();
        reader.endObject();
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testArrayWithSingleElement() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("hello")));
        assertEquals("hello", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testArrayWithMultipleElements() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("hello"))
                .add(new JsonPrimitive(123))
                .add(new JsonPrimitive(true)));
        assertEquals("hello", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals(123, reader.nextInt());
        assertTrue(reader.hasNext());
        assertTrue(reader.nextBoolean());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithSingleNameValuePair() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", new JsonPrimitive("hello")));
        assertEquals("name", reader.nextName());
        assertEquals("hello", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testObjectWithMultipleNameValuePairs() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject()
                .addProperty("name1", new JsonPrimitive("hello1"))
                .addProperty("name2", new JsonPrimitive("hello2"))
                .addProperty("name3", new JsonPrimitive("hello3"))
        );
        assertEquals("name1", reader.nextName());
        assertEquals("hello1", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals("name2", reader.nextName());
        assertEquals("hello2", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals("name3", reader.nextName());
        assertEquals("hello3", reader.nextString());
        assertFalse(reader.hasNext());
        reader.close();
    }

    @Test
    public void testComplexJsonTree() throws IOException {
        String json = "{" +
                "  \"foo\": [ {" +
                "      \"name\": \"bar\"," +
                "      \"items\": [" +
                "        {" +
                "          \"name\": \"baz\", " +
                "          \"value\": 123," +
                "        }," +
                "        {" +
                "          \"name\": \"qux\"," +
                "          \"value\": [ 1, 2, 3 ]," +
                "        }" +
                "      ]" +
                "    }" +
                "  ]," +
                "  \"qux\": [ \"quux\", \"quuz\" ]" +
                "}";
        JsonTreeReader reader = new JsonTreeReader(createJsonElement(json));
        String path = "";
        while (reader.hasNext()) {
            if (reader.isJsonArray()) {
                path += "[";
                int index = reader.nextInt();
                path += index;
                path += "]";
            } else if (reader.isJsonObject()) {
                path += ".";
                String name = reader.nextName();
                path += name;
            } else {
                path += reader.nextString();
            }
            System.out.println(path + ": " + reader.nextString());
            assertEquals("bar", reader.nextName());
            assertEquals("baz", reader.nextName());
            assertEquals("123", reader.nextString());
            reader.skipValue();
            assertEquals("qux", reader.nextName());
            reader.beginArray();
            reader.nextString();
            reader.nextInt();
            reader.nextString();
            reader.endArray();
            reader.skipValue();
            assertEquals("quuz", reader.nextString());
            reader.close();
        }
    }
}