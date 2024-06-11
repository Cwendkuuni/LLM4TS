package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertEquals(generator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsIdNormalCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        writableObjectId.id = id;

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsIdIdIsNullCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdIdWrittenTrueCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdAlwaysAsIdTrueCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        writableObjectId.id = id;

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();
        objectIdWriter.alwaysAsId = true;

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        verify(jsonGenerator).writeObjectRef(eq(String.valueOf(id)));
    }

    @Test
    public void testGenerateIdNormalCase() {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);
        assertEquals(generator.generateId(forPojo), id);
    }

    @Test
    public void testWriteAsFieldNormalCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        writableObjectId.id = id;

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();
        SerializableString name = new SerializableString("testName");

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeFieldName(eq(name));
        verify(objectIdWriter.serializer).serialize(eq(id), eq(jsonGenerator), any(SerializerProvider.class));
    }

    @Test
    public void testWriteAsFieldNullNameCase() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = "testId";
        writableObjectId.id = id;

        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verifyNoMoreInteractions(jsonGenerator);
        verifyNoMoreInteractions(objectIdWriter.serializer);
    }

    private JsonGenerator createMockJsonGenerator() {
        return createMock(JsonGenerator.class);
    }

    private ObjectIdWriter createMockObjectIdWriter() {
        return createMock(ObjectIdWriter.class);
    }

    private SerializerProvider createMockSerializerProvider() {
        return createMock(SerializerProvider.class);
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private void writeToJson(Object src, String outputFilePath) throws IOException {
        ObjectMapper objectMapper = createObjectMapper();
        objectMapper.writeValue(new File(outputFilePath), src);
    }

    private void verifyJsonSchema(String inputFilePath, String outputFilePath, JsonSchema expectedSchema) throws IOException {
        ObjectMapper objectMapper = createObjectMapper();
        JsonSchema actualSchema = objectMapper.getDefaultValidator().validateAndGetSchema(new File(outputFilePath));
        assertEquals(expectedSchema, actualSchema);
    }
}