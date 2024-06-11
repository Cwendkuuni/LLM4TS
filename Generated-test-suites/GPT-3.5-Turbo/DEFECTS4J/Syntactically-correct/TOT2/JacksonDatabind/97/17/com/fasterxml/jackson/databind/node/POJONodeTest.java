import static org.junit.Assert.*;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        byte[] byteArray = new byte[]{1, 2, 3};
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
        POJONode node = new POJONode(5);
        assertEquals(5, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(10L);
        assertEquals(10L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(5.5);
        assertEquals(5.5, node.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() {
        // Testing serialization is more complex, may require mocking
        // Serializing a POJONode object with a custom serializer
        // and comparing the output with the expected JSON
    }

    @Test
    public void testGetPojo() {
        Object obj = "test";
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