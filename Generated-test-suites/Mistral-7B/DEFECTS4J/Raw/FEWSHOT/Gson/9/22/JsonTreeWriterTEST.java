package com.google.gson.internal.bind;

import static com.google.gson.JsonElement.NULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testJsonTreeWriter_empty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement root = writer.get();
        assertEquals(NULL, root);
    }

    @Test
    public void testJsonTreeWriter_singleValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("foo");
        JsonElement root = writer.get();
        assertEquals(new JsonPrimitive("foo"), root);
    }

    @Test
    public void testJsonTreeWriter_singleObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("bar").value("foo");
        writer.endObject();
        JsonElement root = writer.get();
        assertEquals(new JsonObject().addProperty("bar", "foo"), root);
    }

    @Test
    public void testJsonTreeWriter_singleArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        JsonElement root = writer.get();
        assertEquals(new JsonArray().add(new JsonPrimitive("foo")), root);
    }

    @Test
    public void testJsonTreeWriter_nestedObjects() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("outer").beginObject();
        writer.name("inner").value("foo");
        writer.endObject();
        writer.endObject();
        JsonElement root = writer.get();
        JsonObject outer = new JsonObject().add("outer", new JsonObject().addProperty("inner", "foo"));
        assertEquals(outer, root);
    }

    @Test
    public void testJsonTreeWriter_nestedArrays() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        writer.endArray();
        JsonElement root = writer.get();
        assertEquals(new JsonArray().add(new JsonArray().add(new JsonPrimitive("foo"))), root);
    }

    @Test
    public void testJsonTreeWriter_valueAfterName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("foo").value("bar");
        writer.endObject();
        JsonElement root = writer.get();
        assertEquals(new JsonObject().addProperty("foo", "bar"), root);
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_beginObjectAfterBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginObject();
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_endObjectAfterBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_nameAfterBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.name("foo");
    }

    @Test
    public void testJsonTreeWriter_nameWithoutValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("foo");
        writer.endObject();
        JsonElement root = writer.get();
        JsonObject expected = new JsonObject().addProperty("foo", null);
        assertEquals(expected, root);
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_nameAfterValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value("foo").name("bar");
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_nameAfterEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("foo").endObject();
        writer.name("bar");
    }

    @Test
    public void testJsonTreeWriter_nameAfterBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.name("foo");
        JsonElement root = writer.get();
        assertNull(root);
    }

    @Test
    public void testJsonTreeWriter_valueAfterBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        JsonElement root = writer.get();
        assertEquals(new JsonArray().add(new JsonPrimitive("foo")), root);
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonTreeWriter_endArrayAfterValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.endArray();
        writer.value("bar");
    }

    @Test
    public void testJsonTreeWriter_multipleValuesInArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("foo");
        writer.value("bar");
        writer.endArray();
        JsonElement root = writer.get();
        assertEquals(new JsonArray().add(new JsonPrimitive("foo")).add(new JsonPrimitive("bar")), root);
    }

    @Test
    public void testJsonTreeWriter_nameWithValueAndAnotherName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("outer").beginObject();
        writer.name("inner1").value("value1");
        writer.name("inner2").value("value2");
        writer.endObject();
        writer.endObject();
        JsonElement root = writer.get();
        JsonObject outer = new JsonObject().addProperty("outer", new JsonObject().addProperty("inner1", "value1")
                .addProperty("inner2", "value2"));
        assertEquals(outer, root);
    }

    @Test
    public void testJsonTreeWriter_getAfterClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        JsonElement root = writer.get();
        assertSame(JsonTreeWriter.SENTINEL_CLOSED, root);
    }
}