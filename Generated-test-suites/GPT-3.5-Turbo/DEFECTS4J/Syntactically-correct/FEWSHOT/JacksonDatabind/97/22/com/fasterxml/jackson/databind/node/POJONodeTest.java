
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

public class POJONodeTest {
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());

        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));

        POJONode valueNode = new POJONode("test");
        assertEquals("test", valueNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode trueNode = new POJONode(true);
        assertEquals(true, trueNode.asBoolean(false));

        POJONode falseNode = new POJONode(false);
        assertEquals(false, falseNode.asBoolean(true));

        POJONode nullNode = new POJONode(null);
        assertEquals(true, nullNode.asBoolean(true));
    }
    
    @Test
    public void testAsInt() {
        POJONode intNode = new POJONode(10);
        assertEquals(10, intNode.asInt(0));

        POJONode nullNode = new POJONode(null);
        assertEquals(5, nullNode.asInt(5));
    }

    @Test
    public void testAsLong() {
        POJONode longNode = new POJONode(100L);
        assertEquals(100L, longNode.asLong(0L));

        POJONode nullNode = new POJONode(null);
        assertEquals(50L, nullNode.asLong(50L));
    }

    @Test
    public void testAsDouble() {
        POJONode doubleNode = new POJONode(3.14);
        assertEquals(3.14, doubleNode.asDouble(0.0), 0.0);

        POJONode nullNode = new POJONode(null);
        assertEquals(2.71, nullNode.asDouble(2.71), 0.0);
    }

    @Test
    public void testSerialize() {
        POJONode nullNode = new POJONode(null);
        // Test that the generator writes the null value
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        try {
            nullNode.serialize(generator, provider);
            Mockito.verify(generator).writeNull();
        } catch (Exception ex) {
            // Error occurred
            assert false;
        }

        // Test for JsonSerializable implementation
        POJONode serializableNode = new POJONode(new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) {
                try {
                    gen.writeString("serialized");
                } catch (Exception ex) {
                    // Error occurred
                    assert false;
                }
            }
        });

        try {
            serializableNode.serialize(generator, provider);
            Mockito.verify(generator).writeString("serialized");
        } catch (Exception ex) {
            // Error occurred
            assert false;
        }

        // Test writeObject is called when value is not null or JsonSerializable
        POJONode objectNode = new POJONode(new Object());
        try {
            objectNode.serialize(generator, provider);
            Mockito.verify(generator).writeObject(objectNode.getPojo());
        } catch (Exception ex) {
            // Error occurred
            assert false;
        }
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("different");

        // Same object reference
        assertSame(node1, node1);

        // Same value
        assertSame(node1, node2);

        // Different value
        assert !node1.equals(node3);

        // Different class
        assert !node1.equals(new Object());
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("different");

        assertEquals(node1.hashCode(), node2.hashCode());

        assert node1.hashCode() != node3.hashCode();
    }

    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        POJONode byteArrayNode = new POJONode(new byte[10]);
        assertEquals("(binary value of 10 bytes)", byteArrayNode.toString());

        POJONode rawValueNode = new POJONode(new RawValue("value"));
        assertEquals("(raw value 'value')", rawValueNode.toString());

        POJONode stringNode = new POJONode("test");
        assertEquals("test", stringNode.toString());
    }
}
