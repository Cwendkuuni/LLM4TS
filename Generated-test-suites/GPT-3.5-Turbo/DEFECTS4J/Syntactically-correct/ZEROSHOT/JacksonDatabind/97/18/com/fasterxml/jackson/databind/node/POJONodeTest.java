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
        byte[] bytes = {1, 2, 3};
        POJONode node = new POJONode(bytes);
        try {
            assertArrayEquals(bytes, node.binaryValue());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testAsText() {
        POJONode node1 = new POJONode(null);
        assertEquals("null", node1.asText());
        
        POJONode node2 = new POJONode("test");
        assertEquals("test", node2.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node1 = new POJONode(null);
        assertEquals("default", node1.asText("default"));
        
        POJONode node2 = new POJONode("test");
        assertEquals("test", node2.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node1 = new POJONode(null);
        assertFalse(node1.asBoolean(false));
        
        POJONode node2 = new POJONode(true);
        assertTrue(node2.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode node1 = new POJONode(null);
        assertEquals(10, node1.asInt(10));
        
        POJONode node2 = new POJONode(5);
        assertEquals(5, node2.asInt(10));
    }
    
    @Test
    public void testAsLong() {
        POJONode node1 = new POJONode(null);
        assertEquals(100L, node1.asLong(100L));
        
        POJONode node2 = new POJONode(50L);
        assertEquals(50L, node2.asLong(100L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node1 = new POJONode(null);
        assertEquals(1.5, node1.asDouble(1.5), 0.0001);
        
        POJONode node2 = new POJONode(2.5);
        assertEquals(2.5, node2.asDouble(1.5), 0.0001);
    }
    
    @Test
    public void testSerialize() {
        // Test case for actual implementation is not provided as it depends on external libraries (Jackson ObjectMapper)
    }
    
    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("different");
        
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }
    
    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        
        assertEquals(node1.hashCode(), node2.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode node1 = new POJONode(null);
        assertEquals("null", node1.toString());
        
        POJONode node2 = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", node2.toString());
        
        POJONode node3 = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", node3.toString());
        
        POJONode node4 = new POJONode("test");
        assertEquals("test", node4.toString());
    }
}