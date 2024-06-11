package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * JUnit test cases for WritableObjectId class
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWritableObjectIdConstructorNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
    }

    @Test
    public void testWriteAsIdWithIdWritten() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        writableObjectId.id = "testId";
        writableObjectId.writeAsId(gen, provider, w);
        assertTrue(w.wasCalled);
        assertEquals("testId", w.writtenId);
    }

    @Test
    public void testWriteAsIdWithoutIdWritten() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        writableObjectId.writeAsId(gen, provider, w);
        assertFalse(w.wasCalled);
    }

    @Test
    public void testGenerateId() throws InstantiationException, IllegalAccessException, IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();

        Object id = writableObjectId.generateId(obj);
        assertNotNull(id);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        writableObjectId.id = "testId";
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(w.wasCalled);
        assertEquals("testId", w.writtenId);
    }

    @Test
    public void testWriteAsFieldWithoutIdWritten() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        writableObjectId.writeAsField(gen, provider, w);
        assertFalse(w.wasCalled);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteAsFieldWithNullName() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        writableObjectId.writeAsField(gen, provider, w);
        w.name = null;
        writableObjectId.writeAsField(gen, provider, w);
    }

    @Test
    public void testWriteAsFieldWithAlwaysAsIdTrue() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = true;

        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(gen.wasCalledWriteObjectId);
    }

    @Test
    public void testWriteAsFieldWithCanWriteObjectIdFalse() throws IOException {
        ObjectIdGenerator<Object> generator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object();
        SerializableString name = new SerializableString("name");
        JsonGenerator gen = createMockJsonGenerator();
        SerializerProvider provider = createMockSerializerProvider();
        ObjectIdWriter w = createMockObjectIdWriter();

        gen.mockWriteObjectId = false;
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(w.wasCalled);
        assertEquals(name.getText(), w.writtenName);
    }

    private ObjectIdWriter createMockObjectIdWriter() {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);
        when(w.propertyName).thenReturn(new SerializableString("name"));
        when(w.serializer).thenReturn(mock(StdSerializer.class));
        w.wasCalled = false;
        return w;
    }

    private SerializerProvider createMockSerializerProvider() {
        return mock(SerializerProvider.class);
    }

    private JsonGenerator createMockJsonGenerator() {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectRef(String.valueOf(anyString()))).thenReturn(gen);
        gen.wasCalledWriteObjectId = false;
        return gen;
    }
}