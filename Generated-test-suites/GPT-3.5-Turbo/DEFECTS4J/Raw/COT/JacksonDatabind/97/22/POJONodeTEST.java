import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

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
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());

        byte[] bytes = new byte[]{12, 34, 56};
        POJONode binaryNode = new POJONode(bytes);
        assertArrayEquals(bytes, binaryNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());

        Integer value = 10;
        POJONode intNode = new POJONode(value);
        assertEquals(value.toString(), intNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        String defaultValue = "default";
        assertEquals(defaultValue, node.asText(defaultValue));

        Integer value = 10;
        POJONode intNode = new POJONode(value);
        assertEquals(value.toString(), intNode.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));

        Boolean value = false;
        POJONode booleanNode = new POJONode(value);
        assertEquals(value.booleanValue(), booleanNode.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));

        Integer value = 10;
        POJONode intNode = new POJONode(value);
        assertEquals(value.intValue(), intNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));

        Long value = 10L;
        POJONode longNode = new POJONode(value);
        assertEquals(value.longValue(), longNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0.0);

        Double value = 10.5;
        POJONode doubleNode = new POJONode(value);
        assertEquals(value.doubleValue(), doubleNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode nullNode = new POJONode(null);
        StringWriter nullWriter = new StringWriter();
        JsonGenerator nullGenerator = new JsonFactory().createGenerator(nullWriter);
        SerializerProvider nullProvider = new ObjectMapper().getSerializerProvider();
        nullNode.serialize(nullGenerator, nullProvider);
        nullGenerator.flush();

        assertEquals("null", nullWriter.toString());

        String value = "test";
        POJONode stringNode = new POJONode(value);
        StringWriter stringWriter = new StringWriter();
        JsonGenerator stringGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider stringProvider = new ObjectMapper().getSerializerProvider();
        stringNode.serialize(stringGenerator, stringProvider);
        stringGenerator.flush();

        assertEquals(value, stringWriter.toString());
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertSame(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(null);
        assertTrue(node1.equals(node1));

        POJONode node2 = new POJONode(null);
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));

        Integer value = 10;
        POJONode node3 = new POJONode(value);
        assertFalse(node1.equals(node3));
        assertFalse(node3.equals(node1));

        POJONode node4 = new POJONode(value);
        assertTrue(node3.equals(node4));
        assertTrue(node4.equals(node3));

        POJONode node5 = new POJONode(new Object());
        assertFalse(node4.equals(node5));
        assertFalse(node5.equals(node4));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(null);
        assertEquals(0, node1.hashCode());

        Integer value = 10;
        POJONode node2 = new POJONode(value);
        assertEquals(value.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.toString());

        byte[] bytes = new byte[]{12, 34, 56};
        POJONode binaryNode = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", binaryNode.toString());

        String value = "test";
        POJONode stringNode = new POJONode(value);
        assertEquals(value, stringNode.toString());
    }

}