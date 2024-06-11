import static org.junit.Assert.*;
import org.junit.Test;

public class POJONodeTest {

    // Test cases for getNodeType() method
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    // Test cases for asToken() method
    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    // Test cases for binaryValue() method
    @Test
    public void testBinaryValue() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        try {
            byte[] bytes = nullNode.binaryValue();
            assertNull(bytes);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when _value is byte[]
        byte[] data = {1, 2, 3};
        POJONode byteArrayNode = new POJONode(data);
        try {
            byte[] bytes = byteArrayNode.binaryValue();
            assertArrayEquals(data, bytes);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when _value is not byte[]
        POJONode intNode = new POJONode(5);
        try {
            intNode.binaryValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    } 
    
    // Test cases for asText() method
    @Test
    public void testAsText() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());

        // Test when _value is not null
        POJONode stringNode = new POJONode("abc");
        assertEquals("abc", stringNode.asText());
    }

    // Test cases for asText(String defaultValue) method
    @Test
    public void testAsTextWithDefaultValue() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));

        // Test when _value is not null
        POJONode stringNode = new POJONode("abc");
        assertEquals("abc", stringNode.asText("default"));
    }

    // Test cases for asBoolean(boolean defaultValue) method
    @Test
    public void testAsBoolean() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));
        assertTrue(nullNode.asBoolean(true));

        // Test when _value is Boolean
        POJONode falseNode = new POJONode(Boolean.FALSE);
        assertFalse(falseNode.asBoolean(true));
        assertFalse(falseNode.asBoolean(false));

        // Test when _value is not Boolean
        POJONode intNode = new POJONode(5);
        assertTrue(intNode.asBoolean(true));
        assertFalse(intNode.asBoolean(false));
    }

    // Test cases for asInt(int defaultValue) method
    @Test
    public void testAsInt() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals(10, nullNode.asInt(10));

        // Test when _value is Number
        POJONode intNode = new POJONode(5);
        assertEquals(5, intNode.asInt(10));

        // Test when _value is not Number
        POJONode stringNode = new POJONode("abc");
        assertEquals(10, stringNode.asInt(10));
    }

    // Test cases for asLong(long defaultValue) method
    @Test
    public void testAsLong() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals(10L, nullNode.asLong(10L));

        // Test when _value is Number
        POJONode intNode = new POJONode(5);
        assertEquals(5L, intNode.asLong(10L));

        // Test when _value is not Number
        POJONode stringNode = new POJONode("abc");
        assertEquals(10L, stringNode.asLong(10L));
    }

    // Test cases for asDouble(double defaultValue) method
    @Test
    public void testAsDouble() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals(10.0, nullNode.asDouble(10.0), 0.0);

        // Test when _value is Number
        POJONode doubleNode = new POJONode(5.5);
        assertEquals(5.5, doubleNode.asDouble(10.0), 0.0);

        // Test when _value is not Number
        POJONode stringNode = new POJONode("abc");
        assertEquals(10.0, stringNode.asDouble(10.0), 0.0);
    }

    // Test cases for serialize(JsonGenerator gen, SerializerProvider ctxt) method
    @Test
    public void testSerialize() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        try {
            nullNode.serialize(null, null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when _value is a JsonSerializable
        JsonSerializable serializable = new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
                gen.writeString("serialized");
            }
        };
        POJONode serializableNode = new POJONode(serializable);
        try {
            serializableNode.serialize(null, null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when _value is not a JsonSerializable
        POJONode intNode = new POJONode(5);
        try {
            intNode.serialize(null, null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Test cases for getPojo() method
    @Test
    public void testGetPojo() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertNull(nullNode.getPojo());

        // Test when _value is not null
        POJONode intNode = new POJONode(5);
        assertEquals(5, intNode.getPojo());
    }

    // Test cases for equals(Object o) method
    @Test
    public void testEquals() {
        // Test when o is the same object
        POJONode node = new POJONode(5);
        assertTrue(node.equals(node));

        // Test when o is null
        assertFalse(node.equals(null));

        // Test when o is not a POJONode
        assertFalse(node.equals("abc"));

        // Test when o is a different POJONode with same _value
        POJONode node2 = new POJONode(5);
        assertTrue(node.equals(node2));

        // Test when o is a different POJONode with different _value
        POJONode node3 = new POJONode(10);
        assertFalse(node.equals(node3));
    }

    // Test cases for hashCode() method
    @Test
    public void testHashCode() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.hashCode());

        // Test when _value is not null
        POJONode node = new POJONode(5);
        assertEquals(5, node.hashCode());
    }

    // Test cases for toString() method
    @Test
    public void testToString() {
        // Test when _value is null
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        // Test when _value is byte[]
        byte[] data = {1, 2, 3};
        POJONode byteArrayNode = new POJONode(data);
        assertEquals("(binary value of 3 bytes)", byteArrayNode.toString());

        // Test when _value is RawValue
        RawValue rawValue = new RawValue("raw");
        POJONode rawValueNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", rawValueNode.toString());

        // Test when _value is not null, byte[], or RawValue
        POJONode stringNode = new POJONode("abc");
        assertEquals("abc", stringNode.toString());
    }

}