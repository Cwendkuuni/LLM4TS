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
    public void testBinaryValue() throws IOException {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefault() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefault() {
        POJONode pojoNode = new POJONode(10);
        assertEquals(10, pojoNode.asInt(0));
    }

    @Test
    public void testAsLongWithDefault() {
        POJONode pojoNode = new POJONode(100L);
        assertEquals(100L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDoubleWithDefault() {
        POJONode pojoNode = new POJONode(3.14);
        assertEquals(3.14, pojoNode.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode pojoNode = new POJONode("test");
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("\"test\"", json);
    }

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.getPojo());
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