package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private static final Object DUMMY_VALUE = new Object();
    private static final Class<String> STRING_CLASS = String.class;
    private static final Class<Integer> INTEGER_CLASS = Integer.class;

    @Test
    public void testSerialization_ObjectToObject() throws JsonProcessingException, IOException {
        // Given
        Converter<Object, String> converter = new Converter.StdConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return src.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        // When
        String result = serializeAndReadValue(serializer, DUMMY_VALUE);

        // Then
        Assert.assertEquals("", result);
    }

    @Test
    public void testSerialization_ObjectToString() throws JsonProcessingException, IOException {
        // Given
        Converter<Object, String> converter = new Converter.StdConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return src.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(STRING_CLASS, converter);

        // When
        String result = serializeAndReadValue(serializer, DUMMY_VALUE);

        // Then
        Assert.assertEquals("", result);
    }

    @Test
    public void testSerialization_ObjectToInteger() throws JsonProcessingException, IOException {
        // Given
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return 42;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(INTEGER_CLASS, converter);

        // When
        Integer result = serializeAndReadValueAsType(serializer, DUMMY_VALUE, Integer.class);

        // Then
        Assert.assertEquals(42, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialization_ObjectToSameType() throws JsonProcessingException, IOException {
        // Given
        Converter<Object, Object> converter = new Converter.StdConverter<Object, Object>() {
            @Override
            public Object convert(Object src) {
                return src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, converter);

        // When and Then (expect exception)
        serializer.serialize(DUMMY_VALUE, new JsonGenerator(), new SerializerProvider());
    }

    @Test
    public void testSerializationWithSchema() throws JsonProcessingException, IOException {
        // Given
        Converter<Object, String> converter = new Converter.StdConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return src.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        ObjectNode schemaNode = new ObjectMapper().createSchemaNodeBuilder()
                .setRootSchema(JsonSchema.construct(JsonType.STRING))
                .build();

        // When
        JsonNode result = serializer.getSchema(new SerializerProvider(), null);

        // Then
        Assert.assertEquals(schemaNode, result);
    }

    private Object serializeAndReadValue(StdDelegatingSerializer serializer, Object value)
            throws JsonProcessingException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(value, gen, new SerializerProvider());
        gen.flush();
        StringWriter writer = (StringWriter) gen.getOutputWriter();
        return new ObjectMapper().readValue(writer.getBuffer(), Object.class);
    }

    private <T> T serializeAndReadValueAsType(StdDelegatingSerializer serializer, Object value,
            Class<T> type) throws JsonProcessingException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(value, gen, new SerializerProvider());
        gen.flush();
        StringWriter writer = (StringWriter) gen.getOutputWriter();
        return new ObjectMapper().readValue(writer.getBuffer(), type);
    }
}