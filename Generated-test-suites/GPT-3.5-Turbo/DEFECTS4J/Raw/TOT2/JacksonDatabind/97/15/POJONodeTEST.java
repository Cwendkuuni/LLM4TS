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
        byte[] byteArray = new byte[]{0, 1, 2};
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }
    
    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText());
    }
    
    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(5));
    }
    
    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(100L);
        assertEquals(100L, pojoNode.asLong(50L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(10.5);
        assertEquals(10.5, pojoNode.asDouble(5.5), 0.001);
    }
    
    @Test
    public void testSerialize() {
        // Test case for serialization can be added based on specific use cases
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
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.toString());
    }
}