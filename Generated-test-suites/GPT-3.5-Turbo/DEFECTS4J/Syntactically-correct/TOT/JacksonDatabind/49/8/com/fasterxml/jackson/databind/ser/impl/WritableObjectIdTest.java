
package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;
    private Object id;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter writer;

    @Before
    public void setup() {
        generator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(generator);
        id = new Object();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        writer = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsId_withObjectAndIdWrittenAndAlwaysAsId_shouldReturnTrue() throws IOException {
        writableObjectId.id = id;
        writableObjectId.idWritten = true;
        when(writer.alwaysAsId).thenReturn(true);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);

        assertTrue(result);
        verify(jsonGenerator, times(1)).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_withObjectAndIdWrittenAndNotAlwaysAsIdButCanWriteObjectId_shouldReturnTrue() throws IOException {
        writableObjectId.id = id;
        writableObjectId.idWritten = true;
        when(writer.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);

        assertTrue(result);
        verify(jsonGenerator, times(1)).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_withObjectAndIdWrittenAndNotAlwaysAsIdAndCannotWriteObjectId_shouldReturnFalse() throws IOException {
        writableObjectId.id = id;
        writableObjectId.idWritten = true;
        when(writer.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);

        assertFalse(result);
        verify(writer.serializer, times(1)).serialize(eq(id), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsId_withObjectAndIdNotWrittenAndNotAlwaysAsId_shouldReturnFalse() throws IOException {
        writableObjectId.id = id;
        writableObjectId.idWritten = false;
        when(writer.alwaysAsId).thenReturn(false);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);

        assertFalse(result);
        verify(writer.serializer, times(1)).serialize(eq(id), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testWriteAsId_withNullId_shouldReturnFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        when(writer.alwaysAsId).thenReturn(false);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);

        assertFalse(result);
        verify(jsonGenerator, never()).writeObjectRef(anyString());
        verify(writer.serializer, never()).serialize(any(), any(), any());
    }

    @Test
    public void testGenerateId_withNonNullId_shouldReturnId() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        when(generator.generateId(forPojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(forPojo);

        assertEquals(generatedId, result);
        assertEquals(generatedId, writableObjectId.id);
        verifyZeroInteractions(jsonGenerator, serializerProvider, writer);
    }

    @Test
    public void testGenerateId_withNullId_shouldReturnIdFromGenerator() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        writableObjectId.id = id;
        when(generator.generateId(forPojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(forPojo);

        assertEquals(generatedId, result);
        assertEquals(generatedId, writableObjectId.id);
        verifyZeroInteractions(jsonGenerator, serializerProvider, writer);
    }

    @Test
    public void testWriteAsField_withNativeObjectIdSupported_shouldWriteNativeObjectId() throws IOException {
        writableObjectId.id = id;
        jsonGenerator = JsonMapper.builder().build().getFactory().createGenerator(System.out);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, writer);

        verify(jsonGenerator, times(1)).writeObjectId(eq(String.valueOf(id)));
        verify(writer.serializer, never()).serialize(any(), any(), any());
    }

    @Test
    public void testWriteAsField_withNativeObjectIdNotSupported_shouldWriteFieldWithPropertyName() throws IOException {
        writableObjectId.id = id;
        jsonGenerator = JsonMapper.builder().build().getFactory().createGenerator(System.out);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName).thenReturn(mock(SerializableString.class));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, writer);

        verify(jsonGenerator, never()).writeObjectId(anyString());
        verify(writer.serializer, times(1)).serialize(eq(id), eq(jsonGenerator), eq(serializerProvider));
        verify(jsonGenerator, times(1)).writeFieldName(any(SerializableString.class));
    }

    @Test
    public void testWriteAsField_withNullId_shouldNotWriteField() throws IOException {
        writableObjectId.id = null;
        jsonGenerator = JsonMapper.builder().build().getFactory().createGenerator(System.out);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, writer);

        verify(jsonGenerator, never()).writeObjectId(anyString());
        verify(writer.serializer, never()).serialize(any(), any(), any());
        verify(jsonGenerator, never()).writeFieldName(any(SerializableString.class));
    }
}

