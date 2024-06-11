import static org.junit.Assert.*;
import org.junit.Test;

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
        byte[] bytes = {1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(42);
        assertEquals(42, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(42L);
        assertEquals(42L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(42.5);
        assertEquals(42.5, node.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() {
        // Not able to properly test the serialize method without mocks
        // This test will need some changes for complete coverage
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
       POJONode node = new POJONode("test");
       assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }
}