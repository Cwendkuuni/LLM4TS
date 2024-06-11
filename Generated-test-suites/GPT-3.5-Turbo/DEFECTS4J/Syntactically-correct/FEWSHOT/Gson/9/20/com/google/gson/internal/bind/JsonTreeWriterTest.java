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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet_EmptyStack_ReturnsJsonNull() {
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_NonEmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.beginObject().endObject();
        jsonTreeWriter.get();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        assertEquals(1, jsonTreeWriter.get().getAsJsonArray().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_EmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_PendingName_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject().name("key").endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_InvalidElementType_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginArray().beginArray().endArray();
    }
    
    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        assertEquals(1, jsonTreeWriter.get().getAsJsonObject().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_EmptyStack_ThrowsIllegalStateException() {
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_PendingName_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginArray().endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_InvalidElementType_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject().beginObject().endObject();
    }

    @Test
    public void testName_ValidState_SetPendingName() throws IOException {
        jsonTreeWriter.beginObject().name("key");
        assertEquals("key", jsonTreeWriter.pendingName);
    }

    @Test(expected = IllegalStateException.class)
    public void testName_EmptyStack_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.name("key");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_PendingName_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginObject().name("key").name("key2");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_InvalidElementType_ThrowsIllegalStateException() throws IOException {
        jsonTreeWriter.beginArray().name("key");
    }

    @Test
    public void testValue_StringValue_AddsJsonPrimitiveToStack() throws IOException {
        jsonTreeWriter.value("value");
        assertEquals("value", jsonTreeWriter.peek().getAsString());
    }

    @Test
    public void testValue_NullValue_AddsJsonNullToStack() throws IOException {
        jsonTreeWriter.value((String) null);
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
    }

    @Test
    public void testNullValue() throws IOException {
        jsonTreeWriter.nullValue();
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
    }

    @Test
    public void testValue_BooleanValue_AddsJsonPrimitiveToStack() throws IOException {
        jsonTreeWriter.value(true);
        assertEquals(true, jsonTreeWriter.peek().getAsBoolean());
    }

    @Test
    public void testValue_DoubleValue_AddsJsonPrimitiveToStack() throws IOException {
        jsonTreeWriter.value(3.14);
        assertEquals(3.14, jsonTreeWriter.peek().getAsDouble(), 0.0);
    }

    @Test
    public void testValue_LongValue_AddsJsonPrimitiveToStack() throws IOException {
        jsonTreeWriter.value(12345L);
        assertEquals(12345L, jsonTreeWriter.peek().getAsLong());
    }

    @Test
    public void testValue_NumberValue_AddsJsonPrimitiveToStack() throws IOException {
        jsonTreeWriter.value(3.14);
        assertEquals(3.14, jsonTreeWriter.peek().getAsDouble(), 0.0);
    }

    @Test
    public void testValue_NumberValue_NullValue_AddsJsonNullToStack() throws IOException {
        jsonTreeWriter.value((Number) null);
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_DoubleValue_NonLenientMode_ThrowsIllegalArgumentException() throws IOException {
        jsonTreeWriter.setLenient(false);
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlush() throws IOException {
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_IncompleteDocument_ThrowsIOException() throws IOException {
        jsonTreeWriter.beginObject().close();
    }

    @Test
    public void testClose_SetStackToSentinelClosed() throws IOException {
        jsonTreeWriter.close();
        assertEquals(1, jsonTreeWriter.stack.size());
        assertEquals(JsonPrimitive.class, jsonTreeWriter.stack.get(0).getClass());
        assertEquals("closed", jsonTreeWriter.stack.get(0).getAsString());
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriter_Write_ThrowsAssertionError() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.write(new char[10], 0, 5);
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriter_Flush_ThrowsAssertionError() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.flush();
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriter_Close_ThrowsAssertionError() throws IOException {
        Writer unwritableWriter = JsonTreeWriter.UNWRITABLE_WRITER;
        unwritableWriter.close();
    }

    @Test
    public void testGetSerializeNulls_InitiallyFalse() {
        assertFalse(jsonTreeWriter.getSerializeNulls());
    }

    @Test
    public void testSetSerializeNulls() {
        jsonTreeWriter.setSerializeNulls(true);
        assertTrue(jsonTreeWriter.getSerializeNulls());
    }
}