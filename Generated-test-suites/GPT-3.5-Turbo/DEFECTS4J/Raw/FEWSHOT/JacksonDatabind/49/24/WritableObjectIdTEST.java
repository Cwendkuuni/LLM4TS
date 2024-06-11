package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private WritableObjectId objectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private ObjectIdGenerator<?> objectIdGenerator;

    @Before
    public void setup() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        objectId = new WritableObjectId(objectIdGenerator);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsIdWhenIdIsNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrue() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNullAndIdWrittenIsTrueAndAlwaysAsIdIsFalse() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        objectId.idWritten = true;
        assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNullAndIdWrittenIsTrueAndAlwaysAsIdIsTrue() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        objectId.idWritten = true;
        assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        objectId.id = new Object();
        assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrue() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        objectId.id = new Object();
        assertTrue(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).canWriteObjectId();
    }

    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsFalse() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        objectId.idWritten = true;
        objectId.id = new Object();
        assertTrue(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).canWriteObjectId();
    }

    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsTrue() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        objectId.idWritten = true;
        objectId.id = new Object();
        assertTrue(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).canWriteObjectId();
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        when(objectIdGenerator.generateId(forPojo)).thenReturn(generatedId);
        assertEquals(generatedId, objectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        when(objectIdWriter.propertyName).thenReturn(mock(SerializableString.class));
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(objectId.idWritten);
        verify(jsonGenerator, times(0)).canWriteObjectId();
    }

    @Test
    public void testWriteAsFieldWhenObjectIdCanBeWritten() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.propertyName).thenReturn(mock(SerializableString.class));
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(objectId.idWritten);
        verify(jsonGenerator).canWriteObjectId();
    }

    @Test
    public void testWriteAsFieldWhenObjectIdCannotBeWritten() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(mock(SerializableString.class));
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(objectId.idWritten);
        verify(jsonGenerator).writeFieldName(any(SerializableString.class));
    }
}