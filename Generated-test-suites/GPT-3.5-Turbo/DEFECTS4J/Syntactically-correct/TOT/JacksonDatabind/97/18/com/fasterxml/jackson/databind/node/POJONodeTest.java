import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {
    
    private POJONode pojoNode;
    
    @Before
    public void setUp() {
        pojoNode = new POJONode(null);
    }
    
    @Test
    public void testGetNodeType() {
        assertSame(JsonNodeType.POJO, pojoNode.getNodeType());
    }
    
    @Test
    public void testAsToken() {
        assertSame(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }
    
    @Test
    public void testBinaryValue_WhenValueIsByteArray_ReturnsByteArray() throws IOException {
        byte[] byteArray = new byte[] { 1, 2, 3 };
        pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }
    
    @Test
    public void testBinaryValue_WhenValueIsNotByteArray_CallsSuperMethod() throws IOException {
        Object nonByteArray = "value";
        pojoNode = new POJONode(nonByteArray);
        assertThrows(IOException.class, () -> {
            pojoNode.binaryValue();
        });
    }
    
    @Test
    public void testAsText_WhenValueIsNull_ReturnsNull() {
        assertEquals("null", pojoNode.asText());
    }
    
    @Test
    public void testAsText_WhenValueIsNotNull_ReturnsValueAsString() {
        Object value = "test";
        pojoNode = new POJONode(value);
        assertEquals(value.toString(), pojoNode.asText());
    }
    
    @Test
    public void testAsText_WithDefaultValue_WhenValueIsNull_ReturnsDefaultValue() {
        String defaultValue = "default";
        assertEquals(defaultValue, pojoNode.asText(defaultValue));
    }
    
    @Test
    public void testAsText_WithDefaultValue_WhenValueIsNotNull_ReturnsValueAsString() {
        Object value = "test";
        String defaultValue = "default";
        pojoNode = new POJONode(value);
        assertEquals(value.toString(), pojoNode.asText(defaultValue));
    }
    
    @Test
    public void testAsBoolean_WhenValueIsNull_ReturnsDefaultValue() {
        boolean defaultValue = true;
        assertTrue(pojoNode.asBoolean(defaultValue));
    }
    
    @Test
    public void testAsBoolean_WhenValueIsBoolean_ReturnsBooleanValue() {
        Object value = true;
        pojoNode = new POJONode(value);
        assertTrue(pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt_WhenValueIsNotNumber_ReturnsDefaultValue() {
        int defaultValue = 10;
        assertEquals(defaultValue, pojoNode.asInt(defaultValue));
    }
    
    @Test
    public void testAsInt_WhenValueIsNumber_ReturnsIntValue() {
        Object value = 5;
        pojoNode = new POJONode(value);
        assertEquals((Integer)value, pojoNode.asInt(0));
    }
    
    @Test
    public void testAsLong_WhenValueIsNotNumber_ReturnsDefaultValue() {
        long defaultValue = 20L;
        assertEquals(defaultValue, pojoNode.asLong(defaultValue));
    }
    
    @Test
    public void testAsLong_WhenValueIsNumber_ReturnsLongValue() {
        Object value = 100L;
        pojoNode = new POJONode(value);
        assertEquals((Long)value, pojoNode.asLong(0L));
    }
    
    @Test
    public void testAsDouble_WhenValueIsNotNumber_ReturnsDefaultValue() {
        double defaultValue = 0.5;
        assertEquals(defaultValue, pojoNode.asDouble(defaultValue), 0.0);
    }
    
    @Test
    public void testAsDouble_WhenValueIsNumber_ReturnsDoubleValue() {
        Object value = 3.14;
        pojoNode = new POJONode(value);
        assertEquals((Double)value, pojoNode.asDouble(0.0), 0.0);
    }
    
    @Test
    public void testSerialize_WhenValueIsNull_DelegatesToDefaultSerializeNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        pojoNode.serialize(gen, ctxt);
        
        verify(ctxt).defaultSerializeNull(gen);
    }
    
    @Test
    public void testSerialize_WhenValueIsJsonSerializable_CallsSerializeMethodOnValue() throws IOException {
        JsonSerializable value = mock(JsonSerializable.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        pojoNode = new POJONode(value);
        pojoNode.serialize(gen, ctxt);
        
        verify(value).serialize(gen, ctxt);
    }
    
    @Test
    public void testSerialize_WhenValueIsNotJsonSerializable_CallsWriteObjectOnGenerator() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        pojoNode = new POJONode(value);
        pojoNode.serialize(gen, ctxt);
        
        verify(gen).writeObject(value);
    }
    
    @Test
    public void testGetPojo_ReturnsValue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }
    
    @Test
    public void testEquals_WhenObjectIsSameInstance_ReturnsTrue() {
        assertTrue(pojoNode.equals(pojoNode));
    }
    
    @Test
    public void testEquals_WhenObjectIsNull_ReturnsFalse() {
        assertFalse(pojoNode.equals(null));
    }
    
    @Test
    public void testEquals_WhenObjectIsNotPOJONode_ReturnsFalse() {
        assertFalse(pojoNode.equals(new Object()));
    }
    
    @Test
    public void testEquals_WhenValuesAreBothNull_ReturnsTrue() {
        POJONode other = new POJONode(null);
        assertTrue(pojoNode.equals(other));
    }
    
    @Test
    public void testEquals_WhenValuesAreEqual_ReturnsTrue() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        POJONode other = new POJONode(value);
        assertTrue(pojoNode.equals(other));
    }
    
    @Test
    public void testEquals_WhenValuesAreNotEqual_ReturnsFalse() {
        Object value1 = new Object();
        Object value2 = new Object();
        pojoNode = new POJONode(value1);
        POJONode other = new POJONode(value2);
        assertFalse(pojoNode.equals(other));
    }
    
    @Test
    public void testHashCode_ReturnsValueHashCode() {
        Object value = new Object();
        pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }
    
    @Test
    public void testToString_WhenValueIsByteArray_ReturnsBinaryValueMessage() {
        byte[] byteArray = new byte[] { 1, 2, 3 };
        pojoNode = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }
    
    @Test
    public void testToString_WhenValueIsRawValue_ReturnsRawValueMessage() {
        RawValue rawValue = new RawValue("raw");
        pojoNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", pojoNode.toString());
    }
    
    @Test
    public void testToString_WhenValueIsNotByteArrayOrRawValue_ReturnsValueToString() {
        Object value = "test";
        pojoNode = new POJONode(value);
        assertEquals(value.toString(), pojoNode.toString());
    }
}