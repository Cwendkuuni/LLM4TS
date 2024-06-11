package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };

        Object input = "input value";
        Object delegateValue = serializer.convertValue(input);
        Assert.assertEquals(input, delegateValue);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.resolve(provider);
        Assert.assertNotNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        JsonSerializer<Object> serializedSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(delegateSerializer, serializedSerializer);
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Object input = "input value";
        serializer.serialize(input, gen, mapper.getSerializerProvider());
        Assert.assertEquals("\"input value\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeWithType() throws IOException, JsonGenerationException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Object input = "input value";
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer.serializeWithType(input, gen, mapper.getSerializerProvider(), typeSerializer);
        Assert.assertEquals("\"input value\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new Object()));
        Assert.assertFalse(serializer.isEmpty(""));
        Assert.assertFalse(serializer.isEmpty(new String[]{}));
        Assert.assertTrue(serializer.isEmpty(new String[0]));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        Assert.assertNull(serializer.getSchema(provider, Object.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleStringConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getJsonFormatVisitor(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }
}

class SimpleStringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object input) {
        if (input == null) {
            return null;
        }
        return input.toString();
    }
}