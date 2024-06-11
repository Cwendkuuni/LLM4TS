package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Unit tests for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter writer;

    @Mock
    private SerializableString name;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenTrue() throws IOException {
        writableObjectId.idWritten = true;
        writableObjectId.id = "testId";

        when(writer.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        verify(gen, times(1)).writeObjectRef("testId");
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenFalse() throws IOException {
        writableObjectId.idWritten = false;
        writableObjectId.id = "testId";

        when(writer.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        verify(gen, times(0)).writeObjectRef(anyString());
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsFieldWithObjectIdWritable() throws IOException {
        writableObjectId.id = "testId";
        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, times(1)).writeObjectId(anyString());
        verify(gen, times(0)).writeFieldName(any(SerializableString.class));
    }

    @Test
    public void testWriteAsFieldWithObjectIdNotWritable() throws IOException {
        writableObjectId.id = "testId";
        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName).thenReturn(name);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, times(0)).writeObjectId(anyString());
        verify(gen, times(1)).writeFieldName(name);
        verify(writer.serializer, times(1)).serialize(any(), any(JsonGenerator.class), any(SerializerProvider.class));
    }
    
    @Test
    public void testGenerateId() {
        when(generator.generateId(any())).thenReturn("testId");
        
        Object result = writableObjectId.generateId(new Object());
        
        Assert.assertNotNull(result);
        Assert.assertEquals("testId", result);
        Assert.assertEquals("testId", writableObjectId.id);
    }
}