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
        POJONode node = new POJONode(null);
        try {
            byte[] binary = node.binaryValue();
            assertNull(binary);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(10, node.asInt(10));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(100L, node.asLong(100L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(3.14, node.asDouble(3.14), 0.001);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(null);
        // TODO: Add test for serialization
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
    }

}