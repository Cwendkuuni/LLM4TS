package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.ObjectIdGeneratorType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsIdWithGeneratedId() throws Exception {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.getType()).thenReturn(ObjectIdGeneratorType.STD);
        when(generator.generateId(null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter();

        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        boolean written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNull(writableObjectId.id);

        writableObjectId.id = "existingId";
        written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNotNull(writableObjectId.id);

        when(gen.canWriteObjectId()).thenReturn(true);
        written = writableObjectId.writeAsId(gen, provider, w);
        assertTrue(written);
        assertEquals("generatedId", writableObjectId.id);

        when(gen.canWriteObjectId()).thenReturn(false);
        StdSerializer<Object> serializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Object.class, null)).thenReturn(serializer);
        writableObjectId.id = "existingId";
        written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNotNull(writableObjectId.id);
        w.checkWrite("generatedId", serializer, "existingId");
    }

    @Test
    public void testGenerateId() throws Exception {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        writableObjectId.generateId(forPojo);
        assertEquals("generatedId", writableObjectId.id);
        assertTrue(writableObjectId.idWritten);
    }

    @Test(expected = JsonMappingException.Class)
    public void testWriteAsIdWithNullGenerator() throws Exception {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter();

        writableObjectId.writeAsId(gen, provider, w);
    }

    @Test
    public void testWriteAsIdWithNullId() throws Exception {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter();

        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        writableObjectId.id = null;
        boolean written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNull(writableObjectId.id);

        when(gen.canWriteObjectId()).thenReturn(true);
        written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNull(writableObjectId.id);

        when(gen.canWriteObjectId()).thenReturn(false);
        StdSerializer<Object> serializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Object.class, null)).thenReturn(serializer);
        written = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(written);
        assertNull(writableObjectId.id);
        w.checkWrite(null, serializer, null);
    }

    @Test
    public void testWriteAsField() throws Exception {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter();

        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        SerializableString name = new SerializableString("name");
        when(provider.findPropertySerializer(WritableObjectId.class, "name")).thenReturn(mock(StdSerializer.class));

        writableObjectId.writeAsField(gen, provider, w);
        assertEquals("name", w.propertyName);
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);

        writableObjectId.id = "existingId";
        writableObjectId.writeAsField(gen, provider, w);
        assertEquals("name", w.propertyName);
        assertTrue(writableObjectId.idWritten);
        assertEquals("existingId", writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithNullName() throws Exception {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter();

        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        writableObjectId.writeAsField(gen, provider, w);
        assertNull(w.propertyName);
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }
}