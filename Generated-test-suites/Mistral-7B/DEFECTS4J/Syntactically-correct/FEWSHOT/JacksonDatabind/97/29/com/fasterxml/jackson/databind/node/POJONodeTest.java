package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class POJONodeTest {

    // Test for getNodeType method
    @Test
    public void testgetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // Test for asText method with null value
    @Test
    public void testAsTextWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    // Test for asText method with String value
    @Test
    public void testAsTextWithStringValue() {
        POJONode pojoNode = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode.asText());
    }

    // Test for asBoolean method with Boolean value
    @Test
    public void testAsBooleanWithBooleanValue() {
        POJONode pojoNode = new POJONode(Boolean.TRUE);
        assertTrue(pojoNode.asBoolean(false));
    }

    // Test for asBoolean method with default value
    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(true));
    }

    // Test for asInt method with Number value
    @Test
    public void testAsIntWithNumberValue() {
        POJONode pojoNode = new POJONode(123);
        assertEquals(123, pojoNode.asInt(0));
    }

    // Test for asInt method with default value
    @Test
    public void testAsIntWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.asInt(123));
    }

    // Test for asLong method with Number value
    @Test
    public void testAsLongWithNumberValue() {
        POJONode pojoNode = new POJONode(456L);
        assertEquals(456L, pojoNode.asLong(0L));
    }

    // Test for asLong method with default value
    @Test
    public void testAsLongWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0L, pojoNode.asLong(456L));
    }

    // Test for asDouble method with Number value
    @Test
    public void testAsDoubleWithNumberValue() {
        POJONode pojoNode = new POJONode(3.14159265);
        assertEquals(3.14159265, pojoNode.asDouble(0), 0.00001);
    }

    // Test for asDouble method with default value
    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0.0, pojoNode.asDouble(3.14159265), 0.00001);
    }

    // Test for serialize method with null value
    @Test
    public void testSerializeWithNullValue() throws IOException {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        pojoNode.serialize(gen, new DefaultSerializerProvider());
        String json = gen.getOutputStream().toString();
        assertEquals("null", json);
    }

    // Test for serialize method with JsonSerializable value
    @Test
    public void testSerializeWithJsonSerializableValue() throws IOException {
        class TestClass implements JsonSerializable {
            private int value;

            public TestClass(int value) {
                this.value = value;
            }

            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }
        }

        TestClass testClass = new TestClass(789);
        POJONode pojoNode = new POJONode(testClass);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        pojoNode.serialize(gen, new DefaultSerializerProvider());
        String json = gen.getOutputStream().toString();
        assertEquals("789", json);
    }

    // Test for serialize method with Object value
    @Test
    public void testSerializeWithObjectValue() throws IOException {
        String testString = "TestObject";
        POJONode pojoNode = new POJONode(testString);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        pojoNode.serialize(gen, new DefaultSerializerProvider());
        String json = gen.getOutputStream().toString();
        assertEquals("\"TestObject\"", json);
    }

    // Test for equals method with same POJONode
    @Test
    public void testEqualsWithSamePOJONode() {
        Object testObject = new Object();
        POJONode pojoNode1 = new POJONode(testObject);
        POJONode pojoNode2 = new POJONode(testObject);
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    // Test for equals method with different POJONode
    @Test
    public void testEqualsWithDifferentPOJONode() {
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        POJONode pojoNode1 = new POJONode(testObject1);
        POJONode pojoNode2 = new POJONode(testObject2);
        assertFalse(pojoNode1.equals(pojoNode2));
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        Object testObject = new Object();
        POJONode pojoNode1 = new POJONode(testObject);
        POJONode pojoNode2 = new POJONode(testObject);
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    // Test for toString method with null value
    @Test
    public void testToStringWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("(null)", pojoNode.toString());
    }

    // Test for toString method with byte[] value
    @Test
    public void testToStringWithByteArrayValue() {
        byte[] testByteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(testByteArray);
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    // Test for toString method with RawValue value
    @Test
    public void testToStringWithRawValueValue() {
        RawValue rawValue = new RawValue("Test String");
        POJONode pojoNode = new POJONode(rawValue);
        assertEquals("(raw value 'Test String')", pojoNode.toString());
    }

    // Test for toString method with Object value
    @Test
    public void testToStringWithObjectValue() {
        Object testObject = new Object();
        POJONode pojoNode = new POJONode(testObject);
        assertEquals(testObject.toString(), pojoNode.toString());
    }
}