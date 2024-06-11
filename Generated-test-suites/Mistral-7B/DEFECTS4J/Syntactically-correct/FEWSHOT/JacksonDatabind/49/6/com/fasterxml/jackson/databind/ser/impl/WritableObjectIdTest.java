package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.PropertyWriter;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriterImpl;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> idGenerator;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnnotatedClass annotatedClass;

    @Mock
    private AnnotatedMember annotatedMember;

    @Mock
    private PropertyWriter propertyWriter;

    @Mock
    private ObjectIdWriter objectIdWriter;

    @InjectMocks
    private WritableObjectId writableObjectId;

    @Test
    public void testWriteAsIdNoId() throws IOException {
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        verifyNoMoreInteractions(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test
    public void testWriteAsIdHasId() throws IOException {
        Object id = new Object();
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idGenerator.generateId(any())).thenReturn(id);

        writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        verify(jsonGenerator).writeFieldName("id");
        verify(serializerProvider).findPropertyWriter(eq(annotatedMember), any());
        PropertyWriter propertyWriter = mock(PropertyWriter.class);
        when(serializerProvider.findPropertyWriter(eq(annotatedMember), any())).thenReturn(propertyWriter);
        when(propertyWriter.serializeAsField(jsonGenerator, serializerProvider, anyInt(), any(), anyBoolean())).thenReturn(null);
    }

    @Test
    public void testWriteAsIdAlwaysAsId() throws IOException {
        Object id = new Object();
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        verify(jsonGenerator).writeObjectId(eq("id"));
    }

    @Test
    public void testWriteAsIdForwardReference() throws IOException {
        Object id = new Object();
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId = new WritableObjectId(idGenerator);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdNullId() throws IOException {
        Object id = null;
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idGenerator.generateId(any())).thenReturn(id);

        writableObjectId = new WritableObjectId(idGenerator);

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testGenerateIdNoId() {
        Object id = null;

        id = writableObjectId.generateId(null);

        assertNull(id);
    }

    @Test
    public void testGenerateIdIdGenerated() {
        Object id = new Object();

        id = writableObjectId.generateId(new Object());

        assertEquals(id, writableObjectId.id);
    }

    @Test
    public void testGenerateIdAlwaysAsId() {
        Object id = new Object();

        id = writableObjectId.generateId(new Object());

        assertTrue(idWritten);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}