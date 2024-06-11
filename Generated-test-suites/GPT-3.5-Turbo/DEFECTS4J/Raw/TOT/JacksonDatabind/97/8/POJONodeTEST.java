import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] binary = {0x01, 0x02, 0x03};
        POJONode pojoNode = new POJONode(binary);
        try {
            assertArrayEquals(binary, pojoNode.binaryValue());
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(-1));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(100L);
        assertEquals(100L, pojoNode.asLong(-1L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(3.14);
        assertEquals(3.14, pojoNode.asDouble(-1.0), 0.001);
    }

    @Test
    public void testSerialize() {
        // Test serialization is not straightforward as it depends on the behavior of the wrapped POJO
        // In this case, we can only test that the method does not throw any exception.
        POJONode pojoNode = new POJONode("test");
        JsonGenerator generator = null; // create a mock generator
        SerializerProvider context = null; // create a mock context
        try {
            pojoNode.serialize(generator, context);
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        byte[] binary = {0x01, 0x02, 0x03};
        POJONode pojoNode1 = new POJONode(binary);
        assertEquals("(binary value of 3 bytes)", pojoNode1.toString());

        RawValue rawValue = new RawValue("rawTest");
        POJONode pojoNode2 = new POJONode(rawValue);
        assertEquals("(raw value 'rawTest')", pojoNode2.toString());

        POJONode pojoNode3 = new POJONode("test");
        assertEquals("test", pojoNode3.toString());
    }

}