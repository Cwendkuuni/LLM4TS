package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private ObjectIdGenerator<Object> mockIdGenerator;
    private ObjectIdWriter mockObjectIdWriter;

    @Before
    public void setUp() {
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
        mockIdGenerator = mock(IdIntGenerator.class);
        mockObjectIdWriter = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNull(writableObjectId.id);
        assertNull(writableObjectId.idWritten);
        assertEquals(generator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsId_NullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
    }

    @Test
    public void testWriteAsId_NullId() {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
    }

    @Test
    public void testWriteAsId_NullWriter() {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, null));
    }

    @Test
    public void testWriteAsId_IDNullWriter() {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
    }

    @Test
    public void testWriteAsId_IDWrittenFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
    }

    @Test
    public void testWriteAsId_IDWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockJsonGenerator.writeObjectRef(String.valueOf(1L))).thenReturn(mockJsonGenerator);
        when(mockObjectIdWriter.alwaysAsId).thenReturn(false);
        Object id = 1L;
        WritableObjectId writableObjectIdInstance = new WritableObjectId(mockIdGenerator);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        assertTrue(writableObjectIdInstance.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        verify(mockJsonGenerator, times(1)).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_IDWrittenTrue_AlwaysAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockJsonGenerator.writeObjectRef(String.valueOf(1L))).thenReturn(mockJsonGenerator);
        when(mockObjectIdWriter.alwaysAsId).thenReturn(true);
        Object id = 1L;
        WritableObjectId writableObjectIdInstance = new WritableObjectId(mockIdGenerator);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        assertTrue(writableObjectIdInstance.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        verify(mockJsonGenerator, times(1)).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_IDWrittenTrue_IDNullWriter() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockJsonGenerator.writeObjectId(String.valueOf(1L))).thenReturn(mockJsonGenerator);
        when(mockObjectIdWriter.alwaysAsId).thenReturn(false);
        Object id = 1L;
        WritableObjectId writableObjectIdInstance = new WritableObjectId(mockIdGenerator);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        assertTrue(writableObjectIdInstance.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        verify(mockJsonGenerator, times(1)).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testGenerateId_NullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId_NullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId_SameIdGenerated() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        Object id = 1L;
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        Object idGenerated = writableObjectId.generateId(new Object());
        Object idGenerated2 = writableObjectId.generateId(new Object());
        assertNotSame("Ids should not be the same", idGenerated, idGenerated2);
        assertEquals(id, idGenerated);
        assertEquals(id, idGenerated2);
    }

    @Test
    public void testGenerateId_DifferentIdsGenerated() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        Object id1 = 1L;
        Object id2 = 2L;
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id1);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id2);
        Object idGenerated1 = writableObjectId.generateId(new Object());
        Object idGenerated2 = writableObjectId.generateId(new Object());
        assertNotSame("Ids should not be the same", idGenerated1, idGenerated2);
        assertEquals(id1, idGenerated1);
        assertEquals(id2, idGenerated2);
    }

    @Test
    public void testWriteAsField_NullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
    }

    @Test
    public void testWriteAsField_NullJsonGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        writableObjectId.writeAsField(null, mockSerializerProvider, mockObjectIdWriter);
    }

    @Test
    public void testWriteAsField_NullWriter() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, null);
    }

    @Test
    public void testWriteAsField_NullWriterPropertyName() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
    }

    @Test
    public void testWriteAsField_IDWrittenFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_IDWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        Object id = 1L;
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockObjectIdWriter.propertyName).thenReturn(new SerializedString("name"));
        when(mockSerializerProvider.findValueSerializer(id.getClass())).thenReturn(mock(JsonSerializer.class));
        WritableObjectId writableObjectIdInstance = new WritableObjectId(mockIdGenerator);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        writableObjectIdInstance.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertTrue(writableObjectIdInstance.idWritten);
        verify(mockJsonGenerator, times(1)).writeFieldName("name");
        verify(mockSerializerProvider, times(1)).findValueSerializer(id.getClass());
        verify(mockJsonGenerator, times(1)).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testWriteAsField_IDWrittenTrue_AlwaysAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(mockIdGenerator);
        Object id = 1L;
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);
        when(mockObjectIdWriter.propertyName).thenReturn(new SerializableString("name"));
        when(mockObjectIdWriter.alwaysAsId).thenReturn(true);
        when(mockSerializerProvider.findValueSerializer(id.getClass())).thenReturn(mock(JsonSerializer.class));
        WritableObjectId writableObjectIdInstance = new WritableObjectId(mockIdGenerator);
        when(mockIdGenerator.generateId(anyObject())).thenReturn(id);
        writableObjectIdInstance.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertTrue(writableObjectIdInstance.idWritten);
        verify(mockJsonGenerator, times(1)).writeFieldName("name");
        verify(mockSerializerProvider, times(1)).findValueSerializer(id.getClass());
        verify(mockJsonGenerator, times(1)).writeObjectId(String.valueOf(id));
    }
}