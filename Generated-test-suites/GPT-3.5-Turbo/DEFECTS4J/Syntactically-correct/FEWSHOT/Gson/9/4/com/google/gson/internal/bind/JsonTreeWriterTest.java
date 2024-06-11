package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
    private JsonTreeWriter writer;
    private StringWriter stringWriter;
    private Gson gson;

    @Before
    public void setUp() {
        writer = new JsonTreeWriter();
        stringWriter = new StringWriter();
        gson = new Gson();
    }

    @Test
    public void testGet() {
        writer.beginObject();
        writer.name("name").value("John");
        writer.name("age").value(30);
        writer.endObject();

        JsonObject expected = new JsonObject();
        expected.addProperty("name", "John");
        expected.addProperty("age", 30);

        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_withNonEmptyStack() {
        writer.beginObject();

        writer.get(); // Should throw IllegalStateException
    }

    @Test
    public void testBeginArray() {
        writer.beginArray();

        assertEquals(1, writer.get().getAsJsonArray().size());
        assertTrue(writer.stack.get(0) instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        writer.beginArray();
        writer.endArray();

        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_withEmptyStack() {
        writer.endArray(); // Should throw IllegalStateException
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_withNonArrayStack() {
        writer.beginObject();
        writer.endArray(); // Should throw IllegalStateException
    }

    @Test
    public void testBeginObject() {
        writer.beginObject();

        assertEquals(1, writer.get().getAsJsonObject().entrySet().size());
        assertTrue(writer.stack.get(0) instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        writer.beginObject();
        writer.endObject();

        assertTrue(writer.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_withEmptyStack() {
        writer.endObject(); // Should throw IllegalStateException
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_withNonObjectStack() {
        writer.beginArray();
        writer.endObject(); // Should throw IllegalStateException
    }

    @Test
    public void testName() {
        writer.beginObject();
        writer.name("name");

        assertEquals("name", writer.pendingName);
    }

    @Test(expected = IllegalStateException.class)
    public void testName_withEmptyStack() {
        writer.name("name"); // Should throw IllegalStateException
    }

    @Test(expected = IllegalStateException.class)
    public void testName_withNonObjectStack() {
        writer.beginArray();
        writer.name("name"); // Should throw IllegalStateException
    }

    @Test
    public void testValue_String() {
        writer.value("John");

        assertEquals("John", writer.stack.get(0).getAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_String_withEmptyStack() {
        writer.value("John"); // Should throw IllegalStateException
    }

    @Test
    public void testNullValue() {
        writer.nullValue();

        assertTrue(writer.stack.get(0).isJsonNull());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullValue_withEmptyStack() {
        writer.nullValue(); // Should throw IllegalStateException
    }

    @Test
    public void testValue_boolean() {
        writer.value(true);

        assertTrue(writer.stack.get(0).getAsBoolean());
    }

    @Test
    public void testValue_double() {
        writer.value(5.5);

        assertEquals(5.5, writer.stack.get(0).getAsDouble(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_double_withInfinityNonLenient() {
        writer.setLenient(false);
        writer.value(Double.POSITIVE_INFINITY); // Should throw IllegalArgumentException
    }

    @Test
    public void testValue_long() {
        writer.value(10L);

        assertEquals(10L, writer.stack.get(0).getAsLong());
    }

    @Test
    public void testValue_Number() {
        writer.value(100);

        assertEquals(100, writer.stack.get(0).getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Number_withEmptyStack() {
        writer.value(100); // Should throw IllegalStateException
    }

    @Test
    public void testFlush() throws IOException {
        writer.beginObject();
        writer.name("name").value("John");
        writer.flush();

        assertTrue(writer.stack.isEmpty());
    }

    @Test
    public void testClose() throws IOException {
        writer.beginObject();
        writer.name("name").value("John");
        writer.close();

        assertFalse(writer.stack.isEmpty());
        assertTrue(writer.stack.get(writer.stack.size() - 1) instanceof JsonPrimitive);
    }

    @Test(expected = IOException.class)
    public void testClose_withNonEmptyStack() throws IOException {
        writer.beginObject();
        writer.close(); // Should throw IOException
    }

    @Test
    public void testWriteJson() throws IOException {
        writer.beginObject();
        writer.name("name").value("John");
        writer.name("age").value(30);
        writer.endObject();

        JsonElement expectedJson = gson.fromJson("{\"name\":\"John\",\"age\":30}", JsonElement.class);

        assertEquals(expectedJson, writer.get());
    }

    @Test
    public void testWriteJson_withWriter() throws IOException {
        writer.setWriter(stringWriter);

        writer.beginObject();
        writer.name("name").value("John");
        writer.name("age").value(30);
        writer.endObject();

        JsonElement expectedJson = gson.fromJson("{\"name\":\"John\",\"age\":30}", JsonElement.class);

        assertEquals(expectedJson, gson.fromJson(stringWriter.toString(), JsonElement.class));
    }
}