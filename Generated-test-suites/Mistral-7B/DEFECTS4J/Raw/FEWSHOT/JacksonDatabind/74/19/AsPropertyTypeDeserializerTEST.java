package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test for deserialization from JSON object with type property.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeFromJsonObjectWithTypeProperty() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null, "type", true, String.class, As.PROPERTY);

        String json = "{\"type\":\"String\",\"data\":\"Hello World\"}";
        ObjectNode node = mapper.readTree(json);

        Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());

        assertEquals("Hello World", result);
    }

    /**
     * Test for deserialization from JSON object with missing type property.
     *
     * @throws IOException if deserialization fails
     */
    @Test(expected = IOException.class)
    public void testDeserializeFromJsonObjectWithMissingTypeProperty() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null, "type", true, String.class, As.PROPERTY);

        String json = "{\"data\":\"Hello World\"}";
        ObjectNode node = mapper.readTree(json);

        deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
    }

    /**
     * Test for deserialization from JSON object with invalid type property.
     *
     * @throws IOException if deserialization fails
     */
    @Test(expected = IOException.class)
    public void testDeserializeFromJsonObjectWithInvalidTypeProperty() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null, "type", true, String.class, As.PROPERTY);

        String json = "{\"type\":1,\"data\":\"Hello World\"}";
        ObjectNode node = mapper.readTree(json);

        deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
    }

    /**
     * Test for deserialization from JSON object with type id visible.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeFromJsonObjectWithTypeIdVisible() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null, "type", true, String.class, As.PROPERTY);

        String json = "{\"type\":\"String\",\"data\":\"Hello World\",\"@class\":\"java.lang.String\"}";
        ObjectNode node = mapper.readTree(json);

        Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());

        assertEquals("Hello World", result);
    }

    /**
     * Test for deserialization from JSON array with default implementation.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeFromJsonArrayWithDefaultImplementation() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructTypeListType(Integer.class),
                null, null, false, null, As.PROPERTY);

        String json = "[1,2,3]";
        ObjectNode node = mapper.readTree(json);

        Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());

        assertArrayEquals(new Integer[]{1, 2, 3}, (Object[]) result);
    }

    /**
     * Test for deserialization from JSON array with missing type property.
     *
     * @throws IOException if deserialization fails
     */
    @Test(expected = IOException.class)
    public void testDeserializeFromJsonArrayWithMissingTypeProperty() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructTypeListType(Integer.class),
                null, null, false, null, As.PROPERTY);

        String json = "[1,2,3]";
        ObjectNode node = mapper.readTree(json);

        deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
    }

    /**
     * Test for deserialization from JSON array with invalid type property.
     *
     * @throws IOException if deserialization fails
     */
    @Test(expected = IOException.class)
    public void testDeserializeFromJsonArrayWithInvalidTypeProperty() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructTypeListType(Integer.class),
                null, null, false, null, As.PROPERTY);

        String json = "[1,2,3]";
        ObjectNode node = mapper.readTree(json);

        node.put("type", "String");

        deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
    }

    /**
     * Test for deserialization from JSON array with type id visible.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeFromJsonArrayWithTypeIdVisible() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructTypeListType(Integer.class),
                null, null, false, null, As.PROPERTY);

        String json = "[1,2,3]";
        ObjectNode node = mapper.readTree(json);

        node.put("@class", "java.util.ArrayList");

        Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());

        assertArrayEquals(new Integer[]{1, 2, 3}, (Object[]) result);
    }
}