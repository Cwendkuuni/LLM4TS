package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Tests for {@link WritableObjectId} class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> mockObjectIdGenerator;
    private ObjectIdWriter mockObjectIdWriter;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        mockObjectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        mockObjectIdWriter = Mockito.mock(ObjectIdWriter.class);
        mockJsonGenerator = Mockito.mock(JsonGenerator.class);
        mockSerializerProvider = Mockito.mock(SerializerProvider.class);
        writableObjectId = new WritableObjectId(mockObjectIdGenerator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        // Given
        when(mockObjectIdGenerator.isEnabled()).thenReturn(true);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockJsonGenerator.writeObjectId(Mockito.anyString())).thenAnswer(invocation -> {
            ObjectIdWriter objectIdWriter = invocation.getArgument(0);
            SerializableString serializableString = new SerializableString("testId");
            objectIdWriter.propertyName = serializableString;
            return serializableString.toString();
        });

        // When
        boolean result = writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        // Then
        assertTrue(result);
        verify(mockJsonGenerator).writeObjectId(Mockito.anyString());
    }

    @Test
    public void testWriteAsIdWhenIdNullAndWriterAlwaysAsIdFalse() throws IOException {
        // Given
        when(mockObjectIdGenerator.isEnabled()).thenReturn(true);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockJsonGenerator.writeObjectRef(Mockito.anyString())).thenAnswer(invocation -> {
            JsonGenerator jsonGenerator = invocation.getArgument(0);
            SerializableString serializableString = new SerializableString("testId");
            jsonGenerator.writeObjectId(serializableString.toString());
            return null;
        });

        // When
        writableObjectId.id = null;
        boolean result = writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        // Then
        assertFalse(result);
        verify(mockJsonGenerator, never()).writeObjectRef(Mockito.anyString());
    }

    @Test
    public void testWriteAsIdWhenIdNullAndWriterAlwaysAsIdTrue() throws IOException {
        // Given
        when(mockObjectIdGenerator.isEnabled()).thenReturn(true);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockObjectIdWriter.alwaysAsId).thenReturn(true);

        // When
        writableObjectId.id = null;
        boolean result = writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        // Then
        assertTrue(result);
        verify(mockJsonGenerator).writeNull();
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWhenIdGeneratorDisabled() throws IOException {
        // Given
        when(mockObjectIdGenerator.isEnabled()).thenReturn(false);

        // When
        writableObjectId.id = new Object();
        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        // Then
        fail("Expected JsonMappingException");
    }

    @Test
    public void testGenerateId() {
        // Given
        when(mockObjectIdGenerator.generateId(Mockito.any())).thenReturn("testId");

        // When
        Object id = writableObjectId.generateId(new Object());

        // Then
        assertEquals("testId", id);
        verify(mockObjectIdGenerator).generateId(Mockito.any());
    }

    @Test
    public void testWriteAsField() throws IOException {
        // Given
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

        // When
        String propertyName = "testPropertyName";
        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        // Then
        verify(mockJsonGenerator).writeFieldName(propertyName);
        verify(mockObjectIdWriter.serializer).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
}