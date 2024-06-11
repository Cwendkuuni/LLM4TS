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
import com.fasterxml.jackson.test.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdDelegatingSerializerTest extends BaseTest {

    private StdDelegatingSerializer<Object> delegateSerializer;
    private Converter<Object, String> converter;

    @Before
    public void setUp() throws Exception {
        converter = new MinimalJacksonConverter.Std((Class<? extends Object>) Object.class, String.class);
        delegateSerializer = new StdDelegatingSerializer<>(converter);
    }

    @Test
    public void testSerializeObject() throws JsonMappingException, JsonGenerationException {
        String expected = "expected";
        JsonGenerator gen = getTestGenerator();

        delegateSerializer.serialize(expected, gen, null);
        assertEquals(expected, gen.getOutputContentAsString());
    }

    @Test
    public void testSerializeValueWithTypeSerializer() throws JsonMappingException, JsonGenerationException {
        String expected = "expected";
        TypeSerializer typeSerializer = getTestTypeSerializer();
        JsonGenerator gen = getTestGenerator();

        delegateSerializer.serializeWithType(expected, gen, null, typeSerializer);
        assertEquals(expected, gen.getOutputContentAsString());
    }

    @Test
    public void testResolveContextualSerialization() throws Exception {
        JsonSerializer<String> stringSerializer = createMapper().findValueSerializer(String.class);
        JsonSerializer<Number> numberSerializer = createMapper().findValueSerializer(Number.class);

        StdDelegatingSerializer<Object> delegatingSerializer = delegateSerializer.withDelegate(converter, String.class, stringSerializer);
        assertSame(stringSerializer, delegatingSerializer.getDelegatee());

        delegatingSerializer = delegateSerializer.withDelegate(converter, Number.class, numberSerializer);
        assertSame(numberSerializer, delegatingSerializer.getDelegatee());

        delegatingSerializer = delegateSerializer.withDelegate(converter, Object.class, null);
        assertNull(delegatingSerializer.getDelegatee());
    }

    @Test
    public void testAcceptJsonFormatVisitorWrapper() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = createTestJsonFormatVisitorWrapper();
        Object value = new Object();
        delegateSerializer.acceptJsonFormatVisitor(visitor, null);
        assertNull(visitor.getFormatter());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SchemaAware schemaAware = createMapper().findValueSerializer(String.class);
        Object schema = delegateSerializer.withDelegate(converter, String.class, schemaAware).getSchema(null, String.class);
        assertNotNull(schema);
    }
}