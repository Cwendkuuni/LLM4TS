package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator mockGenerator;

    @Mock
    private JsonGenerator mockGenerator;

    @Mock
    private SerializerProvider mockProvider;

    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writableObjectId = new WritableObjectId(mockGenerator);
    }

    @Test
    public void testWriteAsId_WithNonNullIdAndIdWritten_True() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId).thenReturn(false);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));

        verify(mockGenerator, times(1)).writeObjectRef(eq("123"));
    }

    @Test
    public void testWriteAsId_WithNonNullIdAndIdWritten_False() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));

        verify(mockGenerator, times(1)).writeObjectRef(eq("123"));
    }

    @Test
    public void testWriteAsId_WithNonNullIdAndIdWritten_False_CannotWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(false);

        assertTrue(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));

        verify(mockWriter.serializer, times(1)).serialize(eq("123"), eq(mockGenerator), eq(mockProvider));
    }

    @Test
    public void testWriteAsId_WithNullId() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId).thenReturn(true);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter));

        verify(mockGenerator, never()).writeObjectRef(anyString());
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        when(mockGenerator.generateId(eq(forPojo))).thenReturn("generatedId");

        Object id = writableObjectId.generateId(forPojo);

        assertEquals("generatedId", id);
        assertSame("generatedId", writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WithCanWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockGenerator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator, times(1)).writeObjectId(eq("123"));
        verify(mockGenerator, never()).writeFieldName(anyString());
        verify(mockWriter.serializer, never()).serialize(any(), eq(mockGenerator), eq(mockProvider));
    }

    @Test
    public void testWriteAsField_WithPropertyName() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(mockWriter.propertyName).thenReturn(mock(SerializableString.class));

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator, never()).writeObjectId(anyString());
        verify(mockGenerator, times(1)).writeFieldName(any());
        verify(mockWriter.serializer, times(1)).serialize(eq("123"), eq(mockGenerator), eq(mockProvider));
    }

    @Test
    public void testWriteAsField_WithoutPropertyName() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);

        verify(mockGenerator, never()).writeObjectId(anyString());
        verify(mockGenerator, never()).writeFieldName(any());
        verify(mockWriter.serializer, times(1)).serialize(eq("123"), eq(mockGenerator), eq(mockProvider));
    }
}