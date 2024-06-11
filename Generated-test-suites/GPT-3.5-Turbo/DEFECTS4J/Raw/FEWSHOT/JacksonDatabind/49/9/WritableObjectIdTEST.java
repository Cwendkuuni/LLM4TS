
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;
    
    @Mock
    private JsonGenerator gen;
    
    @Mock
    private SerializerProvider provider;
    
    @Mock
    private ObjectIdWriter w;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        when(w.alwaysAsId).thenReturn(true);
        when(gen.writeObjectRef(anyString())).thenReturn(null);
        when(w.serializer.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        verify(gen, times(1)).canWriteObjectId();
        verify(gen, times(1)).writeObjectRef(anyString());
        verify(w.serializer, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        assertEquals(true, result);
    }

    @Test
    public void testWriteAsId_IdIsNull() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        when(w.alwaysAsId).thenReturn(true);
        when(gen.writeObjectRef(anyString())).thenReturn(null);
        when(w.serializer.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        verify(gen, times(1)).canWriteObjectId();
        verify(gen, never()).writeObjectRef(anyString());
        verify(w.serializer, never()).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        assertEquals(false, result);
    }

    @Test
    public void testWriteAsId_AlwaysAsIdFalse() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        when(w.alwaysAsId).thenReturn(false);
        when(w.serializer.serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

        boolean result = writableObjectId.writeAsId(gen, provider, w);

        verify(gen, times(1)).canWriteObjectId();
        verify(gen, never()).writeObjectRef(anyString());
        verify(w.serializer, times(1)).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
        assertEquals(true, result);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object expectedResult = new Object();

        when(generator.generateId(forPojo)).thenReturn(expectedResult);

        Object result = writableObjectId.generateId(forPojo);

        verify(generator, times(1)).generateId(forPojo);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testWriteAsField() throws IOException {
        SerializableString name = mock(SerializableString.class);

        when(w.propertyName).thenReturn(name);
        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, times(1)).canWriteObjectId();
        verify(gen, never()).writeString(anyString());
        verify(name, times(1)).getValue();
        verify(gen, times(1)).writeFieldName(name);
        verify(w.serializer, times(1)).serialize(anyObject(), any(JsonGenerator.class), any(SerializerProvider.class));
    }
}
