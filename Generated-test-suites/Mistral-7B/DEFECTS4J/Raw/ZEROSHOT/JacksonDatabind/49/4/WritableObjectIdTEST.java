package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdWriter;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private ObjectMapper objectMapper;
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private Object somePojo;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(somePojo)).thenReturn("testId");
        idWriter = mock(ObjectIdWriter.class);
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);
        when(serializerProvider.findValueSerializer(ClassUtil.findRawClass(WritableObjectId.class), null, null)).thenReturn(new WritableObjectIdSerializer());
        when(serializerProvider.findValueSerializer(ClassUtil.findRawClass(String.class), null, null)).thenReturn(new StringSerializer());
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(String.valueOf(writableObjectId.id))).thenCallRealMethod();

        somePojo = new Object() {};
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(idWriter.alwaysAsId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));

        // Test writing id when it is not null and not already written
        when(jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id))).thenReturn(jsonGenerator);
        WritableObjectId expectedWritableObjectId = new WritableObjectId(idGenerator);
        expectedWritableObjectId.id = "testId";
        WritableObjectId actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);
        assertSame(expectedWritableObjectId, actualWritableObjectId);

        // Test writing id when it is not null and already written
        when(jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id))).thenThrow(IOException.class);
        when(idWriter.propertyName).thenReturn(null);
        actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.id = "testId";
        actualWritableObjectId.idWritten = true;
        actualWritableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);
        assertSame(actualWritableObjectId, actualWritableObjectId);

        // Test writing null id
        actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);
        assertNull(actualWritableObjectId.id);
    }

    @Test
    public void testGenerateId() throws JsonMappingException {
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.getObjectId("testId")).thenReturn(somePojo);
        when(idGenerator.generateId(somePojo)).thenReturn(null);

        writableObjectId.generateId(somePojo);
        assertSame(somePojo, writableObjectId.id);

        when(idGenerator.generateId(somePojo)).thenReturn("testGeneratedId");
        when(idReader.getObjectId("testGeneratedId")).thenThrow(JsonMappingException.class);

        writableObjectId.generateId(somePojo);
        assertSame("testGeneratedId", writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(idWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));

        // Test writing id when it is not null
        when(jsonGenerator.writeFieldName(new SerializableString("testPropertyName"))).thenReturn(jsonGenerator);
        when(jsonGenerator.writeString("testId")).thenReturn(jsonGenerator);
        WritableObjectId expectedWritableObjectId = new WritableObjectId(idGenerator);
        expectedWritableObjectId.id = "testId";
        WritableObjectId actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertSame(expectedWritableObjectId, actualWritableObjectId);

        // Test writing null id
        actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertNull(actualWritableObjectId.id);
    }

    private static class WritableObjectIdSerializer extends StdSerializer<WritableObjectId> {
        public WritableObjectIdSerializer() {
            this(null);
        }

        public WritableObjectIdSerializer(Class<WritableObjectId> t, Converter<Object, Writeable> c, TypeSerializer ts) {
            super(t, c, ts);
        }

        @Override
        public void serialize(WritableObjectId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(value.id);
        }
    }

    private static class StringSerializer extends StdSerializer<String> {
        public StringSerializer() {
            this(null);
        }

        public StringSerializer(Class<String> t, Converter<String, Writeable> c, TypeSerializer ts) {
            super(t, c, ts);
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }
}