
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> mockGenerator;
    private WritableObjectId writableObjectId;
    private JsonGenerator mockGenerator;
    private SerializerProvider mockProvider;
    private ObjectIdWriter mockWriter;

    @Before
    public void setup() {
        mockGenerator = mock(JsonGenerator.class);
        mockProvider = mock(SerializerProvider.class);
        mockWriter = mock(ObjectIdWriter.class);
        mockGenerator = mock(JsonGenerator.class);
        mockSingletonSerializer = mock(StdSerializer.class);

        writableObjectId = new WritableObjectId(mockGenerator);
    }

    @Test
    public void testWriteAsId_WhenIdNullAndIdWrittenFalse_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        writableObjectId.id = null;
        writableObjectId.idWritten = false;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNullAndIdWrittenTrue_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        writableObjectId.id = null;
        writableObjectId.idWritten = true;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalseAndCanWriteObjectIdFalse_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalseAndCanWriteObjectIdTrue_CallsWriteObjectRef() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator).writeObjectRef("123");
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdFalseAndCanWriteObjectIdFalse_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrueAndAlwaysAsIdFalseAndCanWriteObjectIdTrue_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(false);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrueAndCanWriteObjectIdFalse_ReturnsFalse() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        boolean actual = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        assertFalse(actual);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrueAndCanWriteObjectIdTrue_CallsWriteObjectRef() throws IOException {
        when(mockWriter.alwaysAsId()).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator).writeObjectRef("123");
    }

    @Test
    public void testGenerateId_WhenIdNotNull_ReturnsId() {
        Object forPojo = new Object();
        writableObjectId.id = "123";

        Object actual = writableObjectId.generateId(forPojo);

        assertEquals("123", actual);
    }

    @Test
    public void testGenerateId_WhenIdNull_CallsGeneratorAndReturnsGeneratedId() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        when(mockGenerator.generateId(forPojo)).thenReturn(generatedId);

        Object actual = writableObjectId.generateId(forPojo);

        assertEquals(generatedId, actual);
        assertEquals(generatedId, writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdTrue_CallsWriteObjectId() throws IOException {
        when(mockGenerator.canWriteObjectId()).thenReturn(true);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator).writeObjectId("123");
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdFalse_CallsWriteFieldNameAndSerialize() throws IOException {
        when(mockGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString mockPropertyName = mock(SerializableString.class);
        when(mockWriter.propertyName()).thenReturn(mockPropertyName);
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator).writeFieldName(mockPropertyName);
        verify(mockWriter.serializer()).serialize("123", mockGenerator, mockProvider);
    }

}
