package com.example.numberserializers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static org.junit.jupiter.api.Assertions.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NumberSerializersTest {

    private ObjectMapper objectMapper;
    private Map<String, JsonSerializer<?>> allDeserializers;

    @BeforeAll
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.allDeserializers = NumberSerializers.addAll(new java.util.HashMap<>());
    }

    @Test
    public void testAddAllMethod() throws Exception {
        Assertions.assertNotNull(allDeserializers);
        Assertions.assertEquals(12, allDeserializers.size());

        Assertions.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assertions.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assertions.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assertions.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assertions.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        Assertions.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assertions.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws Exception {
        Short testShort = Short.MIN_VALUE;
        JsonNode jsonNode = this.objectMapper.writeValueAsTree(testShort);
        ObjectNode jsonObject = (ObjectNode) jsonNode;
        String actualType = jsonObject.get("@class").asText();
        assertEquals("java.lang.Short", actualType);
        assertEquals(-32768, jsonNode.get(0).asInt());
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        Integer testInteger = Integer.MIN_VALUE;
        JsonNode jsonNode = this.objectMapper.writeValueAsTree(testInteger);
        ObjectNode jsonObject = (ObjectNode) jsonNode;
        String actualType = jsonObject.get("@class").asText();
        assertEquals("java.lang.Integer", actualType);
        assertEquals(-2147483648, jsonNode.get(0).asInt());

        // Test Serialization of a positive integer
        Integer positiveInteger = 12345;
        JsonNode positiveJsonNode = this.objectMapper.writeValueAsTree(positiveInteger);
        ObjectNode positiveJsonObject = (ObjectNode) positiveJsonNode;
        String positiveActualType = positiveJsonObject.get("@class").asText();
        assertEquals("java.lang.Integer", positiveActualType);
        assertEquals(12345, positiveJsonNode.get(0).asInt());

        // Test Serialization of a zero integer
        Integer zeroInteger = 0;
        JsonNode zeroJsonNode = this.objectMapper.writeValueAsTree(zeroInteger);
        ObjectNode zeroJsonObject = (ObjectNode) zeroJsonNode;
        String zeroActualType = zeroJsonObject.get("@class").asText();
        assertEquals("java.lang.Integer", zeroActualType);
        assertEquals(0, zeroJsonNode.get(0).asInt());

        // Test Serialization of a large positive integer
        Integer largePositiveInteger = Integer.MAX_VALUE;
        JsonNode largePositiveJsonNode = this.objectMapper.writeValueAsTree(largePositiveInteger);
        ObjectNode largePositiveJsonObject = (ObjectNode) largePositiveJsonNode;
        String largePositiveActualType = largePositiveJsonObject.get("@class").asText();
        assertEquals("java.lang.Integer", largePositiveActualType);
        assertEquals(2147483647, largePositiveJsonNode.get(0).asInt());

        // Test Serialization of a String
        String testString = "testString";
        JsonNode stringJsonNode = this.objectMapper.writeValueAsTree(testString);
        ObjectNode stringJsonObject = (ObjectNode) stringJsonNode;
        String stringActualType = stringJsonObject.get("@class").asText();
        assertNotEquals("java.lang.Integer", stringActualType);
    }
}

//