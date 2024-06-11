package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTest;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_whenIdIsNull() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testWriteAsId_whenIdWrittenOrAlwaysAsId() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);
        writableObjectId.idWritten = true;

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_whenGeneratorCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_whenGeneratorCannotWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);
        writableObjectId.idWritten = true;

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;

        // Configure generator to not be able to write ObjectId
        JsonGenerator spyGen = createSpy(JsonGenerator.class);
        doReturn(false).when(spyGen).canWriteObjectId();
        gen = spyGen;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen).writeFieldName(anyString());
        verify(gen).writeString(String.valueOf(id));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);
        assertNotNull(id);
    }

    @Test(expected = IOException.class)
    public void testGenerateId_whenGeneratorThrowsIOException() throws IOException {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object id) throws IOException {
                throw new IOException();
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        writableObjectId.generateId(forPojo);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;
        writableObjectId.writeAsField(gen, provider, w);
        verify(gen).writeFieldName(anyString());
        verify(gen).writeString(String.valueOf(id));
    }

    @Test
    public void testWriteAsField_whenGeneratorCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = ObjectIdGeneratorTest.createSimpleGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = new Long(1L);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, null, null, null, null);

        writableObjectId.id = id;

        // Configure generator to be able to write ObjectId
        JsonGenerator spyGen = createSpy(JsonGenerator.class);
        doReturn(true).when(spyGen).canWriteObjectId();
        gen = spyGen;

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen).writeObjectId(eq(String.valueOf(id)));
    }

}