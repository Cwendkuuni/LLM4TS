package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class WritableObjectIdTest {
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = mock(ObjectIdWriter.class);
        when(provider.getGenerator()).thenReturn(gen);
        WritableObjectId writableObjectId = new WritableObjectId(null);
        this.writableObjectId = writableObjectId;
    }

    @Test
    public void testWriteAsId() throws IOException {
        Object id = new Object();
        when(w.alwaysAsId).thenReturn(false);
        writableObjectId.generator = mock(ObjectIdGenerator.class);
        when(writableObjectId.generator.generateId(any())).thenReturn(id);

        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertTrue(result);
        verify(gen, times(1)).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_GeneratorCanWriteObjectId() throws IOException {
        Object id = new Object();
        when(w.alwaysAsId).thenReturn(false);
        writableObjectId.generator = mock(ObjectIdGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertTrue(result);
        verify(gen, times(1)).writeObjectId(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_GeneratorCannotWriteObjectId() throws IOException {
        Object id = new Object();
        when(w.alwaysAsId).thenReturn(false);
        writableObjectId.generator = mock(ObjectIdGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, w);
        assertFalse(result);
        verify(w, times(1)).serializer.serialize(eq(id), isA(JsonGenerator.class), any());
    }

    @Test
    public void testWriteAsId_WithNullJsonGenerator() throws IOException {
        Object id = new Object();
        when(w.alwaysAsId).thenReturn(false);
        WritableObjectId writableObjectId = new WritableObjectId(null);

        boolean result = writableObjectId.writeAsId(null, provider, w);
        assertFalse(result);
        verify(w, times(1)).serializer.serialize(eq(id), isA(JsonGenerator.class), any());
    }

    @Test
    public void testGenerateId_WithNullForPojo() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        when(writableObjectId.generator.generateId(any())).thenThrow(IllegalArgumentException.class);

        Object result = writableObjectId.generateId(null);
        assertNull(result);
    }

    @Test
    public void testWriteAsField_WithNullJsonGenerator() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(null);

        writableObjectId.writeAsField(null, provider, w);
        verify(w, never()).serializer.serialize(any(), isA(JsonGenerator.class), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateId_WithNullForPojoAndGeneratorThrowsIllegalArgumentException() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        when(writableObjectId.generator.generateId(any())).thenThrow(IllegalArgumentException.class);

        writableObjectId.generateId(null);
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        Object id = writableObjectId.generateId(new Object());
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object id = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen, times(1)).writeFieldName(anyString());
        verify(w, times(1)).serializer.serialize(eq(id), isA(JsonGenerator.class), any());
    }
}