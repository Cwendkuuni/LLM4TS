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
    public void testBinaryValue() throws IOException {
        byte[] value = new byte[] {1, 2, 3};
        POJONode node = new POJONode(value);
        assertArrayEquals(value, node.binaryValue());
        
        POJONode node2 = new POJONode("string");
        try {
            node2.binaryValue();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        POJONode node2 = new POJONode("string");
        assertEquals("string", node2.asText());
    }
    
    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
        
        POJONode node2 = new POJONode("string");
        assertEquals("string", node2.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(false));
        
        POJONode node2 = new POJONode(true);
        assertTrue(node2.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(1, node.asInt(1));
        
        POJONode node2 = new POJONode(10);
        assertEquals(10, node2.asInt(0));
    }
    
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(1L, node.asLong(1L));
        
        POJONode node2 = new POJONode(10L);
        assertEquals(10L, node2.asLong(0L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(1.0, node.asDouble(1.0), 0);
        
        POJONode node2 = new POJONode(10.0);
        assertEquals(10.0, node2.asDouble(0.0), 0);
    }
    
    @Test
    public void testSerializeNull() throws IOException {
        POJONode node = new POJONode(null);
        // TODO: Verify the correct behavior of serialize method
    }
    
    @Test
    public void testSerializeJsonSerializable() throws IOException {
        // TODO: Create a test case for serializing a JsonSerializable object
    }
    
    @Test
    public void testSerializePojo() throws IOException {
        // TODO: Create a test case for serializing a regular POJO
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
        
        POJONode node3 = new POJONode("string");
        assertFalse(node1.equals(node3));
    }
    
    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.hashCode());
        
        POJONode node2 = new POJONode("string");
        assertEquals("string".hashCode(), node2.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());
        
        POJONode node2 = new POJONode("string");
        assertEquals("string", node2.toString());
        
        POJONode node3 = new POJONode(new byte[] {1, 2, 3});
        assertEquals("(binary value of 3 bytes)", node3.toString());
        
        RawValue rawValue = new RawValue("raw value");
        POJONode node4 = new POJONode(rawValue);
        assertEquals("(raw value 'raw value')", node4.toString());
    }
}