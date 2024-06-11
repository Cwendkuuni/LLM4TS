package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    private Converter<Object, ?> converter;
    private StdDelegatingSerializer delegatingSerializer;

    @Before
    public void setup() {
        converter = new Converter.Std() {};
        delegatingSerializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testSerializeObjectWithDelegateSerializer() throws IOException {
        String input = "input";
        Object value = new Object() {};

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        delegatingSerializer.serialize(value, gen, new DefaultSerializerProvider());
        gen.flush();

        Assert.assertEquals("{\"@class\":\"[B\",\"@type\":\"java.lang.Object\"]}", gen.getOutputBuffer().toString());

        gen.close();

        delegatingSerializer = new StdDelegatingSerializer(converter, Object.class, new StdSerializer<Object>(Object.class) {});
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        delegatingSerializer.serialize(value, gen, new DefaultSerializerProvider());
        gen.flush();

        Assert.assertEquals("{}", gen.getOutputBuffer().toString());

        gen.close();

        delegatingSerializer = new StdDelegatingSerializer(converter, String.class, new StdSerializer<String>(String.class) {});
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        delegatingSerializer.serialize(input, gen, new DefaultSerializerProvider());
        gen.flush();

        Assert.assertEquals("\"input\"", gen.getOutputBuffer().toString());

        gen.close();
    }

    @Test
    public void testSerializeObjectWithNullDelegateSerializer() throws IOException {
        Object value = null;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        delegatingSerializer.serialize(value, gen, new DefaultSerializerProvider());
        gen.flush();

        Assert.assertEquals("null", gen.getOutputBuffer().toString());

        gen.close();
    }

    @Test
    public void testCreateContextualWithNullDelegateSerializer() throws JsonMappingException {
        JsonSerializer<?> nullDelegateSerializer = null;
        delegatingSerializer = delegatingSerializer.withDelegate(converter, Object.class, nullDelegateSerializer);
        Assert.assertNull(delegatingSerializer.getDelegatee());
    }

    @Test
    public void testCreateContextualWithNonMatchingDelegateSerializerType() throws JsonMappingException {
        JsonSerializer<Integer> intDelegateSerializer = new StdSerializer<Integer>(Integer.class) {};
        delegatingSerializer = delegatingSerializer.withDelegate(converter, String.class, intDelegateSerializer);
        Assert.assertNotNull(delegatingSerializer.getDelegatee());
        Assert.assertNotEquals(intDelegateSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testCreateContextualWithMatchingDelegateSerializer() throws JsonMappingException {
        JsonSerializer<Object> objectDelegateSerializer = new StdSerializer<Object>(Object.class) {};
        delegatingSerializer = delegatingSerializer.withDelegate(converter, Object.class, objectDelegateSerializer);
        Assert.assertNotNull(delegatingSerializer.getDelegatee());
        Assert.assertEquals(objectDelegateSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdConfig().withSerializableFormatVisitor(null, null);
        delegatingSerializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonFactory jsonFactory = new JsonFactory();
        SchemaFactory schemaFactory = jsonFactory.getSchemaFactory();
        Schema schema = schemaFactory.getSchema(Object.class);
        delegatingSerializer = new StdDelegatingSerializer(converter, Object.class, new StdSerializer<Object>(Object.class) {});
        JsonNode schemaNode = delegatingSerializer.getSchema(new DefaultSerializerProvider(), Object.class);
        Assert.assertEquals(schema.toString(), schemaNode.toString());
    }
}