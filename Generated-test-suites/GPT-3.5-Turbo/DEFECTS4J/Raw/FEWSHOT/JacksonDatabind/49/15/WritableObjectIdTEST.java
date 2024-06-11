
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> objectIdGenerator;
   
    private WritableObjectId writableObjectId;
  
    private Object object;
    
    private JsonGenerator jsonGenerator;
  
    private SerializerProvider serializerProvider;
  
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setUp() throws Exception {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        object = mock(Object.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);

        writableObjectId = new WritableObjectId(objectIdGenerator);
    }

    @Test
    public void testWriteAsId_WithIdWritten_AlwaysAsIdTrue() throws IOException {
        when(objectIdWriter.alwaysAsId()).thenReturn(true);
        when(objectIdWriter.propertyName()).thenReturn(null);

        writableObjectId.id = object;
        writableObjectId.idWritten = true;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        verify(jsonGenerator).writeObjectRef(String.valueOf(object));
    }

    @Test
    public void testWriteAsId_WithIdWritten_AlwaysAsIdFalse() throws IOException {
        when(objectIdWriter.alwaysAsId()).thenReturn(false);
        when(objectIdWriter.propertyName()).thenThrow(new AssertionError());

        writableObjectId.id = object;
        writableObjectId.idWritten = true;

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_WithoutIdWritten_AlwaysAsIdTrue() throws IOException {
        when(objectIdWriter.alwaysAsId()).thenReturn(true);
        when(objectIdWriter.propertyName()).thenThrow(new AssertionError());

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_WithoutIdWritten_AlwaysAsIdFalse() throws IOException {
        when(objectIdWriter.alwaysAsId()).thenReturn(false);
        when(objectIdWriter.propertyName()).thenThrow(new AssertionError());

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() {
        when(objectIdGenerator.generateId(object)).thenReturn("123");

        assertEquals("123", writableObjectId.generateId(object));

        assertEquals("123", writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WithObjectIdWriter() throws IOException {
        SerializableString name = mock(SerializableString.class);

        when(objectIdWriter.propertyName()).thenReturn(name);

        writableObjectId.id = object;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(name);
        verify(objectIdWriter.serializer()).serialize(object, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_WithoutObjectIdWriter() throws IOException {
        writableObjectId.id = object;

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);

        verify(jsonGenerator).writeObjectId(String.valueOf(object));
    }

    @Test
    public void testWriteAsField_IdWrittenTrue() throws IOException {
        writableObjectId.id = object;
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName());
        verify(objectIdWriter.serializer()).serialize(object, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField_IdWrittenFalseNativeObjectIdSupported() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.id = object;
        writableObjectId.idWritten = false;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeObjectId(String.valueOf(object));
    }

    @Test
    public void testWriteAsField_IdWrittenFalseNativeObjectIdNotSupported() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId.id = object;
        writableObjectId.idWritten = false;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName());
        verify(objectIdWriter.serializer()).serialize(object, jsonGenerator, serializerProvider);
    }
}
