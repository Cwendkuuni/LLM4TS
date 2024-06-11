
package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private ObjectIdWriter objectIdWriter;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjectIdGenerator<?> objectIdGenerator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(objectIdGenerator);
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenTrueAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        when(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter)).thenReturn(true);
        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator, times(1)).writeObjectRef(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenFalseAndAlwaysAsIdTrue() throws IOException {
        writableObjectId.idWritten = false;
        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator, times(1)).writeObjectRef(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenTrueAndAlwaysAsIdFalse() throws IOException {
        projectId.idWritten = true;
        when(objectIdWriter.alwaysAsId()).thenReturn(false);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator, times(1)).writeFieldName(any(SerializableString.class));
        verify(objectIdWriter.serializer, times(1)).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenFalseAndAlwaysAsIdFalse() throws IOException {
        writableObjectId.idWritten = false;
        when(objectIdWriter.alwaysAsId()).thenReturn(false);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator, times(1)).writeFieldName(any(SerializableString.class));
        verify(objectIdWriter.serializer, times(1)).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        writableObjectId.id = null;
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> objectIdGenerator = writableObjectId.generator;
        Object forPojo = new Object();
        when(objectIdGenerator.generateId(forPojo)).thenReturn("123");
        assertEquals("123", writableObjectId.generateId(forPojo));
        assertEquals("123", writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
        verify(jsonGenerator, times(1)).writeObjectId(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrueAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
        verify(jsonGenerator, times(1)).writeObjectId(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndCanWriteObjectIdFalse() throws IOException {
        writableObjectId.idWritten = false;
        SerializableString propertyName = mock(SerializableString.class);
        when(objectIdWriter.propertyName()).thenReturn(propertyName);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
        verify(jsonGenerator, times(1)).writeFieldName(propertyName);
        verify(objectIdWriter.serializer, times(1)).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrueAndCanWriteObjectIdFalse() throws IOException {
        writableObjectId.idWritten = true;
        SerializableString propertyName = mock(SerializableString.class);
        when(objectIdWriter.propertyName()).thenReturn(propertyName);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
        verify(jsonGenerator, times(1)).writeFieldName(propertyName);
        verify(objectIdWriter.serializer, times(1)).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }
}
