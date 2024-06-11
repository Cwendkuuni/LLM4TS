package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIdGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test cases for WritableObjectId class
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
        assertEquals(generator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsIdHappyPath() throws IOException {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.id = id;
        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdNullGenerator() throws IOException {
        ObjectIdGenerator<?> generator = null;
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.writeAsId(gen, provider, w);
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdNullId() throws IOException {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.writeAsId(gen, provider, w);
    }

    @Test
    public void testGenerateIdHappyPath() {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);
        assertNotNull(id);
        assertEquals(generator, writableObjectId.generator);
    }

    @Test(expected = JsonProcessingException.class)
    public void testGenerateIdNullGenerator() {
        ObjectIdGenerator<?> generator = null;
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        writableObjectId.generateId(forPojo);
    }

    @Test(expected = JsonMappingException.class)
    public void testGenerateIdNullForPojo() {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        writableObjectId.generateId(null);
    }

    @Test
    public void testWriteAsFieldHappyPath() throws IOException {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        String fieldName = "testField";

        writableObjectId.writeAsField(gen, provider, w);
        w.propertyName = new SerializableString(fieldName);
        writableObjectId.id = "testId";

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, times(2)).writeFieldName(eq(fieldName));
        verify(gen, times(2)).writeObjectId(eq(String.valueOf(writableObjectId.id)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteAsFieldNullGenerator() throws IOException {
        ObjectIdGenerator<?> generator = null;
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.writeAsField(gen, provider, w);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteAsFieldNullId() throws IOException {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.writeAsField(gen, provider, w);
    }

    @Test
    public void testWriteAsFieldNullPropertyName() throws IOException {
        ObjectIdGenerator<?> generator = new IdIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectIdWriter w = new ObjectIdWriter(null, null);

        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();

        writableObjectId.writeAsField(gen, provider, w);
        writableObjectId.id = "testId";

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, never()).writeFieldName(anyString());
        verify(gen, times(1)).writeObjectId(eq(String.valueOf(writableObjectId.id)));
    }

    private JsonGenerator createMockJsonGenerator() {
        return mock(JsonGenerator.class);
    }

    private SerializerProvider createMockSerializerProvider() {
        return mock(SerializerProvider.class);
    }
}