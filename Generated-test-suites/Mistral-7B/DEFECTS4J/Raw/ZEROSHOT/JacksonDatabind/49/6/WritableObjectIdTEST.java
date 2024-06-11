package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdIntgenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderHolder;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for WritableObjectId.java
 */
public class WritableObjectIdTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private ObjectIdGenerator<Object> objectIdGenerator;

    @Mock
    private ObjectIdWriter objectIdWriter;

    @Mock
    private SerializerProvider.SerializerProviderHolder serializerProviderHolder;

    @Mock
    private TypeIdResolver typeIdResolver;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(serializerProvider.getProviderHolder()).thenReturn(serializerProviderHolder);
        when(serializerProviderHolder.getSerializerProvider()).thenReturn(serializerProvider);
        when(serializerProvider.findValueSerializer(eq(Object.class),eq(typeIdResolver)))
                .thenReturn(mock(ObjectSerializer.class));
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("propertyName"));
        when(objectIdGenerator.generateId(eq(Object.class), any()))
                .thenReturn(new Object());
        writableObjectId = new WritableObjectId(objectIdGenerator);
    }

    @Test
    public void testGenerateId() throws IOException {
        writableObjectId.generateId(new Object());
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(anyString()))
                .thenAnswer(invocation -> invocation.callRealMethod());

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(result);
    }

    @Test
    public void testWriteAsIdWhenCannotWriteObjectId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertFalse(result);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(jsonGenerator.writeFieldName(anyString())).thenAnswer(invocation -> invocation.callRealMethod());
        when(objectIdWriter.serializer).thenReturn(mock(ObjectSerializer.class));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(objectIdWriter, times(1)).serializer;
    }
}

class ObjectSerializer {}