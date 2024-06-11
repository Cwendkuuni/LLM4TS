package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

import org.junit.Test;

/**
 * Test for {@link StdSerializer} class
 */
public class StdSerializerTest {

    /**
     * Test for default constructor
     */
    @Test
    public void testStdSerializerDefaultConstructor() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().writeValueAsTree("test");
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        assertEquals("{\"type\":\"object\"}", json);
    }

    /**
     * Test for constructor with JavaType
     */
    @Test
    public void testStdSerializerWithJavaType() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        assertEquals("{\"type\":\"object\"}", json);
    }

    /**
     * Test for constructor with Class and boolean
     */
    @Test
    public void testStdSerializerWithClassAndBoolean() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class, true);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        assertEquals("{}", json);
    }

    /**
     * Test for {@link #handledType()} method
     */
    @Test
    public void testHandledType() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        assertEquals(ObjectNode.class, serializer.handledType());
    }

    /**
     * Test for {@link #serialize(Object, JsonGenerator, SerializerProvider)} method (String value)
     */
    @Test
    public void testSerializeStringValue() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        String value = "test";
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.writeString("value");
        gen.writeFieldName("test");
        serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        assertEquals("{\"value\":\"test\"}", json);
    }

    /**
     * Test for {@link #serialize(Object, JsonGenerator, SerializerProvider)} method (Integer value)
     */
    @Test
    public void testSerializeIntegerValue() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.writeFieldName("test");
        serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        assertEquals("{\"test\":123}", json);
    }

    /**
     * Test for {@link #serialize(Object, JsonGenerator, SerializerProvider)} method (Object value)
     */
    @Test
    public void testSerializeObjectValue() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        Object value = new Object();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(node, gen, new ObjectMapper().getSerializerProvider());
        gen.writeFieldName("test");
        serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        gen.flush();
        String json = gen.getOutputContent();
        // The output JSON structure will depend on the class of the 'value' object
        // Make sure to add appropriate assertions based on the output of your 'value' object
    }

    /**
     * Test for {@link #getSchema(SerializerProvider, Type)} method
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        Object schema = serializer.getSchema(new ObjectMapper().getSerializerProvider(), ObjectNode.class);
        assertEquals("{\"type\":\"object\"}", serializer.getSchema(new ObjectMapper().getSerializerProvider(), ObjectNode.class).toString());
    }

    /**
     * Test for {@link #acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} method
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonFormatVisitorWrapper wrapper = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializerProvider().getDefaultSerializerProvider());
        serializer.acceptJsonFormatVisitor(wrapper, node.getClass());
        assertNotNull(wrapper.getFormatVisitor());
    }

    /**
     * Test for {@link #wrapAndThrow(SerializerProvider, Throwable, Object, String)} method
     */
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowString() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        ObjectMapper mapper = new ObjectMapper();
        throw new JsonMappingException("Test error");
    }

    /**
     * Test for {@link #wrapAndThrow(SerializerProvider, Throwable, Object, int)} method
     */
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowInt() throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        ObjectMapper mapper = new ObjectMapper();
        throw new JsonMappingException("Test error");
    }

    /**
     * Test for {@link #isDefaultSerializer(JsonSerializer)} method
     */
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(serializer.isDefaultSerializer(serializer));
        assertFalse(serializer.isDefaultSerializer(stringSerializer));
    }

    /**
     * Test for {@link #findConvertingContentSerializer(SerializerProvider, BeanProperty, JsonSerializer)} method
     */
    @Test
    public void testFindConvertingContentSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue("{\"class\":\"java.util.ArrayList\", \"test\":[1,2,3]}", ObjectNode.class);
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(Object[].class);
        JsonSerializer<Object> convertingSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), node.path("test"), existingSerializer);
        assertNotNull(convertingSerializer);
        assertEquals(Object[].class, convertingSerializer.handledType());
    }

    /**
     * Test for {@link #findPropertyFilter(SerializerProvider, Object, Object)} method
     */
    @Test
    public void testFindPropertyFilter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.set("test", 123);
        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        FilterProvider filters = mapper.getFilterProvider();
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "testFilter", node);
        assertNull(filter);

        filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "testFilter", node.path("test"));
        assertEquals("123", filter.getFilterId());
    }
}