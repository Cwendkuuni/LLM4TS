package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    @InjectMocks
    private WritableObjectId writableObjectId;

    @Mock
    private ObjectIdGenerator<?> mockGenerator;

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        writableObjectId = new WritableObjectId(mockGenerator);
    }

    @Test
    public void testWriteAsId() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, null);

        verify(mockJsonGenerator).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_notWritten() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, null);

        verify(mockJsonGenerator, never()).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_idIsNull() throws Exception {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;

        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, null);

        verify(mockJsonGenerator, never()).writeObjectRef(anyString());
    }

    @Test
    public void testWriteAsId_alwaysAsId() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.alwaysAsId()).thenReturn(true);

        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockWriter);

        verify(mockJsonGenerator).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsField() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;

        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.propertyName()).thenReturn(null);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockWriter);

        verify(mockJsonGenerator, never()).writeFieldName(any(SerializableString.class));
        verify(mockJsonGenerator).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testWriteAsField_propertyNameNotNull() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;

        SerializableString mockName = mock(SerializableString.class);
        ObjectIdWriter mockWriter = mock(ObjectIdWriter.class);
        when(mockWriter.propertyName()).thenReturn(mockName);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockWriter);

        verify(mockJsonGenerator).writeFieldName(mockName);
        verify(mockJsonGenerator).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testGenerateId() throws Exception {
        Object forPojo = new Object();
        Object id = new Object();

        when(mockGenerator.generateId(forPojo)).thenReturn(id);

        Object result = writableObjectId.generateId(forPojo);

        assertEquals(id, result);
        assertEquals(id, writableObjectId.id);
    }
}