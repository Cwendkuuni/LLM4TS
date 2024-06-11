package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private WritableObjectId objectId;
    private ObjectIdGenerator<?> generator;

    @Before
    public void setup() {
        generator = mock(ObjectIdGenerator.class);
        objectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        // Case when id and idWritten are both true
        objectId.id = "Test";
        objectId.idWritten = true;
        when(writer.alwaysAsId()).thenReturn(true);
        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(objectId.writeAsId(gen, provider, writer));
        verify(gen).writeObjectRef("Test");

        // Case when id is null
        objectId.id = null;

        assertFalse(objectId.writeAsId(gen, provider, writer));
        verify(writer).alwaysAsId();
        verify(gen).canWriteObjectId();

        // Case when idWritten is false
        objectId.id = "Test";
        objectId.idWritten = false;
        when(writer.alwaysAsId()).thenReturn(false);

        assertFalse(objectId.writeAsId(gen, provider, writer));
        verify(gen).canWriteObjectId();
        verify(gen).writeFieldName(null);
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        when(generator.generateId(forPojo)).thenReturn("Test");

        assertEquals("Test", objectId.generateId(forPojo));
        verify(generator).generateId(forPojo);
        assertEquals("Test", objectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);

        objectId.idWritten = false;
        objectId.id = "Test";
        when(gen.canWriteObjectId()).thenReturn(true);

        objectId.writeAsField(gen, provider, writer);
        verify(gen).canWriteObjectId();
        verify(gen).writeObjectId("Test");

        objectId.idWritten = true;
        objectId.id = "Test2";
        when(writer.propertyName()).thenReturn(mock(SerializableString.class));

        objectId.writeAsField(gen, provider, writer);
        verify(gen).canWriteObjectId();
        verify(gen).writeFieldName(writer.propertyName());
        verify(writer).serializer();
    }
}