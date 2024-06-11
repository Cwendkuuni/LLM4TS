import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setUp() {
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
    public void testBinaryValue_WhenValueIsByteArray_ExpectByteArray() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);

        try {
            byte[] result = pojoNode.binaryValue();
            assertArrayEquals(byteArray, result);
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testBinaryValue_WhenValueIsNotByteArray_ExpectSuperBinaryValue() {
        String stringValue = "test";
        POJONode pojoNode = new POJONode(stringValue);

        try {
            byte[] result = pojoNode.binaryValue();
            fail("IOException not thrown when expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAsText_WhenValueIsNull_ExpectNullString() {
        String result = pojoNode.asText();
        assertEquals("null", result);
    }

    @Test
    public void testAsText_WhenValueIsNotNull_ExpectValueString() {
        String value = "test";
        POJONode pojoNode = new POJONode(value);

        String result = pojoNode.asText();
        assertEquals(value, result);
    }

    @Test
    public void testAsTextWithDefaultValue_WhenValueIsNull_ExpectDefaultValue() {
        String defaultValue = "default";
        String result = pojoNode.asText(defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testAsTextWithDefaultValue_WhenValueIsNotNull_ExpectValueString() {
        String value = "test";
        String defaultValue = "default";
        POJONode pojoNode = new POJONode(value);

        String result = pojoNode.asText(defaultValue);
        assertEquals(value, result);
    }

    @Test
    public void testAsBoolean_WhenValueIsNull_ExpectDefaultValue() {
        boolean defaultValue = true;
        boolean result = pojoNode.asBoolean(defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testAsBoolean_WhenValueIsBoolean_ExpectBooleanValue() {
        boolean value = true;
        POJONode pojoNode = new POJONode(value);

        boolean result = pojoNode.asBoolean(false);
        assertEquals(value, result);
    }

    @Test
    public void testAsInt_WhenValueIsNotNumber_ExpectDefaultValue() {
        int defaultValue = 123;
        int result = pojoNode.asInt(defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testAsInt_WhenValueIsInt_ExpectIntValue() {
        int value = 123;
        POJONode pojoNode = new POJONode(value);

        int result = pojoNode.asInt(0);
        assertEquals(value, result);
    }

    @Test
    public void testAsLong_WhenValueIsNotNumber_ExpectDefaultValue() {
        long defaultValue = 123L;
        long result = pojoNode.asLong(defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testAsLong_WhenValueIsLong_ExpectLongValue() {
        long value = 123L;
        POJONode pojoNode = new POJONode(value);

        long result = pojoNode.asLong(0L);
        assertEquals(value, result);
    }

    @Test
    public void testAsDouble_WhenValueIsNotNumber_ExpectDefaultValue() {
        double defaultValue = 123.0;
        double result = pojoNode.asDouble(defaultValue);
        assertEquals(defaultValue, result, 0.0);
    }

    @Test
    public void testAsDouble_WhenValueIsDouble_ExpectDoubleValue() {
        double value = 123.0;
        POJONode pojoNode = new POJONode(value);

        double result = pojoNode.asDouble(0.0);
        assertEquals(value, result, 0.0);
    }

    @Test
    public void testSerialize_WhenValueIsNull_ExpectDefaultSerializeNullCalled() {
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider ctxt = new MockSerializerProvider();

        pojoNode.serialize(gen, ctxt);

        assertTrue(gen.defaultSerializeNullCalled);
    }

    @Test
    public void testSerialize_WhenValueIsJsonSerializable_ExpectSerializeCalled() {
        JsonSerializable value = new MockJsonSerializable();
        POJONode pojoNode = new POJONode(value);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider ctxt = new MockSerializerProvider();

        pojoNode.serialize(gen, ctxt);

        assertTrue(value.serializeCalled);
    }

    @Test
    public void testSerialize_WhenValueIsNotJsonSerializable_ExpectWriteObjectCalled() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider ctxt = new MockSerializerProvider();

        pojoNode.serialize(gen, ctxt);

        assertTrue(gen.writeObjectCalled);
        assertEquals(value, gen.writeObjectValue);
    }

    @Test
    public void testGetPojo_WhenValueIsNotNull_ExpectValueObject() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);

        Object result = pojoNode.getPojo();

        assertEquals(value, result);
    }

    @Test
    public void testEquals_WhenSameObject_ExpectTrue() {
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testEquals_WhenDifferentClass_ExpectFalse() {
        assertFalse(pojoNode.equals(new Object()));
    }

    @Test
    public void testEquals_WhenOtherObjectIsNull_ExpectFalse() {
        assertFalse(pojoNode.equals(null));
    }

    @Test
    public void testEquals_WhenValueIsSame_ExpectTrue() {
        POJONode pojoNode2 = new POJONode(null);

        assertTrue(pojoNode.equals(pojoNode2));
    }

    @Test
    public void testEquals_WhenValueIsNull_ExpectFalse() {
        POJONode pojoNode2 = new POJONode("test");

        assertFalse(pojoNode.equals(pojoNode2));
    }

    @Test
    public void testEquals_WhenValueIsNull_ExpectTrue() {
        POJONode pojoNode = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);

        assertTrue(pojoNode.equals(pojoNode2));
    }

    @Test
    public void testEquals_WhenValueIsEqual_ExpectTrue() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        POJONode pojoNode2 = new POJONode(value);

        assertTrue(pojoNode.equals(pojoNode2));
    }

    @Test
    public void testEquals_WhenValueIsNotEqual_ExpectFalse() {
        POJONode pojoNode = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test2");

        assertFalse(pojoNode.equals(pojoNode2));
    }

    @Test
    public void testHashCode_WhenValueIsNull_ExpectHashCodeOfNull() {
        POJONode pojoNode = new POJONode(null);

        int hashCode = pojoNode.hashCode();

        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCode_WhenValueIsNotNull_ExpectHashCodeOfValue() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);

        int hashCode = pojoNode.hashCode();

        assertEquals(value.hashCode(), hashCode);
    }

    @Test
    public void testToString_WhenValueIsByteArray_ExpectBinaryValueString() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);

        String result = pojoNode.toString();

        assertEquals("(binary value of 3 bytes)", result);
    }

    @Test
    public void testToString_WhenValueIsNotByteArray_ExpectStringValue() {
        String value = "test";
        POJONode pojoNode = new POJONode(value);

        String result = pojoNode.toString();

        assertEquals(value, result);
    }

    private static class MockJsonGenerator extends JsonGenerator {
        boolean defaultSerializeNullCalled = false;
        boolean writeObjectCalled = false;
        Object writeObjectValue = null;

        @Override
        public void defaultSerializeNull() throws IOException {
            defaultSerializeNullCalled = true;
        }

        @Override
        public void writeString(String text) throws IOException {
            // Not used in this test
        }

        @Override
        public void writeObject(Object value) throws IOException {
            writeObjectCalled = true;
            writeObjectValue = value;
        }

        // Other abstract methods of JsonGenerator
    }

    private static class MockSerializerProvider extends SerializerProvider {
        // Not used in this test
        // Other abstract methods of SerializerProvider
    }

    private static class MockJsonSerializable implements JsonSerializable {
        boolean serializeCalled = false;

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            serializeCalled = true;
        }
    }
}