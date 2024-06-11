package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;

import org.junit.Test;

public class WritableObjectIdTest {
    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);

        when(mockGenerator.generateId(mock(Object.class))).thenReturn("generatedId");
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);

        assertNotNull(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        assertEquals("generatedId", mockJsonGenerator.getOutputBuffer().toString());

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockObjectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));

        writableObjectId.id = "originalId";
        assertNotNull(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        assertEquals("testPropertyName", mockJsonGenerator.getCurrentName());
        assertEquals("originalId", mockJsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testGenerateId() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        Object mockPojo = mock(Object.class);

        when(mockGenerator.generateId(mockPojo)).thenReturn("generatedId");

        Object result = writableObjectId.generateId(mockPojo);
        assertEquals("generatedId", result);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.id = "testId";
        when(mockObjectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        when(mockSerializer.serialize(writableObjectId.id, mockJsonGenerator, mockSerializerProvider)).thenReturn(null);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);

        assertEquals("testPropertyName", mockJsonGenerator.getCurrentName());
        assertEquals("testId", mockJsonGenerator.getOutputBuffer().toString());

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockObjectIdWriter.propertyName).thenReturn(null);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertNull(mockJsonGenerator.getCurrentName());
    }
}