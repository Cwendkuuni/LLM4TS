package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        jsonArray.add(new JsonPrimitive("bar"));

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        jsonArray.add(new JsonPrimitive("bar"));

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        reader.endArray();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 25);

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 25);

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.endObject();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        jsonArray.add(new JsonPrimitive("bar"));

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        assertTrue(reader.hasNext());
        reader.next();
        assertTrue(reader.hasNext());
        reader.next();
        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        jsonArray.add(new JsonPrimitive("bar"));

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 25);

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();

        assertEquals("name", reader.nextName());
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();

        assertEquals("John", reader.nextString());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(true);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(true, reader.nextBoolean());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonNull jsonNull = JsonNull.INSTANCE;

        JsonTreeReader reader = new JsonTreeReader(jsonNull);

        reader.nextNull();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(3.14);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(3.14, reader.nextDouble(), 0);
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(100000000000000L);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(100000000000000L, reader.nextLong());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(123);

        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(123, reader.nextInt());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("foo"));
        jsonArray.add(new JsonPrimitive("bar"));

        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        reader.skipValue();

        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 25);
        jsonObject.addProperty("city", "New York");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.nextName();
        reader.beginObject();
        reader.nextName();
        reader.beginArray();
        reader.skipValue();
        reader.nextString();

        assertEquals("$.name", reader.getPath());
    }
}