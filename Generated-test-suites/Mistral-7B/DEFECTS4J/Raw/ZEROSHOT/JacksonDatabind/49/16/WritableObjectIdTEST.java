package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @SuppressWarnings("unchecked")
    private static final ObjectIdGenerator<Object> idGenerator = new IdIntGenerator();

    @SuppressWarnings("unchecked")
    private static final ObjectIdWriter idWriter = new ObjectIdWriter() {

        @Override
        public void serialize(Object id, JsonGenerator gen, SerializerProvider provider) throws IOException {
            SerializableString name = new SerializableString("id");
            gen.writeFieldName(name);
            gen.writeNumber(id.hashCode());
        }

        @Override
        public String getPropertyName() {
            return "id";
        }
    };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(mapper.getDefaultSerializerFactory().withSerializerFactory(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Empty implementation to avoid default serialization
            }
        }));
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNotNull(writableObjectId);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    /**
     * Test writeAsId method when id is null.
     */
    @org.junit.Test
    public void testWriteAsId_idIsNull() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = createJsonGenerator();

        assertFalse(writableObjectId.writeAsId(gen, new SerializerProvider(), idWriter));
        assertNull(gen.getLastValue());
    }

    /**
     * Test writeAsId method when idWritten is true.
     */
    @org.junit.Test
    public void testWriteAsId_idWrittenIsTrue() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        writableObjectId.generateId(forPojo);

        JsonGenerator gen = createJsonGenerator();

        writableObjectId.writeAsId(gen, new SerializerProvider(), idWriter);

        assertEquals("\"id\"", gen.getLastValueAsString());
    }

    /**
     * Test writeAsId method when idWritten is false and alwaysAsId is true.
     */
    @org.junit.Test
    public void testWriteAsId_idWrittenIsFalseAndAlwaysAsIdIsTrue() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        writableObjectId.id = "existingId";

        JsonGenerator gen = createJsonGenerator();

        writableObjectId.writeAsId(gen, new SerializerProvider(), idWriter);

        assertEquals("\"existingId\"", gen.getLastValueAsString());
    }

    /**
     * Test writeAsId method when idWritten is false and alwaysAsId is false.
     */
    @org.junit.Test
    public void testWriteAsId_idWrittenIsFalseAndAlwaysAsIdIsFalse() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonGenerator gen = createJsonGenerator();

        assertFalse(writableObjectId.writeAsId(gen, new SerializerProvider(), idWriter));
        assertNull(gen.getLastValue());
    }

    /**
     * Test generateId method.
     */
    @org.junit.Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();

        Object generatedId = writableObjectId.generateId(forPojo);
        assertNotNull(generatedId);
    }

    /**
     * Test writeAsField method when idWritten is false.
     */
    @org.junit.Test
    public void testWriteAsField_idWrittenIsFalse() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        writableObjectId.generateId(forPojo);

        JsonGenerator gen = createJsonGenerator();

        writableObjectId.writeAsField(gen, new SerializerProvider(), idWriter);

        ObjectNode jsonNode = (ObjectNode) createJsonFactory().createValue(gen.getOutputBuffer().toString());
        assertEquals("\"id\"", jsonNode.get("@class").asText());
        assertEquals("id", jsonNode.get("id").asText());
    }

    /**
     * Test writeAsField method when idWritten is true.
     */
    @org.junit.Test
    public void testWriteAsField_idWrittenIsTrue() throws JsonProcessingException, JsonGenerationException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object forPojo = new Object();
        writableObjectId.generateId(forPojo);
        writableObjectId.writeAsId(createJsonGenerator(), new SerializerProvider(), idWriter);

        JsonGenerator gen = createJsonGenerator();

        writableObjectId.writeAsField(gen, new SerializerProvider(), idWriter);

        ObjectNode jsonNode = (ObjectNode) createJsonFactory().createValue(gen.getOutputBuffer().toString());
        assertEquals("\"id\"", jsonNode.get("@class").asText());
        assertEquals("id", jsonNode.get("id").asText());
    }

    private JsonGenerator createJsonGenerator() throws JsonProcessingException {
        return createJsonFactory().createGenerator(new MockOutputStream());
    }

    private ObjectMapper createJsonFactory() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(mapper.getDefaultSerializerFactory().withSerializerFactory(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Empty implementation to avoid default serialization
            }
        }));
        return mapper;
    }
}