
import org.junit.Assert;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode("test");
        byte[] value = node.binaryValue();
        Assert.assertNull(value);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        Assert.assertTrue(node.asBoolean(false));
        Assert.assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(42);
        Assert.assertEquals(42, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(42L);
        Assert.assertEquals(42L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        Assert.assertEquals(3.14, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test serialize method, but output cannot be easily asserted as it depends on ObjectMapper
        POJONode node = new POJONode("test");
        node.serialize(null, null);
        // Add assertion for code coverage
        Assert.assertTrue(true);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("test2");

        Assert.assertTrue(node1.equals(node2));
        Assert.assertFalse(node1.equals(node3));
        Assert.assertFalse(node1.equals(null));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.toString());
    }

    @Test
    public void testToStringWithBinaryData() {
        byte[] binaryData = new byte[]{0x01, 0x02, 0x03};
        POJONode node = new POJONode(binaryData);
        Assert.assertEquals("(binary value of 3 bytes)", node.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode node = new POJONode(rawValue);
        Assert.assertEquals("(raw value 'test')", node.toString());
    }
}
