
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.SerializableString;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WritableObjectIdTest {

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(generator);

        assertEquals(generator, objectId.generator);
        assertNull(objectId.id);
        assertFalse(objectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWritten() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = true;

        assertTrue(objectId.writeAsId(gen, provider, writer));
        verify(gen).writeObjectRef(eq("123"));
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndAlwaysAsId() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = false;

        assertTrue(objectId.writeAsId(gen, provider, writer));
        verify(gen).writeObjectRef(eq("123"));
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndCannotWriteObjectId() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = false;

        assertTrue(objectId.writeAsId(gen, provider, writer));
        verify(writer.serializer).serialize(eq(123), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsIdWithIdNotNotNullAndCannotWriteObjectId() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = null;
        objectId.idWritten = false;

        assertFalse(objectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        WritableObjectId objectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        when(generator.generateId(forPojo)).thenReturn(123);

        assertEquals(123, objectId.generateId(forPojo));
        assertEquals(123, objectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, mock(SerializableString.class), null, true);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = true;

        objectId.writeAsField(gen, provider, writer);
        verify(gen).writeObjectId(eq("123"));
    }

    @Test
    public void testWriteAsFieldWithIdWrittenAndCannotWriteObjectId() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, mock(SerializableString.class), null, false);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = true;

        objectId.writeAsField(gen, provider, writer);
        verify(writer.serializer).serialize(eq(123), eq(gen), eq(provider));
    }

    @Test
    public void testWriteAsFieldWithIdNotWritten() throws IOException {
        ObjectIdWriter writer = new ObjectIdWriter(null, mock(SerializableString.class), null, true);
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        objectId.id = 123;
        objectId.idWritten = false;

        objectId.writeAsField(gen, provider, writer);
        verify(gen).writeFieldName(eq(writer.propertyName));
        verify(writer.serializer).serialize(eq(123), eq(gen), eq(provider));
    }
}
