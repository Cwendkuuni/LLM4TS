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
        byte[] testData = {1, 2, 3};
        POJONode node = new POJONode(testData);
        assertArrayEquals(testData, node.binaryValue());
        
        // Test when _value is not an array of bytes
        POJONode node2 = new POJONode("string");
        assertThrows(IOException.class, node2::binaryValue);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("text");
        assertEquals("text", node.asText());

        POJONode node2 = new POJONode(null);
        assertEquals("null", node2.asText());
    }

    // Other test methods for remaining public methods in the POJONode class

}