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

    private WritableObjectId id;
    private ObjectIdGenerator<?> generator;
    private ObjectIdWriter writer;
    private JsonSerializer<Object> serializer;
    private JsonGenerator gen;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        writer = mock(ObjectIdWriter.class);
        serializer = mock(JsonSerializer.class);
        id = new WritableObjectId(generator);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testWriteAsIdWithNullGenerator() {
        id = new WritableObjectId(null);
        when(writer.alwaysAsId).thenReturn(false);
        assertFalse(id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithNullId() {
        when(generator.generateId(Mockito.any())).thenReturn(null);
        id = new WritableObjectId(generator);
        when(writer.alwaysAsId).thenReturn(false);
        assertFalse(id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdWrittenOrAlwaysAsIdTrue() {
        id = new WritableObjectId(generator);
        when(writer.alwaysAsId).thenReturn(true);
        id.id = "testId";
        when(gen.canWriteObjectId()).thenReturn(true);
        assertTrue(id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdWrittenFalseAndCanWriteObjectIdFalse() {
        id = new WritableObjectId(generator);
        id.id = "testId";
        when(writer.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);
        assertFalse(id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsIdWithIdWrittenFalseAndCanWriteObjectIdTrue() {
        id = new WritableObjectId(generator);
        id.id = "testId";
        when(writer.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectRef(Mockito.anyString())).thenAnswer(i -> i.getArgument(0));
        assertEquals("testId", id.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateIdWithNullForPojo() {
        when(generator.generateId(Mockito.any())).thenReturn("testId");
        id = new WritableObjectId(generator);
        assertNull(id.id);
        id.generateId(null);
        assertSame("testId", id.id);
    }

    @Test
    public void testGenerateIdWithNonNullForPojo() {
        when(generator.generateId(Mockito.any())).thenReturn("testId");
        Object pojo = new Object();
        id = new WritableObjectId(generator);
        id.generateId(pojo);
        assertSame("testId", id.id);
    }

    @Test
    public void testWriteAsFieldWithNullId() {
        id = new WritableObjectId(generator);
        when(writer.propertyName).thenReturn(new SerializableString("testName"));
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeFieldName(Mockito.anyString())).thenAnswer(i -> i.getArguments()[0]);
        id.writeAsField(gen, provider, writer);
        assertEquals("testName", gen.getOutputValue());
        assertNull(id.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() {
        id = new WritableObjectId(generator);
        id.id = "testId";
        when(writer.propertyName).thenReturn(new SerializableString("testName"));
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectId(Mockito.anyString())).thenAnswer(i -> i.getArgument(0));
        id.writeAsField(gen, provider, writer);
        assertEquals("testName", gen.getOutputValue());
        assertEquals("testId", id.id);
    }

    @Test
    public void testWriteAsFieldWithCanWriteObjectIdFalse() {
        id = new WritableObjectId(generator);
        id.id = "testId";
        when(writer.propertyName).thenReturn(new SerializableString("testName"));
        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.serializer).thenReturn(serializer);
        id.writeAsField(gen, provider, writer);
        assertEquals("testName", gen.getOutputValue());
        assertEquals("testId", id.id);
    }

}