package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> generator;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;

    @Before
    public void setup() {
        generator = mock(ObjectIdGenerator.class);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsIdNullGenerator() {
        WritableObjectId id = new WritableObjectId(null);
        assertFalse(id.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdNoIdWrittenOrAlwaysAsId() {
        WritableObjectId id = new WritableObjectId(generator);
        when(generator.generateId(any())).thenReturn("id1");
        assertFalse(id.writeAsId(gen, provider, w));
        when(w.alwaysAsId).thenReturn(true);
        id.id = "id2";
        assertFalse(id.writeAsId(gen, provider, w));
        verify(gen, never()).writeObjectId(anyString());
        verify(gen, never()).writeFieldName(anyString());
        verify(w.serializer).serialize(eq("id2"), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsIdIdWritten() {
        WritableObjectId id = new WritableObjectId(generator);
        when(generator.generateId(any())).thenReturn("id1");
        id.id = "id2";
        id.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        assertTrue(id.writeAsId(gen, provider, w));
        verify(gen).writeObjectId(eq("id2"));
        verify(gen, never()).writeFieldName(anyString());
        verify(w.serializer, never()).serialize(any(), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsIdNoCanWriteObjectId() {
        WritableObjectId id = new WritableObjectId(generator);
        when(generator.generateId(any())).thenReturn("id1");
        id.id = "id2";
        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.propertyName).thenReturn(new SerializableString("name"));
        id.writeAsField(gen, provider, w);
        verify(gen).writeFieldName(eq("name"));
        verify(w.serializer).serialize(eq("id2"), eq(gen), eq(provider));
    }

    @Test
    public void testGenerateId() {
        when(generator.generateId(any())).thenReturn("id1");
        WritableObjectId id = new WritableObjectId(generator);
        id.generateId(null);
        assertSame("id1", id.id);
    }

    @Test
    public void testGenerateIdWithPojo() {
        when(generator.generateId(any())).thenReturn("id1");
        Object pojo = new Object();
        WritableObjectId id = new WritableObjectId(generator);
        id.generateId(pojo);
        assertSame("id1", id.id);
    }
}