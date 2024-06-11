package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.beginArray();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.beginObject();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.endObject();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        assertTrue(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        jsonTreeReader.nextNull();
        assertFalse(jsonTreeReader.hasNext());
    }

    // Add more test cases for other methods as needed

}