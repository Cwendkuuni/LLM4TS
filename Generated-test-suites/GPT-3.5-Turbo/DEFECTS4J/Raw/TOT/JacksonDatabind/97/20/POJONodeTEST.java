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
    public void testBinaryValue_nullValue() throws IOException {
        POJONode pojoNode = new POJONode(null);
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testBinaryValue_byteArrayValue() throws IOException {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }

    @Test
    public void testAsText_nullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsText_stringValue() {
        String stringValue = "test";
        POJONode pojoNode = new POJONode(stringValue);
        assertEquals(stringValue, pojoNode.asText());
    }

    @Test
    public void testAsText_defaultValue() {
        String defaultValue = "default";
        POJONode pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asText(defaultValue));
    }

    @Test
    public void testAsBoolean_booleanValue() {
        boolean booleanValue = true;
        POJONode pojoNode = new POJONode(booleanValue);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsBoolean_nonBooleanValue() {
        POJONode pojoNode = new POJONode("test");
        assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt_integerValue() {
        int integerValue = 10;
        POJONode pojoNode = new POJONode(integerValue);
        assertEquals(integerValue, pojoNode.asInt(0));
    }

    @Test
    public void testAsInt_nonIntegerValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong_longValue() {
        long longValue = 100L;
        POJONode pojoNode = new POJONode(longValue);
        assertEquals(longValue, pojoNode.asLong(0L));
    }

    @Test
    public void testAsLong_nonLongValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble_doubleValue() {
        double doubleValue = 10.5;
        POJONode pojoNode = new POJONode(doubleValue);
        assertEquals(doubleValue, pojoNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testAsDouble_nonDoubleValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);
    }

    @Test
    public void testSerialize_nullValue() {
        POJONode pojoNode = new POJONode(null);
        // TODO: Test serialization
    }

    @Test
    public void testSerialize_JsonSerializableValue() {
        // TODO: Test serialization
    }

    @Test
    public void testSerialize_otherValue() {
        // TODO: Test serialization
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode pojoNode = new POJONode(pojo);
        assertEquals(pojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals_sameNode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(pojoNode, pojoNode);
    }

    @Test
    public void testEquals_nullNode() {
        POJONode pojoNode = new POJONode(null);
        assertNotEquals(pojoNode, null);
    }

    @Test
    public void testEquals_differentClass() {
        POJONode pojoNode = new POJONode("test");
        assertNotEquals(pojoNode, "test");
    }

    @Test
    public void testEquals_sameValue() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(pojoNode1, pojoNode2);
    }

    @Test
    public void testEquals_differentValue() {
        POJONode pojoNode1 = new POJONode("test1");
        POJONode pojoNode2 = new POJONode("test2");
        assertNotEquals(pojoNode1, pojoNode2);
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString_binaryValue() {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    @Test
    public void testToString_rawValue() {
        RawValue rawValue = new RawValue("raw");
        POJONode pojoNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", pojoNode.toString());
    }

    @Test
    public void testToString_otherValue() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test", pojoNode.toString());
    }

}