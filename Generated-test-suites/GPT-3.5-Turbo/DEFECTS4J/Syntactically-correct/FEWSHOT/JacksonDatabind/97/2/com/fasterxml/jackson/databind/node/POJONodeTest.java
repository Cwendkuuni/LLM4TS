package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class POJONodeTest {

    private POJONode pojoNode;
    private Object value;

    @Before
    public void setUp() {
        value = new Object();
        pojoNode = new POJONode(value);
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
    public void testBinaryValue_withByteArrayValue_shouldReturnByteArray() throws IOException {
        byte[] byteArray = new byte[]{0x00, 0x01, 0x02};
        pojoNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, pojoNode.binaryValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBinaryValue_withNonByteArrayValue_shouldThrowException() throws IOException {
        pojoNode.binaryValue();
    }

    @Test
    public void testAsText_withNonNullValue_shouldReturnStringRepresentationOfValue() {
        String stringValue = value.toString();
        assertEquals(stringValue, pojoNode.asText());
    }

    @Test
    public void testAsText_withNullValue_shouldReturnNull() {
        pojoNode = new POJONode(null);
        assertNull(pojoNode.asText());
    }

    @Test
    public void testAsText_withDefaultValueAndNonNullValue_shouldReturnStringRepresentationOfValue() {
        String defaultValue = "default";
        assertEquals(value.toString(), pojoNode.asText(defaultValue));
    }

    @Test
    public void testAsText_withDefaultValueAndNullValue_shouldReturnDefaultValue() {
        String defaultValue = "default";
        pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asText(defaultValue));
    }

    @Test
    public void testAsBoolean_withNonNullBooleanValue_shouldReturnValueAsBoolean() {
        boolean booleanValue = true;
        pojoNode = new POJONode(booleanValue);
        assertEquals(booleanValue, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsBoolean_withNullValue_shouldReturnDefaultValue() {
        boolean defaultValue = true;
        pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asBoolean(defaultValue));
    }

    @Test
    public void testAsBoolean_withNonBooleanValue_shouldReturnDefaultValue() {
        boolean defaultValue = true;
        Object nonBooleanValue = new Object();
        pojoNode = new POJONode(nonBooleanValue);
        assertEquals(defaultValue, pojoNode.asBoolean(defaultValue));
    }

    @Test
    public void testAsInt_withNonNullNumericValue_shouldReturnValueAsInt() {
        int intValue = 1;
        pojoNode = new POJONode(intValue);
        assertEquals(intValue, pojoNode.asInt(0));
    }

    @Test
    public void testAsInt_withNullValue_shouldReturnDefaultValue() {
        int defaultValue = 0;
        pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asInt(defaultValue));
    }

    @Test
    public void testAsInt_withNonNumericValue_shouldReturnDefaultValue() {
        int defaultValue = 0;
        Object nonNumericValue = new Object();
        pojoNode = new POJONode(nonNumericValue);
        assertEquals(defaultValue, pojoNode.asInt(defaultValue));
    }

    @Test
    public void testAsLong_withNonNullNumericValue_shouldReturnValueAsLong() {
        long longValue = 1L;
        pojoNode = new POJONode(longValue);
        assertEquals(longValue, pojoNode.asLong(0L));
    }

    @Test
    public void testAsLong_withNullValue_shouldReturnDefaultValue() {
        long defaultValue = 0L;
        pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asLong(defaultValue));
    }

    @Test
    public void testAsLong_withNonNumericValue_shouldReturnDefaultValue() {
        long defaultValue = 0L;
        Object nonNumericValue = new Object();
        pojoNode = new POJONode(nonNumericValue);
        assertEquals(defaultValue, pojoNode.asLong(defaultValue));
    }

    @Test
    public void testAsDouble_withNonNullNumericValue_shouldReturnValueAsDouble() {
        double doubleValue = 1.0;
        pojoNode = new POJONode(doubleValue);
        assertEquals(doubleValue, pojoNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testAsDouble_withNullValue_shouldReturnDefaultValue() {
        double defaultValue = 0.0;
        pojoNode = new POJONode(null);
        assertEquals(defaultValue, pojoNode.asDouble(defaultValue), 0.0001);
    }

    @Test
    public void testAsDouble_withNonNumericValue_shouldReturnDefaultValue() {
        double defaultValue = 0.0;
        Object nonNumericValue = new Object();
        pojoNode = new POJONode(nonNumericValue);
        assertEquals(defaultValue, pojoNode.asDouble(defaultValue), 0.0001);
    }

    @Test
    public void testSerialize_withNullValue_shouldCallDefaultSerializeNull() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        pojoNode = new POJONode(null);

        pojoNode.serialize(generator, provider);

        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_withJsonSerializableValue_shouldCallSerializeMethodOnValue() throws IOException {
        JsonSerializable jsonSerializable = mock(JsonSerializable.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        pojoNode = new POJONode(jsonSerializable);

        pojoNode.serialize(generator, provider);

        verify(jsonSerializable).serialize(generator, provider);
    }

    @Test
    public void testSerialize_withNonJsonSerializableValue_shouldCallWriteObjectMethodOnGenerator() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        pojoNode.serialize(generator, provider);

        verify(generator).writeObject(value);
    }

    @Test
    public void testGetPojo_shouldReturnWrappedValue() {
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEquals_withSameObjectInstance_shouldReturnTrue() {
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testEquals_withEqualPojoNodes_shouldReturnTrue() {
        POJONode other = new POJONode(value);
        assertTrue(pojoNode.equals(other));
    }

    @Test
    public void testEquals_withDifferentPojoNodes_shouldReturnFalse() {
        Object otherValue = new Object();
        POJONode other = new POJONode(otherValue);
        assertFalse(pojoNode.equals(other));
    }

    @Test
    public void testEquals_withNonPojoNodeObject_shouldReturnFalse() {
        Object other = new Object();
        assertFalse(pojoNode.equals(other));
    }

    @Test
    public void testHashCode_withEqualPojoNodes_shouldReturnSameHashCode() {
        POJONode other = new POJONode(value);
        assertEquals(pojoNode.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_withDifferentPojoNodes_shouldReturnDifferentHashCodes() {
        Object otherValue = new Object();
        POJONode other = new POJONode(otherValue);
        assertNotEquals(pojoNode.hashCode(), other.hashCode());
    }

    @Test
    public void testToString_withByteArrayValue_shouldReturnBinaryValueString() {
        byte[] byteArray = new byte[]{0x00, 0x01, 0x02};
        pojoNode = new POJONode(byteArray);
        String expectedString = String.format("(binary value of %d bytes)", byteArray.length);
        assertEquals(expectedString, pojoNode.toString());
    }

    @Test
    public void testToString_withRawValue_shouldReturnRawValueString() {
        RawValue rawValue = mock(RawValue.class);
        pojoNode = new POJONode(rawValue);
        when(rawValue.toString()).thenReturn("raw value");
        String expectedString = "(raw value 'raw value')";
        assertEquals(expectedString, pojoNode.toString());
    }

    @Test
    public void testToString_withOtherValue_shouldReturnStringRepresentationOfValue() {
        String stringValue = value.toString();
        assertEquals(stringValue, pojoNode.toString());
    }
}