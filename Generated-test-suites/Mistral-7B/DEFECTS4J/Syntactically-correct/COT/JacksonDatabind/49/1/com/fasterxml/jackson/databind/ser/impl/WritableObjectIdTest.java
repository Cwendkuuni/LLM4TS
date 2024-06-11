package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTestBase;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * JUnit test file for WritableObjectId class.
 */
public class WritableObjectIdTest extends ObjectIdGeneratorTestBase {

    private ObjectIdGenerator<?> idGenerator;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() throws Exception {
        idGenerator = createObjectIdGenerator(ObjectIdGenerator.Always.class);
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testConstructor() {
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(true);
        when(w.alwaysAsId).thenReturn(false);

        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen, times(1)).writeObjectRef(anyString());
    }

    @Test
    public void testWriteAsIdWithForwardReference() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.alwaysAsId).thenReturn(false);

        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        assertFalse(writableObjectId.writeAsId(gen, provider, w));
        verify(w, times(1)).serialize(eq(id), any(JsonGenerator.class), any(SerializerProvider.class));
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(true);
        when(w.alwaysAsId).thenReturn(true);

        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen, times(1)).writeObjectRef(anyString());
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdWithNullGenerator() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsId(null, provider, w);
    }

    @Test
    public void testGenerateId() throws IOException {
        Object forPojo = new Object();
        Object id = writableObjectId.generateId(forPojo);

        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen, times(1)).writeObjectId(anyString());
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsField(null, provider, w);
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(gen, provider, w);
        verify(w, times(1)).serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class));
    }

    @Test
    public void testSerializeInvalidId() throws JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        when(provider.findValueSerializer(any(Class.class))).thenReturn(null);
        when(w.propertyName).thenReturn(new SerializableString("testProperty"));

        writableObjectId.writeAsField(gen, provider, w);

        verify(w, times(1)).serialize(null, any(JsonGenerator.class), any(SerializerProvider.class));
        verify(typeSerializer, times(1)).writeNull(eq(gen), any(Object.class), any(SerializerProvider.class));
    }

    @Test
    public void testWriteAsFieldWithNullId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(gen, provider, w);
        verify(w, times(1)).serialize(null, any(JsonGenerator.class), any(SerializerProvider.class));
    }

    @Test
    public void testWriteAsFieldWithNullObjectIdWriter() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writableObjectId.writeAsField(gen, provider, null);
    }
}