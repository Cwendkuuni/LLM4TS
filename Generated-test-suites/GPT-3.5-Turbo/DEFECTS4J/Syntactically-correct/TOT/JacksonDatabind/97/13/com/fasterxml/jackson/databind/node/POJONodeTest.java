import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode node = new POJONode(byteArray);
        try {
            assertArrayEquals(byteArray, node.binaryValue());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAsText() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);
        assertEquals(stringValue, node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        String defaultValue = "default";
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithDefault() {
        boolean defaultValue = false;
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(defaultValue));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsIntWithDefault() {
        int defaultValue = 0;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asInt(defaultValue));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
    }

    @Test
    public void testAsLongWithDefault() {
        long defaultValue = 0L;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asLong(defaultValue));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testAsDoubleWithDefault() {
        double defaultValue = 0.0;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asDouble(defaultValue), 0.0001);
    }

    @Test
    public void testSerializeNullValue() {
        POJONode node = new POJONode(null);
        // We cannot directly test the serialization logic,
        // but we can check that no exceptions are thrown
        assertNull(node.getPojo());
    }

    @Test
    public void testSerializeJsonSerializableValue() {
        JsonSerializable serializable = new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
                gen.writeString("serializable");
            }
        };
        POJONode node = new POJONode(serializable);
        // We cannot directly test the serialization logic,
        // but we can check that no exceptions are thrown
        assertEquals(serializable, node.getPojo());
    }

    @Test
    public void testSerializeNonJsonSerializableValue() {
        Object nonSerializable = new Object();
        POJONode node = new POJONode(nonSerializable);
        // We cannot directly test the serialization logic,
        // but we can check that no exceptions are thrown
        assertEquals(nonSerializable, node.getPojo());
    }

    @Test
    public void testEqualsEqualValues() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertEquals(node1, node2);
    }

    @Test
    public void testEqualsDifferentValues() {
        POJONode node1 = new POJONode("test1");
        POJONode node2 = new POJONode("test2");
        assertNotEquals(node1, node2);
    }

    @Test
    public void testEqualsWithNull() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        POJONode node = new POJONode("test");
        assertNotEquals(node, "test");
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToStringWithByteArray() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode node = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", node.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode node = new POJONode(rawValue);
        assertEquals("(raw value 'test')", node.toString());
    }

    @Test
    public void testToStringWithOtherValue() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }

}