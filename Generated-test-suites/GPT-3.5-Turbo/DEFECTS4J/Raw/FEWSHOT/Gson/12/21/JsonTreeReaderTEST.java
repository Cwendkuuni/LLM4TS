
package com.google.gson.internal.bind;

import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();
        
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();
        reader.endArray();

        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testBeginObject() throws Exception {
        JsonObject object = new JsonObject();
        JsonElement element = object;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws Exception {
        JsonObject object = new JsonObject();
        JsonElement element = object;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.endObject();

        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testHasNext() throws Exception {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("test"));
        array.add(new JsonPrimitive("test"));
        JsonElement element = array;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();

        assertTrue(reader.hasNext());

        reader.skipValue();

        assertTrue(reader.hasNext());

        reader.skipValue();

        assertFalse(reader.hasNext());

        reader.endArray();
    }

    @Test
    public void testPeek() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextName() throws Exception {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive("test"));
        JsonElement element = object;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();

        assertEquals("name", reader.nextName());
    }

    @Test
    public void testNextString() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals("test", reader.nextString());
    }

    @Test
    public void testNextBoolean() throws Exception {
        JsonElement element = new JsonPrimitive(true);
        JsonTreeReader reader = new JsonTreeReader(element);

        assertTrue(reader.nextBoolean());
    }

    @Test
    public void testNextNull() throws Exception {
        JsonElement element = JsonNull.INSTANCE;
        JsonTreeReader reader = new JsonTreeReader(element);

        reader.nextNull();
    }

    @Test
    public void testNextDouble() throws Exception {
        JsonElement element = new JsonPrimitive(1.0);
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals(1.0, reader.nextDouble(), 0.0);
    }

    @Test
    public void testNextLong() throws Exception {
        JsonElement element = new JsonPrimitive(1L);
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals(1L, reader.nextLong());
    }

    @Test
    public void testNextInt() throws Exception {
        JsonElement element = new JsonPrimitive(1);
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals(1, reader.nextInt());
    }

    @Test
    public void testClose() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);

        reader.close();
    }

    @Test
    public void testSkipValue() throws Exception {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);

        reader.skipValue();
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive("test"));
        JsonElement element = object;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();

        reader.promoteNameToValue();

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testGetPath() throws Exception {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("test"));
        JsonObject object = new JsonObject();
        object.add("name", array);
        JsonElement element = object;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        assertEquals("$.name[0]", reader.getPath());
    }
}
