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
    public void testBinaryValue() throws IOException {
        byte[] byteArray = {10, 20, 30};
        POJONode node = new POJONode(byteArray);
        assertArrayEquals(byteArray, node.binaryValue());
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
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.5);
        assertEquals(10.5, node.asDouble(0.0), 0.001);
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
        byte[] byteArray = {10, 20, 30};
        POJONode node = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", node.toString());
    }

    // Add more tests for edge cases and error scenarios as needed

}