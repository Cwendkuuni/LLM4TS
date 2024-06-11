package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private StdSerializer<Object> objectSerializer;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setUp() throws Exception {
        idGenerator = mock(IdIdGenerator.class);
        objectMapper = new ObjectMapper();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectSerializer = mock(StdSerializer.class);
        objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);
    }

    @Test
    public void testConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsId_NullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_NullId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        writableObjectId.id = null;
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_IdWrittenAndAlwaysAsIdFalse() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);
        objectIdWriter.alwaysAsId = false;

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator, never()).writeObjectId(anyString());
        verify(objectSerializer, never()).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsId_IdWrittenAndAlwaysAsIdTrue() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);
        objectIdWriter.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectId(eq("1"));
        verify(objectSerializer, never()).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsId_CannotWriteObjectId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(objectSerializer).serialize(eq(1L), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testGenerateId_NullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId_IdGenerated() {
        when(idGenerator.generateId(any())).thenReturn(1L);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        Object id = writableObjectId.generateId(new Object());
        assertNotNull(id);
        assertEquals(1L, id);
    }

    @Test
    public void testWriteAsField_NullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator, never()).writeFieldName(anyString());
        verify(objectSerializer, never()).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsField_NullId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        SerializableString name = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(name);

        writableObjectId.id = null;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator, never()).writeFieldName(anyString());
        verify(objectSerializer, never()).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsField_IdWritten() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);

        SerializableString name = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(name);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeFieldName(eq("id"));
        verify(objectSerializer).serialize(eq(1L), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsField_CannotWriteObjectId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(1L);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, objectMapper);

        SerializableString name = mock(SerializableString.class);
        when(objectIdWriter.propertyName).thenReturn(name);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(objectSerializer).serialize(eq(1L), eq(jsonGenerator), eq(serializerProvider));
    }
}