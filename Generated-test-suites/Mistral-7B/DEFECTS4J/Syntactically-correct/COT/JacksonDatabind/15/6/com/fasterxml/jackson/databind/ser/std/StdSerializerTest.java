package com.fasterxml.jackson.databind.ser.std; // make sure the test file is in the same package as the class under test

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.ConverterFactory;
import com.fasterxml.jackson.databind.util.StdDelegatingSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;

/**
 * Test class for StdSerializer
 */
public class StdSerializerTest {

    /**
     * Test for the constructor with a Class argument.
     */
    @Test
    public void testStdSerializerConstructorWithClass() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test for the constructor with a JavaType argument.
     */
    @Test
    public void testStdSerializerConstructorWithJavaType() throws Exception {
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(String.class);
        StdSerializer<String> serializer = new StdSerializer<>(javaType);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test for the constructor with a Class argument and a dummy boolean.
     */
    @Test
    public void testStdSerializerConstructorWithClassAndDummyBoolean() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class, false);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test for the serialize method.
     */
    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        String value = "Hello, World!";
        StringWriter stringWriter = new StringWriter(null);
        JsonGenerator jsonGenerator = mapper.getFactory()
                .createGenerator(stringWriter);
        serializer.serialize(value, jsonGenerator, new SerializerProvider());
        assertEquals(value, stringWriter.toString());
    }

    /**
     * Test for the getSchema method with no arguments.
     */
    @Test
    public void testGetSchemaWithNoArguments() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("string", schema.path("type").asText());
    }

    /**
     * Test for the getSchema method with a Type argument.
     */
    @Test
    public void testGetSchemaWithTypeArgument() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
        assertEquals("string", schema.path("type").asText());
    }

    /**
     * Test for the getSchema method with a boolean argument.
     */
    @Test
    public void testGetSchemaWithIsOptionalArgument() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode schema = (ObjectNode) serializer.getSchema(mapper.getSerializerProvider(), String.class, true);
        assertTrue(schema.path("required").booleanValue());
    }

    /**
     * Test for the acceptJsonFormatVisitor method.
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider()
                .getDefaultSerializerProvider()
                .getFactory()
                .createJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        visitor.expectAnyFormat(null);
    }

    /**
     * Test for the wrapAndThrow method with a null provider.
     */
    @Test(expected = IOException.class)
    public void testWrapAndThrowWithNullProvider() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        Throwable t = new RuntimeException("Test exception");
        serializer.wrapAndThrow(null, t, null, null);
    }

    /**
     * Test for the wrapAndThrow method with a non-JsonMappingException IOException.
     */
    @Test(expected = IOException.class)
    public void testWrapAndThrowWithPlainIOException() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        Throwable t = new FileNotFoundException("Test file not found");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), t, null, null);
    }

    /**
     * Test for the wrapAndThrow method with a JsonMappingException.
     */
    @Test
    public void testWrapAndThrowWithJsonMappingException() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        Throwable t = new JsonMappingException("Test exception");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), t, null, null);
        fail("Expected JsonMappingException");
    }

    /**
     * Test for the isDefaultSerializer method.
     */
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertTrue(serializer.isDefaultSerializer(serializer));
        assertFalse(serializer.isDefaultSerializer(new CustomSerializer<>()));
    }

    /**
     * Test for the findConvertingContentSerializer method.
     */
    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<Object> serializer = new ObjectSerializer();
        ConverterFactory<Object, ObjectNode> converterFactory = mapper.getTypeFactory()
                .constructConverterFactory(ObjectNode.class, Object.class);
        Converter<Object, ObjectNode> converter = converterFactory.getConverter(mapper.getSerializerProvider(), null);
        StdSerializer<ObjectNode> delegateSerializer = new ObjectNodeSerializer();
        StdSerializer<Object> existingSerializer = new StdSerializer<>(Object.class);
        StdSerializer<ObjectNode> serializerWithConverter = new StdDelegatingSerializer<>(converter, ObjectNode.class, existingSerializer);
        Object value = new Object();
        PropertyFilter filter = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(),
                ObjectNode.class.getName(), value);
        assertEquals(serializerWithConverter, filter);
    }

    /**
     * Test for the findPropertyFilter method.
     */
    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullProvider() throws Exception {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = "TestFilter";
        Object valueToFilter = new Object();
        serializer.findPropertyFilter(null, filterId, valueToFilter);
    }

    /**
     * Test for the findPropertyFilter method with a null filterId.
     */
    @Test
    public void testFindPropertyFilterWithNullFilterId() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = null;
        Object valueToFilter = new Object();
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, valueToFilter);
        assertNull(filter);
    }

    /**
     * Test for the findPropertyFilter method with a non-null filterId and a valid filter.
     */
    @Test
    public void testFindPropertyFilterWithValidFilterIdAndFilter() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = "TestFilter";
        Object valueToFilter = new Object();
        PropertyFilter filter = mapper.getFilterProvider().findPropertyFilter(filterId, valueToFilter);
        assertEquals(filter, serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, valueToFilter));
    }

    /**
     * Test for the findPropertyFilter method with a non-null filterId and a null filter.
     */
    @Test
    public void testFindPropertyFilterWithValidFilterIdAndNullFilter() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = "TestFilter";
        Object valueToFilter = new Object();
        PropertyFilter filter = null;
        assertEquals(filter, serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, valueToFilter));
    }
}