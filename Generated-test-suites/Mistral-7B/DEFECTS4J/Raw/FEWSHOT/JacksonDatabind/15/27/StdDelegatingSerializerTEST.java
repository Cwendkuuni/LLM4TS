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
    public void testConvertValue() throws IOException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = "test";
        Object delegateValue = serializer.convertValue(value);
        Assert.assertEquals(value, delegateValue);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, Integer> converter = new Converter.Std() {
            @Override
            public Integer convert(Object o) {
                return ((String) o).length();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator gen = mapper.getFactory().createGenerator();
        Object value = "test";
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        Assert.assertEquals(3, gen.getOutputSize());
        gen.close();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class);
        Assert.assertNotNull(schema);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, Integer> converter = new Converter.Std() {
            @Override
            public Integer convert(Object o) {
                return ((String) o).length();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.resolve(provider);
        JsonSerializer<Object> delegateSerializer = serializer.getDelegatee();
        Assert.assertNotNull(delegateSerializer);
    }

    @Test
    public void testIsEmpty() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, Integer> converter = new Converter.Std() {
            @Override
            public Integer convert(Object o) {
                return ((String) o).length();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = null;
        Assert.assertTrue(serializer.isEmpty(mapper.getSerializerProvider(), value));
    }

    @Test(expected = IllegalStateException.class)
    public void testSubclassMustOverrideWithDelegate() throws JsonMappingException {
        class TestSerializer extends StdDelegatingSerializer {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object,?> converter,
                    JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                // empty implementation
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, Integer> converter = new Converter.Std() {
            @Override
            public Integer convert(Object o) {
                return ((String) o).length();
            }
        };
        new TestSerializer(converter, null, null);
    }
}