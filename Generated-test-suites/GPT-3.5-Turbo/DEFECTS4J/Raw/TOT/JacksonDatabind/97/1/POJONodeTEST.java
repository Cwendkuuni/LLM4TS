import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class POJONodeTest {

    private final Object pojo = new Object();
    private final POJONode node = new POJONode(pojo);

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue_WhenValueIsByteArray_ReturnsByteArray() throws IOException {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode node = new POJONode(byteArray);

        byte[] result = node.binaryValue();

        assertEquals(byteArray, result);
    }

    @Test(expected = IOException.class)
    public void testBinaryValue_WhenValueIsNotByteArray_ThrowsIOException() throws IOException {
        String string = "test";
        POJONode node = new POJONode(string);

        node.binaryValue();
    }

    @Test
    public void testAsText_WhenValueIsNull_ReturnsNullString() {
        POJONode node = new POJONode(null);

        String result = node.asText();

        assertEquals("null", result);
    }

    @Test
    public void testAsText_WhenValueIsNotNull_ReturnsValueToString() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        String result = node.asText();

        assertEquals(stringValue, result);
    }

    @Test
    public void testAsText_WithDefaultValue_WhenValueIsNull_ReturnsDefaultValue() {
        POJONode node = new POJONode(null);

        String result = node.asText("default");

        assertEquals("default", result);
    }

    @Test
    public void testAsText_WithDefaultValue_WhenValueIsNotNull_ReturnsValueToString() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        String result = node.asText("default");

        assertEquals(stringValue, result);
    }

    @Test
    public void testAsBoolean_WhenValueIsBoolean_ReturnsBooleanValue() {
        boolean booleanValue = true;
        POJONode node = new POJONode(booleanValue);

        boolean result = node.asBoolean(false);

        assertTrue(result);
    }

    @Test
    public void testAsBoolean_WhenValueIsNotBoolean_ReturnsDefaultValue() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        boolean result = node.asBoolean(false);

        assertFalse(result);
    }

    @Test
    public void testAsInt_WhenValueIsNumber_ReturnsIntValue() {
        int intValue = 10;
        POJONode node = new POJONode(intValue);

        int result = node.asInt(0);

        assertEquals(intValue, result);
    }

    @Test
    public void testAsInt_WhenValueIsNotNumber_ReturnsDefaultValue() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        int result = node.asInt(0);

        assertEquals(0, result);
    }

    @Test
    public void testAsLong_WhenValueIsNumber_ReturnsLongValue() {
        long longValue = 1234567890L;
        POJONode node = new POJONode(longValue);

        long result = node.asLong(0L);

        assertEquals(longValue, result);
    }

    @Test
    public void testAsLong_WhenValueIsNotNumber_ReturnsDefaultValue() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        long result = node.asLong(0L);

        assertEquals(0L, result);
    }

    @Test
    public void testAsDouble_WhenValueIsNumber_ReturnsDoubleValue() {
        double doubleValue = 1.23;
        POJONode node = new POJONode(doubleValue);

        double result = node.asDouble(0.0);

        assertEquals(doubleValue, result, 0.001);
    }

    @Test
    public void testAsDouble_WhenValueIsNotNumber_ReturnsDefaultValue() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        double result = node.asDouble(0.0);

        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testSerialize_WhenValueIsNull_CallsDefaultSerializeNull() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        provider.setCalledDefaultSerializeNull(false);
        POJONode node = new POJONode(null);

        node.serialize(generator, provider);

        assertTrue(provider.isCalledDefaultSerializeNull());
    }

    @Test
    public void testSerialize_WhenValueIsJsonSerializable_CallsJsonSerializableSerialize() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        provider.setCalledDefaultSerializeNull(false);
        JsonSerializableStub jsonSerializable = new JsonSerializableStub();
        POJONode node = new POJONode(jsonSerializable);

        node.serialize(generator, provider);

        assertTrue(jsonSerializable.isCalledSerialize());
    }

    @Test
    public void testSerialize_WhenValueIsNull_CallsWriteObjectWithNull() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        provider.setCalledDefaultSerializeNull(false);
        POJONode node = new POJONode(null);

        node.serialize(generator, provider);

        assertTrue(generator.isCalledWriteObjectWithNull());
    }

    @Test
    public void testSerialize_WhenValueIsNotJsonSerializable_CallsWriteObjectWithValue() throws IOException {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        provider.setCalledDefaultSerializeNull(false);
        Object value = new Object();
        POJONode node = new POJONode(value);

        node.serialize(generator, provider);

        assertTrue(generator.isCalledWriteObjectWithValue());
        assertEquals(value, generator.getWriteObjectValue());
        assertFalse(provider.isCalledDefaultSerializeNull());
    }

    @Test
    public void testGetPojo_WhenCalled_ReturnsValue() {
        Object result = node.getPojo();

        assertEquals(pojo, result);
    }

    @Test
    public void testEquals_WhenSameObject_ReturnsTrue() {
        boolean result = node.equals(node);

        assertTrue(result);
    }

    @Test
    public void testEquals_WhenObjectIsNull_ReturnsFalse() {
        boolean result = node.equals(null);

        assertFalse(result);
    }

    @Test
    public void testEquals_WhenObjectIsNotPOJONode_ReturnsFalse() {
        boolean result = node.equals("test");

        assertFalse(result);
    }

    @Test
    public void testEquals_WhenValuesAreEqual_ReturnsTrue() {
        POJONode other = new POJONode(pojo);

        boolean result = node.equals(other);

        assertTrue(result);
    }

    @Test
    public void testEquals_WhenValuesAreNotEqual_ReturnsFalse() {
        Object differentPojo = new Object();
        POJONode other = new POJONode(differentPojo);

        boolean result = node.equals(other);

        assertFalse(result);
    }

    @Test
    public void testHashCode_WhenCalled_ReturnsHashCodeOfValue() {
        int expectedHash = pojo.hashCode();

        int result = node.hashCode();

        assertEquals(expectedHash, result);
    }

    @Test
    public void testToString_WhenValueIsByteArray_ReturnsBinaryValueString() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode node = new POJONode(byteArray);
        String expectedString = String.format("(binary value of %d bytes)", byteArray.length);

        String result = node.toString();

        assertEquals(expectedString, result);
    }

    @Test
    public void testToString_WhenValueIsRawValue_ReturnsRawValueString() {
        String rawValueString = "test";
        RawValue rawValue = new RawValue(rawValueString);
        POJONode node = new POJONode(rawValue);
        String expectedString = String.format("(raw value '%s')", rawValueString);

        String result = node.toString();

        assertEquals(expectedString, result);
    }

    @Test
    public void testToString_WhenValueIsNotByteArrayOrRawValue_ReturnsValueToString() {
        String stringValue = "test";
        POJONode node = new POJONode(stringValue);

        String result = node.toString();

        assertEquals(stringValue, result);
    }
}