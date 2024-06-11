package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link StdKeyDeserializer} class.
 */
public class StdKeyDeserializerTest {

    /**
     * Test deserialization for boolean key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testBooleanKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":true}");
        Object key = mapper.treeToValue(node, Boolean.TYPE);
        assertEquals(Boolean.TRUE, key);

        node = mapper.readTree("{\"key\":false}");
        key = mapper.treeToValue(node, Boolean.TYPE);
        assertEquals(Boolean.FALSE, key);
    }

    /**
     * Test deserialization for byte key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testByteKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1}");
        Object key = mapper.treeToValue(node, Byte.TYPE);
        assertEquals((byte) 1, key);

        node = mapper.readTree("{\"key\":255}");
        key = mapper.treeToValue(node, Byte.TYPE);
        assertEquals((byte) 255, key);

        node = mapper.readTree("{\"key\":-1}"); // should fail due to negative value
        try {
            key = mapper.treeToValue(node, Byte.TYPE);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test deserialization for short key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testShortKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1}");
        Object key = mapper.treeToValue(node, Short.TYPE);
        assertEquals((short) 1, key);

        node = mapper.readTree("{\"key\":32767}");
        key = mapper.treeToValue(node, Short.TYPE);
        assertEquals((short) 32767, key);

        node = mapper.readTree("{\"key\":32768}"); // should fail due to negative value
        try {
            key = mapper.treeToValue(node, Short.TYPE);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test deserialization for char key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testCharKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":'a'}");
        Object key = mapper.treeToValue(node, Character.TYPE);
        assertEquals('a', key);
    }

    /**
     * Test deserialization for int key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testIntKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1}");
        Object key = mapper.treeToValue(node, Integer.TYPE);
        assertEquals(1, key);
    }

    /**
     * Test deserialization for long key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testLongKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1L}");
        Object key = mapper.treeToValue(node, Long.TYPE);
        assertEquals(1L, key);

        node = mapper.readTree("{\"key\":Long.MAX_VALUE}");
        key = mapper.treeToValue(node, Long.TYPE);
        assertEquals(Long.MAX_VALUE, key);
    }

    /**
     * Test deserialization for float key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testFloatKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1.0f}");
        Object key = mapper.treeToValue(node, Float.TYPE);
        assertEquals(1.0f, key);
    }

    /**
     * Test deserialization for double key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testDoubleKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":1.0}");
        Object key = mapper.treeToValue(node, Double.TYPE);
        assertEquals(1.0, key);
    }

    /**
     * Test deserialization for Locale key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testLocaleKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectKeyDeserializer localeKeyDeserializer = new ObjectKeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                try {
                    return Locale.forLanguageTag(key);
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdKeyException(Locale.class, key, "Invalid locale tag: " + key);
                }
            }
        };
        JsonNode node = mapper.readTree("{\"key\":\"en_US\"}");
        Object key = mapper.treeToValue(node, Locale.class, localeKeyDeserializer);
        assertEquals(Locale.US, key);
    }

    /**
     * Test deserialization for URI key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testURIKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":\"http://example.com\"}");
        Object key = mapper.treeToValue(node, URI.class);
        assertEquals(new URI("http://example.com"), key);
    }

    /**
     * Test deserialization for URL key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testURLKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":\"http://example.com\"}");
        Object key = mapper.treeToValue(node, URL.class);
        assertEquals(new URL("http://example.com"), key);
    }

    /**
     * Test deserialization for Class key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testClassKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":String.class}");
        Object key = mapper.treeToValue(node, Class.class);
        assertEquals(String.class, key);
    }

    /**
     * Test deserialization for Enum key type.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEnumKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyEnum> enumDeserializer = new JsonDeserializer<MyEnum>() {
            @Override
            public MyEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getText();
                return MyEnum.valueOf(value);
            }
        };
        JsonNode node = mapper.readTree("{\"key\":\"MyEnum.RED\"}");
        Object key = mapper.treeToValue(node, MyEnum.class, enumDeserializer);
        assertEquals(MyEnum.RED, key);
    }

    /**
     * Test deserialization for String key type (using default deserializer).
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testStringKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":\"Hello world\"}");
        Object key = mapper.treeToValue(node, String.class);
        assertEquals("Hello world", key);
    }

    /**
     * Test deserialization for Object key type (using default deserializer).
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testObjectKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass myClass = new MyClass("Hello world");
        ObjectNode node = mapper.createObjectNode();
        node.put("key", "MyClass(\"Hello world\")");
        Object key = mapper.treeToValue(node, Object.class);
        assertEquals(myClass, key);
    }

    /**
     * Test deserialization for null key.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testNullKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":null}");
        Object key = mapper.treeToValue(node, String.class);
        assertNull(key);
    }

    /**
     * Test deserialization for non-string key.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testNonStringKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":123}");
        Object key = mapper.treeToValue(node, String.class);
        assertNull(key);
    }

    // Add more test cases as needed
}