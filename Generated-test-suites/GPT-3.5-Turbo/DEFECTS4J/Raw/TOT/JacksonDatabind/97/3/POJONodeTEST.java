import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        // Test when value is not byte array
        POJONode pojoNode = new POJONode("test");
        Assert.assertThrows(IOException.class, pojoNode::binaryValue);

        // Test when value is byte array
        byte[] byteArray = "test".getBytes();
        pojoNode = new POJONode(byteArray);
        Assert.assertArrayEquals(byteArray, pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.asText());

        pojoNode = new POJONode(null);
        Assert.assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.asText("default"));

        pojoNode = new POJONode(null);
        Assert.assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        Assert.assertTrue(pojoNode.asBoolean(false));

        pojoNode = new POJONode(false);
        Assert.assertFalse(pojoNode.asBoolean(true));

        pojoNode = new POJONode(null);
        Assert.assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(1);
        Assert.assertEquals(1, pojoNode.asInt(0));

        pojoNode = new POJONode(1L);
        Assert.assertEquals(0, pojoNode.asInt(0));

        pojoNode = new POJONode(null);
        Assert.assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(1L);
        Assert.assertEquals(1L, pojoNode.asLong(0L));

        pojoNode = new POJONode(1);
        Assert.assertEquals(0L, pojoNode.asLong(0L));

        pojoNode = new POJONode(null);
        Assert.assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(1.0);
        Assert.assertEquals(1.0, pojoNode.asDouble(0.0), 0.0);

        pojoNode = new POJONode(1);
        Assert.assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);

        pojoNode = new POJONode(null);
        Assert.assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode pojoNode = new POJONode("test");
        String json = objectMapper.writeValueAsString(pojoNode);
        Assert.assertEquals("\"test\"", json);
    }

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        POJONode pojoNode3 = new POJONode("different");

        Assert.assertTrue(pojoNode1.equals(pojoNode2));
        Assert.assertFalse(pojoNode1.equals(pojoNode3));
        Assert.assertFalse(pojoNode1.equals(null));
        Assert.assertFalse(pojoNode1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        POJONode pojoNode3 = new POJONode("different");

        Assert.assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
        Assert.assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode1 = new POJONode("test");
        Assert.assertEquals("test", pojoNode1.toString());

        POJONode pojoNode2 = new POJONode(new byte[]{1, 2, 3});
        Assert.assertEquals("(binary value of 3 bytes)", pojoNode2.toString());

        POJONode pojoNode3 = new POJONode(new RawValue("value"));
        Assert.assertEquals("(raw value 'value')", pojoNode3.toString());
    }
}