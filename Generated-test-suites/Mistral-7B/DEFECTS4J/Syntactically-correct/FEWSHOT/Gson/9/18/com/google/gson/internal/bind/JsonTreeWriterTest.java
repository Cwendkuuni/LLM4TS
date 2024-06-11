package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public class JsonTreeWriterTest {
    @Test
    public void testJsonTreeWriter() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement jsonElement = writer.get();
        assertNull(jsonElement);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement jsonElement = writer.get();
        assertEquals(JsonArray.class, jsonElement.getClass());
        assertNull(writer.peek());

        writer.endArray();
        jsonElement = writer.get();
        assertNull(jsonElement);
        assertSame(JsonPrimitive.class, writer.peek().getClass());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        writer.endArray();

        JsonElement jsonElement = writer.get();
        JsonArray jsonArray = (JsonArray) jsonElement;
        assertEquals(1, jsonArray.size());
        JsonArray innerJsonArray = jsonArray.getAsJsonArray();
        assertEquals(1, innerJsonArray.size());
        JsonPrimitive jsonPrimitive = innerJsonArray.getAsJsonPrimitive();
        assertEquals("foo", jsonPrimitive.getAsString());

        assertNull(writer.peek());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("John");
        writer.name("age");
        writer.value(30);
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals(2, jsonObject.entrySet().size());
        JsonPrimitive namePrimitive = jsonObject.get("name").getAsJsonPrimitive();
        assertEquals("John", namePrimitive.getAsString());
        JsonPrimitive agePrimitive = jsonObject.get("age").getAsJsonPrimitive();
        assertEquals(30, agePrimitive.getAsInt());

        assertNull(writer.peek());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("John");
        writer.name("age");
        writer.value(30);
        writer.endObject();
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
        assertEquals("{\"name\":\"John\",\"age\":30}", jsonPrimitive.getAsString());

        assertSame(JsonPrimitive.class, writer.peek().getClass());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("John");
        writer.name("age");
        writer.value(30);
        writer.name("gender");
        writer.value("Male");
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals(3, jsonObject.entrySet().size());
        JsonPrimitive namePrimitive = jsonObject.get("name").getAsJsonPrimitive();
        assertEquals("John", namePrimitive.getAsString());
        JsonPrimitive agePrimitive = jsonObject.get("age").getAsJsonPrimitive();
        assertEquals(30, agePrimitive.getAsInt());
        JsonPrimitive genderPrimitive = jsonObject.get("gender").getAsJsonPrimitive();
        assertEquals("Male", genderPrimitive.getAsString());

        assertNull(writer.peek());
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.value("John");
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("name").getAsJsonPrimitive();
        assertEquals("John", jsonPrimitive.getAsString());

        assertNull(writer.peek());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("isLoggedIn");
        writer.value(true);
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("isLoggedIn").getAsJsonPrimitive();
        assertEquals(true, jsonPrimitive.getAsBoolean());

        assertNull(writer.peek());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("pi");
        writer.value(3.1415);
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("pi").getAsJsonPrimitive();
        assertEquals(3.1415, jsonPrimitive.getAsDouble(), 0.0001);

        assertNull(writer.peek());
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("counter");
        writer.value(1000);
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("counter").getAsJsonPrimitive();
        assertEquals(1000L, jsonPrimitive.getAsLong());

        assertNull(writer.peek());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("number");
        writer.value(new Integer(123));
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("number").getAsJsonPrimitive();
        assertEquals(123, jsonPrimitive.getAsInt());

        assertNull(writer.peek());
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("isNull");
        writer.nullValue();
        writer.endObject();

        JsonElement jsonElement = writer.get();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.get("isNull");
        assertNull(jsonPrimitive.isJsonNull());

        assertNull(writer.peek());
    }

    @Test
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer1 = new OutputStreamWriter(outputStream);
        writer.setWriter(writer1);

        writer.beginArray();
        writer.beginObject();
        writer.name("name");
        writer.value("John");
        writer.name("age");
        writer.value(30);
        writer.endObject();
        writer.endArray();

        writer.flush();
        writer1.flush();

        String jsonString = outputStream.toString();
        assertEquals("[\n  {\n    \"name\":\"John\",\n    \"age\":30\n  }\n]", jsonString);

        assertNull(writer.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionBeginArrayAfterBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.beginArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionBeginObjectAfterBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionNameAfterBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionNameAfterBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        writer.name("age");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionValueAfterBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionValueAfterBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionEndArrayAfterBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionEndObjectAfterBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        writer.endObject();
    }
}