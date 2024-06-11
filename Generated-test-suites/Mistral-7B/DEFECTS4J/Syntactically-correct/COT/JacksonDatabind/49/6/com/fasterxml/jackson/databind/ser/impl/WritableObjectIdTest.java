package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<?> objectIdGenerator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private TypeSerializer typeSerializer;

    @Before
    public void setup() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        typeSerializer = mock(TypeSerializer.class);
    }

    @Test
    public void testConstructors() {
        new WritableObjectId(objectIdGenerator);
    }

    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenOrAlwaysAsIdIsTrue() throws IOException {
        when(objectIdGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = 1L;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNull() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenJsonGeneratorCannotWriteObjectId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = "testId";

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() throws IOException {
        when(objectIdGenerator.generateId(any())).thenReturn("testId");

        Object actualId = new WritableObjectId(objectIdGenerator).generateId(null);

        assertNotNull(actualId);
        assertEquals("testId", actualId);
    }

    @Test(expected = JsonMappingException.class)
    public void testGenerateIdWhenNullObjectPassed() throws IOException {
        when(objectIdGenerator.generateId(any())).thenThrow(JsonMappingException.class);

        new WritableObjectId(objectIdGenerator).generateId(null);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdGenerator.generateId(any())).thenReturn("testId");
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectId("testId");
        verify(jsonGenerator, times(1)).writeFieldName("testPropertyName");
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldWhenJsonGeneratorCannotWriteObjectId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test
    public void testSetIdWrittenToTrue() {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        assertTrue(writableObjectId.idWritten);
    }
}