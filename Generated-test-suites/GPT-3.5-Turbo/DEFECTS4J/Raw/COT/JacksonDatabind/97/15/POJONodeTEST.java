
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private POJONode pojoNode;

    @Test
    public void testGetNodeType() {
        pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        pojoNode = new POJONode(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        pojoNode = new POJONode(null);
        assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        pojoNode = new POJONode(1000L);
        assertEquals(1000L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        pojoNode = new POJONode(3.14);
        assertEquals(3.14, pojoNode.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() {
        pojoNode = new POJONode(null);
        // TODO: test serialization
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        pojoNode = new POJONode(pojo);
        assertEquals(pojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(new Object());
        assertNotNull(node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", node.toString());
    }
}
