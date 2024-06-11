
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Assert;

public class POJONodeTest {
    
    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }
    
    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }
    
    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] {1, 2, 3});
        Assert.assertArrayEquals(new byte[] {1, 2, 3}, node.binaryValue());
    }
    
    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("default", node.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        Assert.assertTrue(node.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        Assert.assertEquals(10, node.asInt(0));
    }
    
    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        Assert.assertEquals(100L, node.asLong(0L));
    }
    
    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.5);
        Assert.assertEquals(10.5, node.asDouble(0.0), 0.001);
    }
    
    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode("test");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        node.serialize(generator, provider);
        generator.close();
        Assert.assertEquals("\"test\"", writer.toString());
    }
    
    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        Assert.assertEquals(pojo, node.getPojo());
    }
    
    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        Assert.assertTrue(node1.equals(node2));
    }
    
    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test".hashCode(), node.hashCode());
    }
    
    @Test
    public void testToString() {
        POJONode node = new POJONode(new byte[] {1, 2, 3});
        Assert.assertEquals("(binary value of 3 bytes)", node.toString());
    }
}
