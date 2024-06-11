package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, null, As.PROPERTY);

        String json = "{\"type\": \"String\", \"data\": \"hello\"}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromObject_MissingTypeProperty() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, null, As.PROPERTY);

        String json = "{\"data\": \"hello\"}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        try {
            deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
            fail("Should have thrown an exception");
        } catch (IOException e) {
            String expectedMessage = String.format("missing property '%s' that is to contain type id (for class %s)", typePropertyName, AsPropertyTypeDeserializer.class.getName());
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromObject_InvalidTypePropertyValue() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, null, As.PROPERTY);

        String json = "{\"type\": 1, \"data\": \"hello\"}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        try {
            deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
            fail("Should have thrown an exception");
        } catch (IOException e) {
            String expectedMessage = "Failed to introspect value of type java.lang.Integer for property 'type'";
            assertEquals(expectedMessage, e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromObject_NativeTypeId() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, null, As.PROPERTY);

        String json = "{\"type\": \"class String\", \"data\": \"hello\"}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromObject_DefaultImpl() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, Integer.class, As.PROPERTY);

        String json = "{\"data\": [1, 2, 3]}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        TokenBuffer tokenBuffer = new TokenBuffer(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(3, list.size());
        assertEquals(Integer.class, list.get(0).getClass());
        assertEquals(Integer.class, list.get(1).getClass());
        assertEquals(Integer.class, list.get(2).getClass());

        // Check that the deserializer reads the entire JSON input
        assertEquals(JsonToken.END_ARRAY, tokenBuffer.getCurrentToken());
    }

    @Test
    public void testDeserializeTypedFromObject_NoTypeId() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, true, null, As.PROPERTY);

        String json = "{\"data\": \"hello\"}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        // Testing the base class behavior
        JavaType javaType = MAPPER.getTypeFactory().constructType(List.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsArrayTypeDeserializer.class);
        AsArrayTypeDeserializer deserializer = new AsArrayTypeDeserializer(javaType, idRes, typePropertyName, true, null);

        String json = "[{\"type\": \"java.util.ArrayList\", \"data\": [1, 2, 3]}]";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromArray(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof List);
        List innerList = (List) list.get(0);
        assertEquals(3, innerList.size());
        assertEquals(Integer.class, innerList.get(0).getClass());
        assertEquals(Integer.class, innerList.get(1).getClass());
        assertEquals(Integer.class, innerList.get(2).getClass());

        // Check that the deserializer reads the entire JSON input
        assertEquals(JsonToken.END_ARRAY, node.get(0).asText());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        // Testing the base class behavior
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = MAPPER.getTypeFactory();
        String typePropertyName = "type";

        JavaType bt = MAPPER.getTypeFactory().constructType(AsArrayTypeDeserializer.class);
        AsArrayTypeDeserializer deserializer = new AsArrayTypeDeserializer(javaType, idRes, typePropertyName, true, null);

        String json = "{\"data\": 42}";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromScalar(MAPPER.getFactory().createParser(node), MAPPER.getDeserializationContext());
        assertEquals(42, result);
    }
}