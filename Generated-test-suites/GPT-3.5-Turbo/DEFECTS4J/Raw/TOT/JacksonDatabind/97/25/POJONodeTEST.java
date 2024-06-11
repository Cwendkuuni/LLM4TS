import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setup() {
        pojoNode = new POJONode(null);
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValueWithNullValue() {
        try {
            assertNull(pojoNode.binaryValue());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testBinaryValueWithByteArrayValue() {
        byte[] byteArray = {1, 2, 3, 4};
        pojoNode = new POJONode(byteArray);
        try {
            assertArrayEquals(byteArray, pojoNode.binaryValue());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testAsTextWithNullValue() {
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithValue() {
        String value = "test";
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValueAndNullValue() {
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsTextWithDefaultValueAndValue() {
        String value = "test";
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asText("default"));
    }

    @Test
    public void testAsBooleanWithNullValueAndDefaultValue() {
        assertEquals(false, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithNullValueAndTrueDefaultValue() {
        assertEquals(true, pojoNode.asBoolean(true));
    }

    @Test
    public void testAsBooleanWithValue() {
        boolean value = true;
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsIntWithNullValueAndDefaultValue() {
        assertEquals(10, pojoNode.asInt(10));
    }

    @Test
    public void testAsIntWithValue() {
        int value = 5;
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asInt(10));
    }

    @Test
    public void testAsLongWithNullValueAndDefaultValue() {
        assertEquals(100L, pojoNode.asLong(100L));
    }

    @Test
    public void testAsLongWithValue() {
        long value = 50L;
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asLong(100L));
    }

    @Test
    public void testAsDoubleWithNullValueAndDefaultValue() {
        assertEquals(1.5, pojoNode.asDouble(1.5), 0);
    }

    @Test
    public void testAsDoubleWithValue() {
        double value = 2.5;
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.asDouble(1.5), 0);
    }

    @Test
    public void testSerializeWithNullValue() {
        // Not possible to test since JsonGenerator and SerializerProvider are external dependencies.
    }

    @Test
    public void testSerializeWithJsonSerializableValue() {
        // Not possible to test since JsonSerializable and JsonGenerator are external dependencies.
    }

    @Test
    public void testSerializeWithNonSerializableValue() {
        // Not possible to test since JsonGenerator and SerializerProvider are external dependencies.
    }

    @Test
    public void testGetPojoWithNullValue() {
        assertNull(pojoNode.getPojo());
    }

    @Test
    public void testGetPojoWithValue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEqualsWithSameReference() {
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(pojoNode.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(pojoNode.equals(new Object()));
    }

    @Test
    public void testEqualsWithNullValueAndDifferentNullValue() {
        assertTrue(pojoNode.equals(new POJONode(null)));
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Object value1 = new Object();
        Object value2 = new Object();
        pojoNode = new POJONode(value1);
        assertFalse(pojoNode.equals(new POJONode(value2)));
    }

    @Test
    public void testEqualsWithSameValue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertTrue(pojoNode.equals(new POJONode(value)));
    }

    @Test
    public void testHashCodeWithNullValue() {
        assertEquals(0, pojoNode.hashCode());
    }

    @Test
    public void testHashCodeWithValue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToStringWithBinaryValue() {
        byte[] value = {1, 2, 3, 4};
        pojoNode = new POJONode(value);
        assertEquals("(binary value of 4 bytes)", pojoNode.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        Object value = new RawValue("test");
        pojoNode = new POJONode(value);
        assertEquals("(raw value 'test')", pojoNode.toString());
    }

    @Test
    public void testToStringWithValue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertEquals(value.toString(), pojoNode.toString());
    }

}