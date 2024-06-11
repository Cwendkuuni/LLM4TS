package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTestBase;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest extends ObjectIdGeneratorTestBase {

    private ObjectIdGenerator<?> objectIdGeneratorMock;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private ObjectIdWriter objectIdWriterMock;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        objectIdGeneratorMock = mock(ObjectIdGenerator.class);
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        objectIdWriterMock = mock(ObjectIdWriter.class);
        when(objectIdGeneratorMock.generateId(any())).thenReturn("generatedId");
        writableObjectId = new WritableObjectId(objectIdGeneratorMock);
    }

    @Test
    public void testWriteAsIdSuccess() throws IOException {
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);
        when(jsonGeneratorMock.writeObjectRef(anyString())).thenReturn(writableObjectId);
        when(objectIdWriterMock.alwaysAsId).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));
        assertNotNull(writableObjectId.getId());
        verify(jsonGeneratorMock).writeObjectRef(eq("generatedId"));
    }

    @Test
    public void testWriteAsIdFailure() throws IOException {
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        when(jsonGeneratorMock.getSerializerProvider()).thenReturn(serializerProviderMock);
        when(jsonSerializerMock.isEnabled(anyString())).thenReturn(true);
        when(serializerProviderMock.findValueSerializer(any())).thenReturn(jsonSerializerMock);

        when(objectIdGeneratorMock.generateId(any())).thenReturn("generatedId");
        when(jsonGeneratorMock.writeFieldName(anyString())).thenReturn(writableObjectId);
        when(jsonSerializerMock.serialize(any(), eq(jsonGeneratorMock), eq(serializerProviderMock))).thenReturn(writableObjectId);

        assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));
        assertNotNull(writableObjectId.getId());
        verify(jsonGeneratorMock).writeFieldName(eq("propertyName"));
        verify(jsonSerializerMock).serialize(eq("generatedId"), eq(jsonGeneratorMock), eq(serializerProviderMock));
    }

    @Test
    public void testGenerateId() throws IOException {
        Object forPojo = new Object();

        Object generatedId = writableObjectId.generateId(forPojo);
        assertNotNull(generatedId);
        assertEquals("generatedId", generatedId);
        verify(objectIdGeneratorMock).generateId(eq(forPojo));
    }

    @Test
    public void testWriteAsFieldSuccess() throws IOException {
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);
        when(objectIdWriterMock.propertyName).thenReturn(new SerializableString("propertyName"));

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);
        verify(jsonGeneratorMock).writeFieldName(eq("propertyName"));
        verify(serializerProviderMock).setSerializationShallowEnabled(eq(false));
        verify(objectIdWriterMock.serializer).serialize(eq("generatedId"), eq(jsonGeneratorMock), eq(serializerProviderMock));
    }

    @Test
    public void testWriteAsFieldFailure() throws IOException {
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);
        when(objectIdWriterMock.propertyName).thenReturn(null);

        try {
            writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        verify(jsonGeneratorMock, never()).writeFieldName(anyString());
        verify(objectIdWriterMock, never()).serializer;
    }
}