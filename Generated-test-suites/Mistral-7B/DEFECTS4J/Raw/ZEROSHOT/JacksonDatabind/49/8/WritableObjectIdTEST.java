package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private WritableObjectId writableObjectId;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private ObjectIdWriter objectIdWriter;
    private StdSerializer<Object> objectSerializer;
    private TypeSerializer typeSerializer;

    @Before
    public void setUp() throws Exception {
        idGenerator = mock(IdIntGenerator.class);
        when(idGenerator.generateId(any())).thenReturn(1L);

        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectId(anyString())).thenReturn(jsonGenerator);
        when(jsonGenerator.writeObjectRef(anyString())).thenReturn(jsonGenerator);

        objectIdWriter = mock(ObjectIdWriter.class);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        when(objectIdWriter.alwaysAsId).thenReturn(false);

        objectSerializer = mock(StdSerializer.class);
        when(objectSerializer.serialize(any(), any(), any())).thenReturn("testObjectSerialized");

        typeSerializer = mock(TypeSerializer.class);

        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsIdWithIdAndIdWrittenFalse() throws IOException {
        // Given
        when(serializerProvider.getSerializerProvider().findValueSerializer(any())).thenReturn(objectSerializer);
        when(serializerProvider.getTypeSerializer()).thenReturn(typeSerializer);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Then
        assertFalse(result);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsIdWithIdAndIdWrittenTrue() throws IOException {
        // Given
        Object testObject = new Object();
        writableObjectId.generateId(testObject);
        when(serializerProvider.getSerializerProvider().findValueSerializer(any())).thenReturn(objectSerializer);
        when(serializerProvider.getTypeSerializer()).thenReturn(typeSerializer);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Then
        assertTrue(result);
        assertSame(1L, (Long) writableObjectId.id);
    }

    @Test
    public void testWriteAsIdWithNoId() throws IOException {
        // Given
        when(serializerProvider.getSerializerProvider().findValueSerializer(any())).thenReturn(objectSerializer);
        when(serializerProvider.getTypeSerializer()).thenReturn(typeSerializer);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        // Then
        assertFalse(result);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testGenerateId() throws IOException {
        // Given
        Object testObject = new Object();

        // When
        Object result = writableObjectId.generateId(testObject);

        // Then
        assertSame(1L, result);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalse() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectId(anyString())).thenReturn(jsonGenerator);

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Then
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() throws IOException {
        // Given
        Object testObject = new Object();
        writableObjectId.generateId(testObject);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Then
        assertTrue(writableObjectId.idWritten);
        assertSame(1L, (Long) writableObjectId.id);
    }

}