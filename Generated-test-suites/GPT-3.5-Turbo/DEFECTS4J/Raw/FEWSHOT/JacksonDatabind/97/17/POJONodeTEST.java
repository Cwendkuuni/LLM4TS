
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(10);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(10);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] bytes = { 1, 2, 3 };
        POJONode node = new POJONode(bytes);
        assertEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());

        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));

        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nullNode = new POJONode(null);
        assertEquals(false, nullNode.asBoolean(false));

        POJONode booleanNode = new POJONode(true);
        assertEquals(true, booleanNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.asInt(0));

        POJONode intNode = new POJONode(42);
        assertEquals(42, intNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode nullNode = new POJONode(null);
        assertEquals(0L, nullNode.asLong(0L));

        POJONode longNode = new POJONode(1000L);
        assertEquals(1000L, longNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode nullNode = new POJONode(null);
        assertEquals(0.0, nullNode.asDouble(0.0), 0.0);

        POJONode doubleNode = new POJONode(3.14);
        assertEquals(3.14, doubleNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerializeWithNullValue() {
        StubJsonGenerator generator = new StubJsonGenerator();
        StubSerializerProvider serializerProvider = new StubSerializerProvider();

        POJONode nullNode = new POJONode(null);
        nullNode.serialize(generator, serializerProvider);

        assertEquals(true, generator.defaultSerializeNullCalled);
    }

    @Test
    public void testSerializeWithJsonSerializableValue() {
        StubJsonGenerator generator = new StubJsonGenerator();
        StubSerializerProvider serializerProvider = new StubSerializerProvider();

        JsonSerializable value = new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Custom serialization logic here
            }
        };

        POJONode valueNode = new POJONode(value);
        valueNode.serialize(generator, serializerProvider);

        assertEquals(true, generator.writeObjectCalled);
    }
    
    @Test
    public void testSerializeWithValue() {
        StubJsonGenerator generator = new StubJsonGenerator();
        StubSerializerProvider serializerProvider = new StubSerializerProvider();

        Object value = "hello";

        POJONode valueNode = new POJONode(value);
        valueNode.serialize(generator, serializerProvider);

        assertEquals(true, generator.writeObjectCalled);
    }

    @Test
    public void testGetPojo() {
        Object value = "hello";

        POJONode valueNode = new POJONode(value);
        assertEquals(value, valueNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(10);
        POJONode node2 = new POJONode(10);
        assertEquals(true, node1.equals(node2));

        POJONode node3 = new POJONode(20);
        assertEquals(false, node1.equals(node3));

        assertEquals(false, node1.equals(null));
        assertEquals(false, node1.equals("hello"));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        POJONode stringNode = new POJONode("hello");
        assertEquals("hello", stringNode.toString());

        byte[] bytes = { 1, 2, 3 };
        POJONode binaryNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());

        RawValue rawValue = new RawValue("raw value");
        POJONode rawValueNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw value')", rawValueNode.toString());
    }
}
