import static org.junit.Assert.*;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode nullNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, nullNode.asToken());

        POJONode valueNode = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, valueNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode nullNode = new POJONode(null);
        assertNull(nullNode.binaryValue());

        byte[] byteArray = {1, 2, 3};
        POJONode binaryNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, binaryNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());

        POJONode stringValueNode = new POJONode("test");
        assertEquals("test", stringValueNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode nullNode = new POJONode(null);
        assertEquals("default", nullNode.asText("default"));

        POJONode stringValueNode = new POJONode("test");
        assertEquals("test", stringValueNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));

        POJONode booleanValueNode = new POJONode(true);
        assertTrue(booleanValueNode.asBoolean(false));

        POJONode stringValueNode = new POJONode("true");
        assertFalse(stringValueNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10, nullNode.asInt(10));

        POJONode intValueNode = new POJONode(20);
        assertEquals(20, intValueNode.asInt(10));

        POJONode stringValueNode = new POJONode("30");
        assertEquals(10, stringValueNode.asInt(10));
    }

    @Test
    public void testAsLong() {
        POJONode nullNode = new POJONode(null);
        assertEquals(500L, nullNode.asLong(500L));

        POJONode longValueNode = new POJONode(1000L);
        assertEquals(1000L, longValueNode.asLong(500L));

        POJONode stringValueNode = new POJONode("2000");
        assertEquals(500L, stringValueNode.asLong(500L));
    }

    @Test
    public void testAsDouble() {
        POJONode nullNode = new POJONode(null);
        assertEquals(10.5, nullNode.asDouble(10.5), 0.001);

        POJONode doubleValueNode = new POJONode(20.5);
        assertEquals(20.5, doubleValueNode.asDouble(10.5), 0.001);

        POJONode stringValueNode = new POJONode("30.5");
        assertEquals(10.5, stringValueNode.asDouble(10.5), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        POJONode nullNode = new POJONode(null);
        nullNode.serialize(generator, provider);
        generator.flush();
        assertEquals("null", writer.toString());

        POJONode intValueNode = new POJONode(10);
        intValueNode.serialize(generator, provider);
        generator.flush();
        assertEquals("10", writer.toString());
    }

    @Test
    public void testGetPojo() {
        POJONode nullNode = new POJONode(null);
        assertNull(nullNode.getPojo());

        POJONode stringValueNode = new POJONode("test value");
        assertEquals("test value", stringValueNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);

        POJONode node3 = new POJONode("test");
        assertNotEquals(node1, node3);
    }

    @Test
    public void testHashCode() {
        POJONode nullNode = new POJONode(null);
        assertEquals(0, nullNode.hashCode());

        POJONode stringValueNode = new POJONode("test");
        assertEquals("test".hashCode(), stringValueNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        POJONode byteArrayNode = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", byteArrayNode.toString());

        POJONode rawValueNode = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", rawValueNode.toString());

        POJONode stringValueNode = new POJONode("testValue");
        assertEquals("testValue", stringValueNode.toString());
    }

}