package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private Object forPojo;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter w;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenEqualToTrue() throws IOException {
        writableObjectId.id = "id";
        writableObjectId.idWritten = true;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectRef("id");
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenEqualToFalse() throws IOException {
        writableObjectId.id = "id";
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectRef("id");
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithIdNull() throws IOException {
        assertNull(writableObjectId.id);
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;

        assertFalse(writableObjectId.writeAsId(gen, provider, w));

        verify(gen, never()).writeObjectRef(anyString());
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenEqualToTrueButNotAlwaysAsId() throws IOException {
        writableObjectId.id = "id";
        writableObjectId.idWritten = true;
        w.alwaysAsId = false;

        assertFalse(writableObjectId.writeAsId(gen, provider, w));

        verify(gen, never()).writeObjectRef(anyString());
    }

    @Test
    public void testGenerateId() {
        when(generator.generateId(forPojo)).thenReturn("id");

        assertEquals("id", writableObjectId.generateId(forPojo));
        assertEquals("id", writableObjectId.id);

        verify(generator).generateId(forPojo);
    }

    @Test
    public void testWriteAsFieldWithIdNotNullAndCanWriteObjectId() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        writableObjectId.id = "id";
        w.propertyName = null;

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId("id");
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdNotNullAndPropertyNameNotNull() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = "id";
        w.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeFieldName(w.propertyName);
        verify(w.serializer).serialize("id", gen, provider);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdNotNullAndPropertyNameNull() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = "id";
        w.propertyName = null;

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(w.serializer).serialize("id", gen, provider);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdNull() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(false);
        writableObjectId.id = null;
        w.propertyName = null;

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(w.serializer, never()).serialize(any(Object.class), eq(gen), eq(provider));
        assertTrue(writableObjectId.idWritten);
    }
}