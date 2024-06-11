import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] value = new byte[]{1, 2, 3};
        POJONode node = new POJONode(value);
        assertArrayEquals(value, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeTrue = new POJONode(true);
        assertTrue(nodeTrue.asBoolean(false));

        POJONode nodeFalse = new POJONode(false);
        assertFalse(nodeFalse.asBoolean(true));

        POJONode nodeNull = new POJONode(null);
        assertFalse(nodeNull.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.5);
        assertEquals(10.5, node.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode("test");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl();
        try {
            node.serialize(generator, serializerProvider);
            generator.flush();          
        } catch (IOException e) {
            fail("IOException thrown");
        }
        assertEquals("\"test\"", writer.toString());
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
        assertEquals(node1, node2);
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }
}