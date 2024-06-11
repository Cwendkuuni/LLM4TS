package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeWriter.beginArray();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonArray.class, jsonElement.getClass());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenStackIsEmpty() throws IOException {
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenPendingNameIsNotNull() throws IOException {
        jsonTreeWriter.beginObject().name("test");
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWhenTopOfStackIsNotJsonArray() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeWriter.beginObject();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenStackIsEmpty() throws IOException {
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenPendingNameIsNotNull() throws IOException {
        jsonTreeWriter.beginArray().value("test");
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWhenTopOfStackIsNotJsonObject() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectWhenPendingNameIsNotNull() throws IOException {
        jsonTreeWriter.beginObject().name("test");
        jsonTreeWriter.beginObject();
    }

    @Test
    public void testName() throws IOException {
        jsonTreeWriter.beginObject().name("test");
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());

        JsonObject jsonObject = (JsonObject) jsonElement;
        assertEquals("test", jsonObject.entrySet().iterator().next().getKey());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenStackIsEmpty() throws IOException {
        jsonTreeWriter.name("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenPendingNameIsNotNull() throws IOException {
        jsonTreeWriter.beginArray().value("test");
        jsonTreeWriter.name("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWhenTopOfStackIsNotJsonObject() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.name("test");
    }

    @Test
    public void testValueString() throws IOException {
        jsonTreeWriter.beginObject().name("test").value("value");
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());

        JsonObject jsonObject = (JsonObject) jsonElement;
        assertEquals("value", jsonObject.entrySet().iterator().next().getValue().getAsString());
    }

    @Test
    public void testValueNull() throws IOException {
        jsonTreeWriter.beginArray().nullValue();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonArray.class, jsonElement.getClass());

        JsonArray jsonArray = (JsonArray) jsonElement;
        assertEquals(JsonNull.INSTANCE, jsonArray.get(0));
    }

    @Test
    public void testValueBoolean() throws IOException {
        jsonTreeWriter.beginObject().name("test").value(true);
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());

        JsonObject jsonObject = (JsonObject) jsonElement;
        assertEquals(true, jsonObject.entrySet().iterator().next().getValue().getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        jsonTreeWriter.beginArray().value(3.14);
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonArray.class, jsonElement.getClass());

        JsonArray jsonArray = (JsonArray) jsonElement;
        assertEquals(3.14, jsonArray.get(0).getAsDouble(), 0);
    }

    @Test
    public void testValueLong() throws IOException {
        jsonTreeWriter.beginObject().name("test").value(42);
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());

        JsonObject jsonObject = (JsonObject) jsonElement;
        assertEquals(42, jsonObject.entrySet().iterator().next().getValue().getAsLong());
    }

    @Test
    public void testValueNumber() throws IOException {
        jsonTreeWriter.beginArray().value(123);
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonArray.class, jsonElement.getClass());

        JsonArray jsonArray = (JsonArray) jsonElement;
        assertEquals(123, jsonArray.get(0).getAsInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWhenNonLenientAndValueIsNaN() throws IOException {
        jsonTreeWriter.setLenient(false).beginObject().name("test").value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWhenNonLenientAndValueIsInfinite() throws IOException {
        jsonTreeWriter.setLenient(false).beginArray().value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFlush() throws IOException {
        StringWriter writer = new StringWriter();
        jsonTreeWriter.beginObject().name("test").value("value");
        jsonTreeWriter.flush();

        JsonElement jsonElement = jsonTreeWriter.get();
        jsonElement.writeTo(writer);

        assertEquals("{\"test\":\"value\"}", writer.toString());
    }

    @Test(expected = IOException.class)
    public void testCloseWhenStackIsNotEmpty() throws IOException {
        jsonTreeWriter.beginObject().name("test");
        jsonTreeWriter.close();
    }

    @Test
    public void testCloseWhenStackIsEmpty() throws IOException {
        jsonTreeWriter.close();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test
    public void testGet() throws IOException {
        jsonTreeWriter.beginObject().name("test").value("value");
        JsonElement jsonElement = jsonTreeWriter.get();
        assertEquals(JsonObject.class, jsonElement.getClass());

        StringWriter writer = new StringWriter();
        jsonElement.writeTo(writer);

        assertEquals("{\"test\":\"value\"}", writer.toString());
    }
}