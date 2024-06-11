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
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("abc");
        assertEquals("abc", node.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(123);
        assertEquals(123, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(123L);
        assertEquals(123L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(123.45);
        assertEquals(123.45, node.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() {
        // Implement test for serialize method
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("abc");
        POJONode node2 = new POJONode("abc");
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }
}