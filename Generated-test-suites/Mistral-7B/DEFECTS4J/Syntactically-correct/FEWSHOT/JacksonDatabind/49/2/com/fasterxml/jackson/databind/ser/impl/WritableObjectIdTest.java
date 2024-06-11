package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectMapper mapper;
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = new ObjectIdWriter(new StdSerializer<>((Object) null), new NameTransformer() {
            @Override
            public SerializableString transform(String name, Class<?> hint) {
                return new SerializableString("test_" + name);
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mapper = null;
        idGenerator = null;
        idWriter = null;
    }

    @Test
    public void testWritableObjectIdConstruction() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithNullGen() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdWithNoId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdWithIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object id = new Object();

        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object id = new Object();

        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
        idWriter.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithIdGeneratorFailure() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        idGenerator.generateId = () -> {
            throw new IOException();
        };

        writableObjectId.writeAsId(gen, provider, idWriter);
    }

    @Test
    public void testGenerateId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = new Object();

        Object newId = writableObjectId.generateId(id);
        assertNotNull(newId);
        assertNotEquals(id, newId);
    }

    @Test
    public void testWriteAsFieldWithNullGen() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertFalse(writableObjectId.writeAsField(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsFieldWithNoId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertFalse(writableObjectId.writeAsField(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object id = new Object();

        writableObjectId.id = id;

        WritableObjectId expected = new WritableObjectId(idGenerator);
        expected.idWritten = true;

        WritableObjectId actual = writableObjectId.writeAsField(gen, provider, idWriter);

        assertEquals(expected, actual);
        assertNotNull(actual.id);
        assertTrue(actual.idWritten);
    }
}