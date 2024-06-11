package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

    private JsonTreeWriter writer;

    @Before
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    public void testGet_WhenEmptyJsonWriter_ReturnsJsonNull() {
        assertEquals(JsonNull.INSTANCE, writer.get());
    }

    @Test
    public void testBeginArray_WhenNoElementsAdded_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.beginArray());
    }

    @Test
    public void testEndArray_WhenCalledAfterBeginArray_ReturnsJsonTreeWriter() {
        writer.beginArray();
        assertEquals(writer, writer.endArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenNoMatchingBeginArray_ThrowsIllegalStateException() {
        writer.endArray();
    }

    @Test
    public void testBeginObject_WhenNoFieldsAdded_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.beginObject());
    }

    @Test
    public void testEndObject_WhenCalledAfterBeginObject_ReturnsJsonTreeWriter() {
        writer.beginObject();
        assertEquals(writer, writer.endObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenNoMatchingBeginObject_ThrowsIllegalStateException() {
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.name("name");
    }

    @Test
    public void testName_WhenCalledAfterBeginObject_ReturnsJsonTreeWriter() {
        writer.beginObject();
        assertEquals(writer, writer.name("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenCalledAfterValue_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.value("value");
        writer.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.name("name");
    }

    @Test
    public void testValue_String_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value("value"));
    }

    @Test
    public void testValue_Null_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value((String) null));
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.value("value");
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.value("value");
    }

    @Test
    public void testNullValue_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.nullValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullValue_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.nullValue();
    }

    @Test(expected = IllegalStateException.class)
    public void testNullValue_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.nullValue();
    }

    @Test
    public void testValue_Boolean_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value(true));
        assertEquals(writer, writer.value(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Boolean_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.value(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Boolean_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.value(true);
    }

    @Test
    public void testValue_Double_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value(1.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_WhenNaN_ThrowsIllegalArgumentException() {
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_WhenInfinite_ThrowsIllegalArgumentException() {
        writer.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Double_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.value(1.5);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Double_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.value(1.5);
    }

    @Test
    public void testValue_Long_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Long_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.value(10L);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Long_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.value(10L);
    }

    @Test
    public void testValue_Number_ReturnsJsonTreeWriter() {
        assertEquals(writer, writer.value(10));
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Number_WhenCalledBeforeBeginObject_ThrowsIllegalStateException() {
        writer.value(10);
    }

    @Test(expected = IllegalStateException.class)
    public void testValue_Number_WhenCalledAfterEndObject_ThrowsIllegalStateException() {
        writer.beginObject();
        writer.endObject();
        writer.value(10);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlush_ThrowsIOException() throws IOException {
        writer.flush();
    }

    @Test(expected = IOException.class)
    public void testClose_WhenIncompleteDocument_ThrowsIOException() throws IOException {
        writer.beginObject();
        writer.close();
    }

    @Test(expected = IOException.class)
    public void testClose_WhenCalledTwice_ThrowsIOException() throws IOException {
        writer.close();
        writer.close();
    }

    @Test
    public void testClose_WhenCompleteDocument_DoesNotThrowIOException() throws IOException {
        writer.close();
    }

    @Test
    public void testIsLenient_WhenDefault_ReturnsFalse() {
        assertFalse(writer.isLenient());
    }

    @Test
    public void testSetLenient_ChangesIsLenient() {
        writer.setLenient(true);
        assertTrue(writer.isLenient());
        writer.setLenient(false);
        assertFalse(writer.isLenient());
    }

    @Test
    public void testGetSerializeNulls_WhenDefault_ReturnsFalse() {
        assertFalse(writer.getSerializeNulls());
    }

    @Test
    public void testSetSerializeNulls_ChangesSerializeNulls() {
        writer.setSerializeNulls(true);
        assertTrue(writer.getSerializeNulls());
        writer.setSerializeNulls(false);
        assertFalse(writer.getSerializeNulls());
    }

}