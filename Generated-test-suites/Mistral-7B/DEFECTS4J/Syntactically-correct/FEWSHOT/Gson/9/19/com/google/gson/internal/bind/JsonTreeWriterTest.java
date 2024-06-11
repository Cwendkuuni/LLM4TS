package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("foo").value("bar");
        JsonElement result = writer.get();
        assertNotNull(result);
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("bar", jsonObject.get("foo").getAsString());
    }

    @Test
    public void testPeekAndPut() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement expectedValue1 = new JsonPrimitive("value1");
        JsonElement expectedValue2 = new JsonPrimitive("value2");

        writer.beginArray();
        writer.put(expectedValue1);
        writer.put(expectedValue2);
        JsonElement result = writer.get();

        List<JsonElement> expectedResult = new ArrayList<>();
        expectedResult.add(expectedValue1);
        expectedResult.add(expectedValue2);

        assertEquals(expectedResult, writer.getStack());
        assertEquals(expectedValue2, writer.peek());
        assertEquals(expectedValue1, writer.peek(1));
    }

    @Test
    public void testValueMethods() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("foo");
        JsonElement result = writer.get();
        assertEquals(new JsonPrimitive("foo"), result);

        writer.value(123);
        result = writer.get();
        assertEquals(new JsonPrimitive(123), result);

        writer.value(true);
        result = writer.get();
        assertEquals(new JsonPrimitive(true), result);

        writer.value(1.23);
        result = writer.get();
        assertEquals(new JsonPrimitive(1.23), result);

        writer.value(456L);
        result = writer.get();
        assertEquals(new JsonPrimitive(456), result);

        writer.value(new JsonPrimitive(789));
        result = writer.get();
        assertEquals(new JsonPrimitive(789), result);
    }

    @Test
    public void testBeginArrayAndEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("element1");
        writer.value(123);
        writer.endArray();
        JsonElement result = writer.get();

        JsonArray expectedResult = new JsonArray();
        expectedResult.add(new JsonPrimitive("element1"));
        expectedResult.add(new JsonPrimitive(123));

        assertEquals(expectedResult, result);
        assertEquals(0, writer.getStack().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayWithoutEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("element1");
        writer.value(123);
        writer.close();
    }

    @Test
    public void testBeginObjectAndEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name1").value("value1");
        writer.name("name2").value("value2");
        writer.endObject();

        JsonObject expectedResult = new JsonObject();
        expectedResult.addProperty("name1", "value1");
        expectedResult.addProperty("name2", "value2");

        JsonElement result = writer.get();
        assertEquals(expectedResult, result);
        assertEquals(0, writer.getStack().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectWithoutEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name1").value("value1");
        writer.close();
    }

    @Test
    public void testNullValue() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement result = writer.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.endObject();

        JsonObject expectedResult = new JsonObject();
        expectedResult.addProperty("name", JsonNull.INSTANCE);

        JsonElement result = writer.get();
        assertEquals(expectedResult, result);
        assertEquals(0, writer.getStack().size());
    }
}