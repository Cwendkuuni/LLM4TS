package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * JUnit test cases for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectMapper mapper;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdGenerator<?> generator;
    private ObjectIdWriter w;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        gen = Mockito.mock(JsonGenerator.class);
        provider = Mockito.mock(SerializerProvider.class);
        generator = Mockito.mock(ObjectIdGenerator.class);

        StdSerializer<Object> idSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeObject(value);
            }
        };

        w = new ObjectIdWriter(idSerializer, new NameTransformer() {
            @Override
            public SerializableString transform(String name, AnnotatedClass ac) {
                return new SerializableString(name);
            }

            @Override
            public SerializableString transform(String name, AnnotatedField af) {
                return new SerializableString(name);
            }
        });
    }

    @Test
    public void testWritableObjectIdConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        when(generator.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(gen, times(1)).writeObjectRef(String.valueOf(id));

        when(generator.canWriteObjectId()).thenReturn(false);
        w.serializer = mapper.findSerializer(Object.class);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));
        verify(w.serializer, times(1)).serialize(id, gen, provider);
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdWithNullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        when(generator).thenReturn(null);

        writableObjectId.writeAsId(gen, provider, w);
    }

    @Test
    public void testGenerateId() throws JsonMappingException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        Object generatedId = writableObjectId.generateId(forPojo);

        assertNotNull(generatedId);
        verify(generator, times(1)).generateId(forPojo);
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateIdWithNullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        generator = null;

        writableObjectId.generateId(null);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        when(generator.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen, times(1)).writeObjectId(String.valueOf(id));

        when(generator.canWriteObjectId()).thenReturn(false);
        w.serializer = mapper.findSerializer(Object.class);

        writableObjectId.writeAsField(gen, provider, w);
        verify(w.serializer, times(1)).serialize(id, gen, provider);

        w.propertyName = new SerializableString("testPropertyName");

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen, times(1)).writeFieldName(w.propertyName.getOriginal());
        verify(w.serializer, times(1)).serialize(id, gen, provider);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldWithNullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        writableObjectId.writeAsField(gen, provider, w);

        when(generator).thenReturn(null);

        writableObjectId.writeAsField(gen, provider, w);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteAsFieldWithNullWriter() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        writableObjectId.writeAsField(gen, provider, null);
    }

    @Test
    public void testIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        assertFalse(writableObjectId.idWritten);

        writableObjectId.writeAsId(gen, provider, w);

        assertTrue(writableObjectId.idWritten);
    }
}