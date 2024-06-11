
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    private WritableObjectId objectId;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        objectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndIdWritten() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.id = "123";
        objectId.idWritten = true;

        assertTrue(objectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectRef("123");
    }

    @Test
    public void testWriteAsIdWithNonNullIdAndAlwaysAsId() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.id = "123";
        objectId.idWritten = false;
        w.alwaysAsId = true;

        assertTrue(objectId.writeAsId(gen, provider, w));

        verify(w.serializer).serialize("123", gen, provider);
    }

    @Test
    public void testWriteAsIdWithCanWriteObjectId() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.id = "123";
        objectId.idWritten = false;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, true);

        assertTrue(objectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectId("123");
    }

    @Test
    public void testWriteAsIdWithNullIdAndIdWritten() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.idWritten = true;

        assertFalse(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithNullIdAndAlwaysAsId() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.idWritten = false;
        w.alwaysAsId = true;

        assertFalse(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithCanWriteObjectIdFalse() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.id = "123";
        objectId.idWritten = false;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, false);

        assertFalse(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();

        objectId.id = null;

        assertEquals(forPojo, objectId.generateId(forPojo));
        assertEquals(forPojo, objectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenAndCanWriteObjectId() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        SerializableString name = mock(SerializableString.class);

        objectId.idWritten = true;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, true);

        objectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId(String.valueOf(objectId.id));
    }

    @Test
    public void testWriteAsFieldWithNameNotNull() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        SerializableString name = mock(SerializableString.class);

        objectId.idWritten = true;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, false);
        w.propertyName = name;

        objectId.writeAsField(gen, provider, w);

        verify(gen).writeFieldName(name);
        verify(w.serializer).serialize(objectId.id, gen, provider);
    }

    @Test
    public void testWriteAsFieldWithIdNotWrittenAndCanWriteObjectId() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        objectId.idWritten = false;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, true);

        objectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId(String.valueOf(objectId.id));
    }

    @Test
    public void testWriteAsFieldWithNameNull() throws IOException {
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        SerializableString name = null;

        objectId.idWritten = true;
        gen.setFeature(JsonGenerator.Feature.WRITE_OBJECT_ID, false);
        w.propertyName = name;

        objectId.writeAsField(gen, provider, w);

        verify(w.serializer).serialize(objectId.id, gen, provider);
    }
}
