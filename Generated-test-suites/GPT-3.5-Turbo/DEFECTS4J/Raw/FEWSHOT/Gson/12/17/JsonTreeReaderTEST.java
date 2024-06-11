package com.google.gson.internal.bind;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.*;


public class JsonTreeReaderTest {
    private JsonElement jsonElement;

    @Before
    public void setup() {
        jsonElement = new JsonObject();
    }

    @Test
    public void testBeginAndEndArray() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testBeginAndEndObject() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginObject();
        assertTrue(reader.hasNext());
        reader.endObject();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextName() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginObject();
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.endObject();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextString() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextString();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextBoolean() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextBoolean();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextNull() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextNull();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextDouble() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextDouble();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextLong() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextLong();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testNextInt() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginArray();
        reader.nextInt();
        assertTrue(reader.hasNext());
        reader.endArray();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testSkipValue() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginObject();
        reader.skipValue();
        assertTrue(reader.hasNext());
        reader.endObject();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        reader.beginObject();
        reader.promoteNameToValue();
        assertTrue(reader.hasNext());
        reader.endObject();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testGetPath() throws Exception {
        JsonTreeReader reader = new JsonTreeReader(jsonElement);

        assertEquals("$", reader.getPath());
    }
}