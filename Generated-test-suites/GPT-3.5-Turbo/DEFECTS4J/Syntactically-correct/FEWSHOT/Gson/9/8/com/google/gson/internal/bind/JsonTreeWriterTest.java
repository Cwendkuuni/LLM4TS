package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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

    @Before
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    public void testGet_WhenNoElementsAdded_ShouldReturnJsonNull() {
        JsonElement result = writer.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testBeginObject_WhenInvoked_ShouldAddJsonObjectToStack() throws IOException {
        writer.beginObject();
        JsonObject result = (JsonObject) writer.peek();
        assertEquals(JsonObject.class, result.getClass());
    }

    @Test
    public void testEndObject_WhenStackContainsJsonObject_ShouldRemoveJsonObjectFromStack() throws IOException {
        writer.beginObject();
        writer.endObject();
        JsonElement result = writer.peek();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testEndObject_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws IOException {
        assertThrows(IllegalStateException.class, () -> {
            writer.endObject();
        });
    }

    @Test
    public void testEndObject_WhenPendingNameIsNotNull_ShouldThrowIllegalStateException() throws IOException {
        writer.beginObject();
        writer.name("key");
        assertThrows(IllegalStateException.class, () -> {
            writer.endObject();
        });
    }

    @Test
    public void testBeginArray_WhenInvoked_ShouldAddJsonArrayToStack() throws IOException {
        writer.beginArray();
        JsonArray result = (JsonArray) writer.peek();
        assertEquals(JsonArray.class, result.getClass());
    }

    @Test
    public void testEndArray_WhenStackContainsJsonArray_ShouldRemoveJsonArrayFromStack() throws IOException {
        writer.beginArray();
        writer.endArray();
        JsonElement result = writer.peek();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testEndArray_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws IOException {
        assertThrows(IllegalStateException.class, () -> {
            writer.endArray();
        });
    }

    @Test
    public void testEndArray_WhenPendingNameIsNotNull_ShouldThrowIllegalStateException() throws IOException {
        writer.beginArray();
        writer.name("key");
        assertThrows(IllegalStateException.class, () -> {
            writer.endArray();
        });
    }

    @Test
    public void testName_WhenInvokedOnEmptyStack_ShouldThrowIllegalStateException() throws IOException {
        assertThrows(IllegalStateException.class, () -> {
            writer.name("key");
        });
    }

    @Test
    public void testName_WhenInvokedOnNonJsonObject_ShouldThrowIllegalStateException() throws IOException {
        writer.beginArray();
        assertThrows(IllegalStateException.class, () -> {
            writer.name("key");
        });
    }

    @Test
    public void testValue_WhenValueIsNull_ShouldAddJsonNullToStack() throws IOException {
        writer.value((String) null);
        JsonElement result = writer.peek();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testValue_WhenInvoked_ShouldAddJsonPrimitiveToStack() throws IOException {
        writer.value("value");
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals("value", result.getAsString());
    }

    @Test
    public void testNullValue_WhenInvoked_ShouldAddJsonNullToStack() throws IOException {
        writer.nullValue();
        JsonElement result = writer.peek();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testValue_WhenValueIsBoolean_ShouldAddJsonPrimitiveToStack() throws IOException {
        writer.value(true);
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValue_WhenValueIsDouble_ShouldAddJsonPrimitiveToStack() throws IOException {
        writer.value(3.14);
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(3.14, result.getAsDouble(), 0.0);
    }

    @Test
    public void testValue_WhenValueIsLong_ShouldAddJsonPrimitiveToStack() throws IOException {
        writer.value(123456789L);
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(123456789, result.getAsLong());
    }

    @Test
    public void testValue_WhenValueIsNumber_ShouldAddJsonPrimitiveToStack() throws IOException {
        writer.value(3.14);
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals(3.14, result.getAsDouble(), 0.0);
    }

    @Test
    public void testFlush_WhenInvoked_ShouldNotThrowException() throws IOException {
        writer.flush();
    }

    @Test
    public void testClose_WhenInvoked_ShouldAddSentinelClosedToStack() throws IOException {
        writer.close();
        JsonPrimitive result = (JsonPrimitive) writer.peek();
        assertEquals(JsonPrimitive.class, result.getClass());
        assertEquals("closed", result.getAsString());
    }

    @Test
    public void testClose_WhenStackIsNotEmpty_ShouldThrowIOException() throws IOException {
        writer.beginObject();
        assertThrows(IOException.class, () -> {
            writer.close();
        });
    }

    @Test
    public void testGet_WhenMultipleElementsAdded_ShouldReturnExpectedJsonElement() throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value("John Doe");
        writer.name("age");
        writer.value(30);
        writer.endObject();

        JsonObject result = (JsonObject) writer.get();
        assertEquals(JsonObject.class, result.getClass());
        assertEquals("John Doe", result.get("name").getAsString());
        assertEquals(30, result.get("age").getAsInt());
    }
}