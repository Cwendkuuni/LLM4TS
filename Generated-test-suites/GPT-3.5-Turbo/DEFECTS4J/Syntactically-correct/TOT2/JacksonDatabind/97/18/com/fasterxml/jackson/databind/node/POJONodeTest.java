import static org.junit.Assert.*;

import org.junit.Test;

public class POJONodeTest {

    // Test getNodeType method
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    // Test asToken
    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    // Test binaryValue method
    @Test
    public void testBinaryValue() {
        byte[] binaryData = {1, 0, 1};
        POJONode node = new POJONode(binaryData);
        assertArrayEquals(binaryData, node.binaryValue());
    }

    // Test asText method
    @Test
    public void testAsText() {
        POJONode node = new POJONode(123);
        assertEquals("123", node.asText());
    }

    // Test asBoolean method
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    // Test asInt method
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(456);
        assertEquals(456, node.asInt(0));
    }

    // Test asLong method
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(789L);
        assertEquals(789L, node.asLong(0L));
    }

    // Test asDouble method
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.0001);
    }

    // Test serialize method
    @Test
    public void testSerialize() {
        POJONode node = new POJONode("test");
        // Add test for serialization
    }

    // Test getPojo method
    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }

    // Test equals method
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertTrue(node1.equals(node2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    // Test toString method
    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }

}