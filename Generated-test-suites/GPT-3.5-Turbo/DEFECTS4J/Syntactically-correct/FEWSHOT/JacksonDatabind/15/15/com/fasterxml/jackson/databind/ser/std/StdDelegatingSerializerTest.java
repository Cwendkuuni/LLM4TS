package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Object value = createValue();

        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Object value = createValue();
        TypeSerializer typeSer = createTypeSerializer();

        serializer.serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        Object value = createValue();

        assertEquals(false, serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyWithProvider() {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        SerializerProvider provider = createSerializerProvider();
        Object value = createValue();

        assertEquals(false, serializer.isEmpty(provider, value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        SerializerProvider provider = createSerializerProvider();
        Type typeHint = createType();

        assertEquals(null, serializer.getSchema(provider, typeHint));
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        SerializerProvider provider = createSerializerProvider();
        Type typeHint = createType();
        boolean isOptional = createOptional();

        assertEquals(null, serializer.getSchema(provider, typeHint, isOptional));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = createStdDelegatingSerializer();
        JsonFormatVisitorWrapper visitor = createJsonFormatVisitorWrapper();
        JavaType typeHint = createJavaType();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    private StdDelegatingSerializer createStdDelegatingSerializer() {
        Converter<Object, ?> converter = createConverter();
        JavaType delegateType = createDelegateType();
        JsonSerializer<?> delegateSerializer = createDelegateSerializer();

        return new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    private Converter<Object, ?> createConverter() {
        // create and return a converter
    }

    private JavaType createDelegateType() {
        // create and return a JavaType object
    }

    private JsonSerializer<?> createDelegateSerializer() {
        // create and return a delegate serializer
    }

    private JsonGenerator createJsonGenerator() {
        // create and return a JSON generator
    }

    private SerializerProvider createSerializerProvider() {
        // create and return a serializer provider
    }

    private Object createValue() {
        // create and return a value object
    }

    private TypeSerializer createTypeSerializer() {
        // create and return a type serializer
    }

    private Type createType() {
        // create and return a Java Type
    }

    private boolean createOptional() {
        // create and return a boolean value
    }

    private JsonFormatVisitorWrapper createJsonFormatVisitorWrapper() {
        // create and return a JsonFormatVisitorWrapper
    }

    private JavaType createJavaType() {
        // create and return a JavaType object
    }
}