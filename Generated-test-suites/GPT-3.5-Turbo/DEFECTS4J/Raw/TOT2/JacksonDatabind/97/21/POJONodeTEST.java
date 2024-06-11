import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);
        assertEquals(obj, pojoNode.getPojo());
    }

    @Test
    public void testJsonNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] binaryData = {'a', 'b', 'c'};
        POJONode pojoNode = new POJONode(binaryData);
        byte[] returnedData = pojoNode.binaryValue();
        assertNotNull(returnedData);
        assertEquals(binaryData.length, returnedData.length);
        for (int i = 0; i < binaryData.length; i++) {
            assertEquals(binaryData[i], returnedData[i]);
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("text");
        assertEquals("text", pojoNode.asText());
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(42);
        assertEquals(42, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(9999999999L);
        assertEquals(9999999999L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(3.14);
        assertEquals(3.14, pojoNode.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = new POJONode("test");
        String jsonString = mapper.writeValueAsString(pojoNode);
        assertEquals("\"test\"", jsonString);
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test1");
        POJONode pojoNode2 = new POJONode("test1");
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testPojoEquals() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        assertTrue(pojoNode1._pojoEquals(pojoNode2));
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