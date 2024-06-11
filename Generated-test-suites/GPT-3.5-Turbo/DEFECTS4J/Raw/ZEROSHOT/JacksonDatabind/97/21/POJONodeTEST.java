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
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());

        byte[] bytes = new byte[]{1, 2, 3};
        node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
        
        node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        String defaultValue = "default";
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asText(defaultValue));
        
        node = new POJONode("test");
        assertEquals("test", node.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        boolean defaultValue = false;
        POJONode node = new POJONode(null);
        assertFalse(node.asBoolean(defaultValue));

        node = new POJONode(true);
        assertTrue(node.asBoolean(defaultValue));

        node = new POJONode(false);
        assertFalse(node.asBoolean(defaultValue));
    }

    @Test
    public void testAsInt() {
        int defaultValue = 0;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asInt(defaultValue));

        node = new POJONode(123);
        assertEquals(123, node.asInt(defaultValue));

        node = new POJONode(3.14);
        assertEquals(defaultValue, node.asInt(defaultValue));
    }

    @Test
    public void testAsLong() {
        long defaultValue = 0L;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asLong(defaultValue));

        node = new POJONode(123L);
        assertEquals(123L, node.asLong(defaultValue));

        node = new POJONode(3.14);
        assertEquals(defaultValue, node.asLong(defaultValue));
    }

    @Test
    public void testAsDouble() {
        double defaultValue = 0.0;
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asDouble(defaultValue), 0.0001);

        node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(defaultValue), 0.0001);

        node = new POJONode("test");
        assertEquals(defaultValue, node.asDouble(defaultValue), 0.0001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);

        node.serialize(gen, ctxt);
        verify(ctxt).defaultSerializeNull(gen);

        Object pojo = new Object();
        node = new POJONode(pojo);

        node.serialize(gen, ctxt);
        verify(gen).writeObject(pojo);
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
        assertEquals(node1, node1);

        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);

        POJONode node3 = new POJONode("test");
        assertNotEquals(node1, node3);
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1.hashCode(), node2.hashCode());

        POJONode node3 = new POJONode("test");
        assertNotEquals(node1.hashCode(), node3.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.toString());

        byte[] bytes = new byte[]{1, 2, 3};
        node = new POJONode(bytes);
        assertEquals("(binary value of 3 bytes)", node.toString());

        node = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", node.toString());

        Object pojo = new Object();
        node = new POJONode(pojo);
        assertEquals(pojo.toString(), node.toString());
    }

}