package com.example.pojonode; // replace with your package name if necessary

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testConstructor() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = new POJONode("testPOJO");
        Assert.assertNotNull(pojoNode);
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // getNodeType test case

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // asToken test case

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(new Object());
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    // binaryValue test case (with a byte array)

    @Test
    public void testBinaryValueWithByteArray() throws IOException {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        byte[] result = pojoNode.binaryValue();
        Assert.assertArrayEquals(byteArray, result);
    }

    @Test(expected = IOException.class)
    public void testBinaryValueWithNonByteArray() throws IOException {
        POJONode pojoNode = new POJONode(new Object());
        pojoNode.binaryValue();
    }

    // asText test case (with default value)

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("testString");
        Assert.assertEquals("testString", pojoNode.asText());
    }

    // asText test case (with custom default value)

    @Test
    public void testAsTextWithCustomDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    // asBoolean test case (with default value)

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode pojoNode = new POJONode(Boolean.FALSE);
        Assert.assertFalse(pojoNode.asBoolean(true));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(Boolean.TRUE);
        Assert.assertTrue(pojoNode.asBoolean(false));
    }

    // asInt test case (with default value)

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode pojoNode = new POJONode(Integer.valueOf(10));
        Assert.assertEquals(10, pojoNode.asInt(20));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(Integer.valueOf(5));
        Assert.assertEquals(5, pojoNode.asInt(0));
    }

    // asLong test case (with default value)

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode pojoNode = new POJONode(Long.valueOf(20L));
        Assert.assertEquals(20L, pojoNode.asLong(30L));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(Long.valueOf(15L));
        Assert.assertEquals(15L, pojoNode.asLong(0L));
    }

    // asDouble test case (with default value)

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode pojoNode = new POJONode(Double.valueOf(3.14));
        Assert.assertEquals(3.14, pojoNode.asDouble(2.718), 0.01);
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(Double.valueOf(1.618));
        Assert.assertEquals(1.618, pojoNode.asDouble(0.0), 0.01);
    }

    // serialize test case (with JsonSerializable object)

    @Test
    public void testSerializeWithJsonSerializableObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        POJONode pojoNode = new POJONode(new TestSerializableObject());
        pojoNode.serialize(gen, new SerializerProvider());
        Assert.assertEquals("{\"name\":\"TestSerializableObject\",\"value\":1}", gen.getOutput().toString());
    }

    // serialize test case (with non-JsonSerializable object)

    @Test
    public void testSerializeWithNonJsonSerializableObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        POJONode pojoNode = new POJONode(new Object());
        pojoNode.serialize(gen, new SerializerProvider());
        Assert.assertEquals("{}", gen.getOutput().toString());
    }

    // getPojo test case

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode(new Object());
        Assert.assertNotNull(pojoNode.getPojo());
    }

    // equals test case

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(new Object());
        POJONode pojoNode2 = new POJONode(new Object());
        Assert.assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        POJONode pojoNode1 = new POJONode(new Object());
        Object otherObject = new Object();
        Assert.assertFalse(pojoNode1.equals(otherObject));
    }

    // hashCode test case

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode(new Object());
        Object otherObject = new Object();
        Object sameObject = new Object();
        Assert.assertEquals(pojoNode.hashCode(), pojoNode.hashCode());
        Assert.assertEquals(pojoNode.hashCode(), sameObject.hashCode());
        Assert.assertNotEquals(pojoNode.hashCode(), otherObject.hashCode());
    }

    // toString test case

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode("testString");
        Assert.assertEquals("testString", pojoNode.toString());
    }

    // toString test case (with binary value)

    @Test
    public void testToStringWithBinaryValue() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        Assert.assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    // toString test case (with raw value)

    @Test
    public void testToStringWithRawValue() {
        POJONode pojoNode = new POJONode(new RawValue("testRawValue"));
        Assert.assertEquals("(raw value 'testRawValue')", pojoNode.toString());
    }
}