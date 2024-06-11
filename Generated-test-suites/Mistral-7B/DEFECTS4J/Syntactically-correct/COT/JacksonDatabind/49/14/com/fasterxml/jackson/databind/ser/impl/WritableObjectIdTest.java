package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

    private ObjectMapper mapper;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        mapper = new JsonMapper();
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = new ObjectIdWriter(null, new StdSerializer<Object>(Object.class), null);
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWritableObjectIdConstructor() {
        assertNotNull(writableObjectId);
    }

    @Test
    public void testIsIdWrittenInitially() {
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdNullId() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsIdAlreadyWritten() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(true);
        writableObjectId.writeAsId(gen, provider, idWriter);
        writableObjectId.writeAsId(gen, provider, idWriter);

        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdNullGenerator() throws IOException {
        assertFalse(writableObjectId.writeAsId(null, provider, idWriter));
    }

    @Test
    public void testWriteAsIdNullIdWritten() {
        writableObjectId.writeAsId(gen, provider, idWriter);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdAlwaysAsId() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);
        idWriter.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(gen, provider, idWriter));
    }

    @Test
    public void testGenerateIdNullForPojo() {
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId() {
        when(idGenerator.generateId(null)).thenReturn(new Object());

        assertNotNull(writableObjectId.generateId(null));
    }

    @Test(expected = IOException.class)
    public void testGenerateIdIOException() {
        when(idGenerator.generateId(null)).thenThrow(new IOException());

        writableObjectId.generateId(null);
    }

    @Test
    public void testWriteAsFieldNullGenerator() throws IOException {
        assertFalse(writableObjectId.writeAsField(null, provider, idWriter));
    }

    @Test
    public void testWriteAsFieldNullId() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);

        assertFalse(writableObjectId.writeAsField(gen, provider, idWriter));
    }

    @Test
    public void testWriteAsFieldNullIdWritten() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);
        writableObjectId.writeAsField(gen, provider, idWriter);

        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldCanWriteObjectId() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(true);
        when(gen.canWriteObjectId()).thenReturn(true);
        SerializableString name = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, idWriter);
        when(gen.writeObjectId(anyString())).thenReturn(gen);
        writableObjectId.writeAsField(gen, provider, idWriter);

        assertTrue(writableObjectId.idWritten);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldIOException() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);
        when(gen.writeFieldName(anyString())).thenThrow(new IOException());

        writableObjectId.writeAsField(gen, provider, idWriter);
    }

    @Test
    public void testWriteAsFieldNullName() throws IOException {
        when(provider.isEnabled(JsonGenerator.Feature.WRITE_OBJECT_IDS)).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(false);
        when(gen.writeFieldName(null)).thenReturn(gen);
        when(gen.writeObjectId(anyString())).thenReturn(gen);
        when(idWriter.propertyName).thenReturn(null);

        writableObjectId.writeAsField(gen, provider, idWriter);

        assertFalse(writableObjectId.idWritten);
    }
}