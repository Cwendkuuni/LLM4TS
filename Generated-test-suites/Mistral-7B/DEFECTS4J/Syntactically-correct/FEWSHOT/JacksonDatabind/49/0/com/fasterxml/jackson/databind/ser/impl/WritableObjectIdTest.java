package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;

import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsIdWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithNullId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithIdWritten() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);

        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        when(generator.generateId(Mockito.any())).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithIdNotWritten() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);

        Object id = new Object();
        writableObjectId.id = id;

        when(generator.generateId(Mockito.any())).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(true);

        Object id = new Object();
        writableObjectId.id = id;

        when(generator.generateId(Mockito.any())).thenReturn(null);
        when(gen.canWriteObjectId()).thenReturn(true);

        when(w.serializer).thenThrow(new JsonMappingException("Test exception"));

        try {
            writableObjectId.writeAsId(gen, provider, w);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testGenerateIdWithNullForPojo() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Object id = new Object();
        when(generator.generateId(forPojo)).thenReturn(id);

        assertEquals(id, writableObjectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        assertFalse(w.written);
    }

    @Test
    public void testWriteAsFieldWithNullId() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        assertFalse(w.written);
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        assertFalse(w.written);
    }

    @Test
    public void testWriteAsField() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            boolean written = false;
            SerializableString propertyName = new SerializableString("testPropertyName");
            Serializer<Object> serializer = mock(Serializer.class);

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                this.serializer.serialize(value, gen, provider);
                written = true;
            }
        };
        Object id = new Object();

        when(generator.generateId(Mockito.any())).thenReturn(id);
        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.id = id;
        writableObjectId.writeAsField(gen, provider, w);

        assertTrue(w.written);
    }
}