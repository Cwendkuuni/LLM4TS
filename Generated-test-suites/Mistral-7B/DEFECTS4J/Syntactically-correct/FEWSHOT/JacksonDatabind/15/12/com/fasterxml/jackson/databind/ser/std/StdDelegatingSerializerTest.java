package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
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
    public void testConvertValue() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object input = new Integer(42);
        Object delegateValue = serializer.convertValue(input);
        Assert.assertEquals(42, delegateValue);

        input = new String("42");
        delegateValue = serializer.convertValue(input);
        Assert.assertEquals(42, delegateValue);
    }

    @Test
    public void testSerialize() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        JsonSerializer<Integer> delegateSerializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, new ClassType(Integer.class), delegateSerializer);

        Object input = new Integer(42);
        byte[] result = new byte[512];

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(result));
        SerializerProvider serializers = new StandardSerializerFinder().findValueSerializer(null, new JavaClass(Integer.class), null);
        serializer.serialize(input, gen, serializers);
        gen.close();

        Assert.assertEquals("42", new String(result));

        input = new String("42");
        result = new byte[512];
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(result));
        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer = serializer.withDelegate(converter, new ClassType(Integer.class), delegateSerializer);
        serializer.serialize(input, gen, serializers);
        gen.close();

        Assert.assertEquals("\"42\"", new String(result));
    }

    @Test
    public void testResolve() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider serializers = new StandardSerializerFinder().findValueSerializer(null, Object.class, null);

        Object input = new Integer(42);
        JsonSerializer<?> delegateSerializer = serializers.findValueSerializer(Integer.class);

        serializer.resolve(serializers);
        Assert.assertEquals(delegateSerializer, serializer.getDelegatee());

        input = new String("42");
        serializers = new StandardSerializerFinder().findValueSerializer(null, String.class, null);
        delegateSerializer = serializers.findValueSerializer(Integer.class);
        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer = serializer.withDelegate(converter, new ClassType(Integer.class), delegateSerializer);
        serializer.resolve(serializers);
        Assert.assertEquals(delegateSerializer, serializer.getDelegatee());
    }

    @Test
    public void testCreateContextual() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        JsonSerializer<Integer> delegateSerializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        SerializerProvider serializers = new StandardSerializerFinder().findValueSerializer(null, Object.class, null);

        JsonSerializer<?> result = serializer.createContextual(serializers, null);
        Assert.assertEquals(delegateSerializer, result);

        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer = serializer.withDelegate(converter, new ClassType(Integer.class), delegateSerializer);
        result = serializer.createContextual(serializers, null);
        Assert.assertEquals(delegateSerializer, result);
    }

    @Test
    public void testGetSchema() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Assert.assertNull(serializer.getSchema(new StandardSerializerFinder(), null));

        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer = serializer.withDelegate(converter, new ClassType(Integer.class), null);

        JsonNode result = serializer.getSchema(new StandardSerializerFinder(), null);
        Assert.assertNull(result);

        result = serializer.getSchema(new StandardSerializerFinder(), new ClassType(Integer.class));
        Assert.assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) throws Exception {
                return (Integer) src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {};
        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonFormatVisitorWrapper visitor2 = new JsonFormatVisitorWrapper() {
            @Override
            public JsonNode visitObjectField(String name, JsonSerializer<?> serializer) throws IOException {
                throw new RuntimeException("Unexpected call to visitObjectField");
            }
        };
        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer = serializer.withDelegate(converter, new ClassType(Integer.class), null);
        serializer.acceptJsonFormatVisitor(visitor2, null);
    }
}