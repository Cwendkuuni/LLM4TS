package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

/**
 * Comprehensive testing for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    /**
     * Test deserialization with default deserializer.
     */
    @Test
    public void testDefaultDeserializer() throws Exception {
        StringJsonParser jp = new StringJsonParser();
        ObjectMapper mapper = new ObjectMapper();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext(mapper, new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"element1", "element2", "element3"}, result);
    }

    /**
     * Test deserialization with custom deserializer.
     */
    @Test
    public void testCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        StringJsonParser jp = new StringJsonParser(mapper.writeValueAsString(new Object[]{"customDeserializer:element1", "customDeserializer:element2"}));
        ObjectDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(mapper, new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"customDeserializer:element1", "customDeserializer:element2"}, result);
    }

    /**
     * Test deserialization with empty array.
     */
    @Test
    public void testEmptyArray() throws Exception {
        StringJsonParser jp = new StringJsonParser("[]");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test deserialization with null value.
     */
    @Test
    public void testNullValue() throws Exception {
        StringJsonParser jp = new StringJsonParser("null");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[]{null}, result);
    }

    /**
     * Test deserialization with single value.
     */
    @Test
    public void testSingleValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonParser jp = new StringJsonParser(mapper.writeValueAsString(new Object[]{"singleValue"}));
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext(mapper, new StandardDeserializationContext()));
        assertEquals(new String[]{"singleValue"}, result);
    }

    /**
     * Test deserialization with custom type identifier.
     */
    @Test
    public void testCustomTypeIdentifier() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getFactory().createArrayTypeDeserializer(new TypeReference<String[]>() {});
        StringJsonParser jp = new StringJsonParser(mapper.writeValueAsString(new Object[]{"element1", "element2"}));
        String[] result = StringArrayDeserializer.instance.deserializeWithType(jp, new DeserializationContext(mapper, new StandardDeserializationContext()), typeDeserializer);
        assertArrayEquals(new String[]{"element1", "element2"}, result);
    }

    /**
     * Test handling of non-array input.
     */
    @Test
    public void testHandleNonArray() throws Exception {
        StringJsonParser jp = new StringJsonParser("123");
        String[] result = StringArrayDeserializer.instance.handleNonArray(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"123"}, result);
    }

    /**
     * Test contextual deserialization.
     */
    @Test
    public void testContextualDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        ObjectDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        BeanProperty property = new BeanProperty("testProperty", String[].class);
        ObjectDeserializer contextualDeserializer = deserializer.createContextual(new DeserializationContext(mapper, new StandardDeserializationContext()), property);
        StringJsonParser jp = new StringJsonParser("{\"testProperty\":[\"customDeserializer:element1\", \"customDeserializer:element2\"]}");
        String[] result = contextualDeserializer.deserialize(jp, new DeserializationContext(mapper, new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"customDeserializer:element1", "customDeserializer:element2"}, result);
    }
}