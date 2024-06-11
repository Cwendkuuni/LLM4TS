
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonWriter;

    @Before
    public void setUp() {
        jsonWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_WhenNoElementsInStack_ShouldReturnJsonNull() {
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = jsonWriter.get();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_WhenElementsInStack_ShouldThrowIllegalStateException() {
        JsonArray array = new JsonArray();
        jsonWriter.beginArray().value("value").endArray();
        jsonWriter.get();
    }
    
    @Test
    public void testBeginArray_WhenCalled_ShouldAddJsonArrayToStack() {
        jsonWriter.beginArray();
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonArray);
    }
    
    @Test
    public void testValue_WhenStringInput_ShouldAddJsonPrimitiveToString() {
        String expected = "value";
        jsonWriter.value(expected);
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertTrue(jsonPrimitive.isString());
        String actual = jsonPrimitive.getAsString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testValue_WhenBooleanInput_ShouldAddJsonPrimitiveToBoolean() {
        boolean expected = true;
        jsonWriter.value(expected);
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertTrue(jsonPrimitive.isBoolean());
        boolean actual = jsonPrimitive.getAsBoolean();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testValue_WhenDoubleInput_ShouldAddJsonPrimitiveToDouble() {
        double expected = 1.5;
        jsonWriter.value(expected);
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertTrue(jsonPrimitive.isNumber());
        double actual = jsonPrimitive.getAsDouble();
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testValue_WhenLongInput_ShouldAddJsonPrimitiveToLong() {
        long expected = 12345L;
        jsonWriter.value(expected);
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
        assertTrue(jsonPrimitive.isNumber());
        long actual = jsonPrimitive.getAsLong();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testValue_WhenNullInput_ShouldAddJsonNull() {
        jsonWriter.value((String) null);
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonNull);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValue_WhenNaNInputAndNotLenient_ShouldThrowIllegalArgumentException() {
        double nan = Double.NaN;
        jsonWriter.value(nan);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValue_WhenInfinityInputAndNotLenient_ShouldThrowIllegalArgumentException() {
        double infinity = Double.POSITIVE_INFINITY;
        jsonWriter.value(infinity);
    }
    
    @Test
    public void testBeginObject_WhenCalled_ShouldAddJsonObjectToStack() {
        jsonWriter.beginObject();
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonObject);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenNoElementsInStack_ShouldThrowIllegalStateException() {
        jsonWriter.endArray();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenPendingNameNotNull_ShouldThrowIllegalStateException() {
        jsonWriter.beginArray().name("name");
        jsonWriter.endArray();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenStackTopIsNotJsonArray_ShouldThrowIllegalStateException() {
        jsonWriter.beginObject().endArray();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenNoElementsInStack_ShouldThrowIllegalStateException() {
        jsonWriter.endObject();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenPendingNameNotNull_ShouldThrowIllegalStateException() {
        jsonWriter.beginObject().name("name");
        jsonWriter.endObject();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenStackTopIsNotJsonObject_ShouldThrowIllegalStateException() {
        jsonWriter.beginArray().endObject();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testName_WhenNoElementsInStack_ShouldThrowIllegalStateException() {
        jsonWriter.name("name");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testName_WhenStackTopIsNotJsonObject_ShouldThrowIllegalStateException() {
        jsonWriter.beginArray().name("name");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testName_WhenPendingNameNotNull_ShouldThrowIllegalStateException() {
        jsonWriter.beginObject().name("name").name("name2");
    }
    
    @Test
    public void testNullValue_WhenCalled_ShouldAddJsonNull() {
        jsonWriter.nullValue();
        JsonElement element = jsonWriter.peek();
        assertTrue(element instanceof JsonNull);
    }
    
    @Test
    public void testFlush_WhenCalled_ShouldNotThrowIOException() {
        try {
            jsonWriter.flush();
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test(expected = IOException.class)
    public void testClose_WhenCalled_ShouldThrowIOException() throws IOException {
        jsonWriter.close();
        jsonWriter.endObject();
    }
    
    @Test
    public void testClose_WhenCalled_ShouldAddSentinelClosedToJsonArray() {
        JsonArray array = new JsonArray();
        jsonWriter.beginArray();
        jsonWriter.close();
        JsonElement element = jsonWriter.peek();
        
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals("closed", element.getAsString());
    }
    
    @Test
    public void testClose_WhenCalled_ShouldAddSentinelClosedToJsonObject() {
        JsonObject object = new JsonObject();
        jsonWriter.beginObject();
        jsonWriter.close();
        JsonElement element = jsonWriter.peek();
        
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals("closed", element.getAsString());
    }
    
}
