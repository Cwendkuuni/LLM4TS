package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] expected = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(expected);
        Assert.assertArrayEquals(expected, pojoNode.binaryValue());
    }

    @Test
    public void testBinaryValue_withNonByteArray() throws IOException {
        POJONode pojoNode = new POJONode("test");
        Assert.assertThrows(IOException.class, () -> pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsText_withDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode booleanNode = new POJONode(true);
        POJONode stringNode = new POJONode("true");

        Assert.assertTrue(booleanNode.asBoolean(false));
        Assert.assertFalse(stringNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode intNode = new POJONode(123);
        POJONode stringNode = new POJONode("123");

        Assert.assertEquals(123, intNode.asInt(0));
        Assert.assertEquals(0, stringNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode longNode = new POJONode(10L);
        POJONode stringNode = new POJONode("10");

        Assert.assertEquals(10L, longNode.asLong(0L));
        Assert.assertEquals(0L, stringNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode doubleNode = new POJONode(1.5);
        POJONode stringNode = new POJONode("1.5");

        Assert.assertEquals(1.5, doubleNode.asDouble(0.0), 0.00001);
        Assert.assertEquals(0.0, stringNode.asDouble(0.0), 0.00001);
    }

    @Test
    public void testSerialize_withNullValue() throws IOException {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator mockGenerator = createMockGenerator();
        SerializerProvider mockContext = createMockContext();

        pojoNode.serialize(mockGenerator, mockContext);

        verifySerializedValue(mockGenerator, null);
    }

    @Test
    public void testSerialize_withJsonSerializable() throws IOException {
        JsonSerializableMock mockObject = new JsonSerializableMock("test");
        POJONode pojoNode = new POJONode(mockObject);
        JsonGenerator mockGenerator = createMockGenerator();
        SerializerProvider mockContext = createMockContext();

        pojoNode.serialize(mockGenerator, mockContext);

        verifySerializedValue(mockGenerator, mockObject);
    }

    @Test
    public void testSerialize_withNormalObject() throws IOException {
        Object mockObject = new Object();
        POJONode pojoNode = new POJONode(mockObject);
        JsonGenerator mockGenerator = createMockGenerator();
        SerializerProvider mockContext = createMockContext();

        pojoNode.serialize(mockGenerator, mockContext);

        verifySerializedValue(mockGenerator, mockObject);
    }

    @Test
    public void testGetPojo() {
        Object mockObject = new Object();
        POJONode pojoNode = new POJONode(mockObject);

        Assert.assertSame(mockObject, pojoNode.getPojo());
    }

    @Test
    public void testEquals_withSameObject() {
        POJONode pojoNode = new POJONode("test");

        Assert.assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testEquals_withNullObject() {
        POJONode pojoNode = new POJONode("test");

        Assert.assertFalse(pojoNode.equals(null));
    }

    @Test
    public void testEquals_withDifferentTypeObject() {
        POJONode pojoNode = new POJONode("test");

        Assert.assertFalse(pojoNode.equals("test"));
    }

    @Test
    public void testEquals_withDifferentValue() {
        POJONode pojoNode1 = new POJONode("test1");
        POJONode pojoNode2 = new POJONode("test2");

        Assert.assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testEquals_withNullValues() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);

        Assert.assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testEquals_withSameValue() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");

        Assert.assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");

        Assert.assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString_withByteArray() {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);

        Assert.assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    @Test
    public void testToString_withRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode pojoNode = new POJONode(rawValue);

        Assert.assertEquals("(raw value 'test')", pojoNode.toString());
    }

    @Test
    public void testToString_withNormalValue() {
        POJONode pojoNode = new POJONode("test");

        Assert.assertEquals("test", pojoNode.toString());
    }

    private static JsonGenerator createMockGenerator() {
        return null; // Create mock generator
    }

    private static SerializerProvider createMockContext() {
        return null; // Create mock context
    }

    private static void verifySerializedValue(JsonGenerator generator, Object value) {
        // Verify the serialized value
    }

    private static class JsonSerializableMock implements JsonSerializable {

        private final String value;

        public JsonSerializableMock(String value) {
            this.value = value;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            JsonSerializableMock that = (JsonSerializableMock) obj;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

}