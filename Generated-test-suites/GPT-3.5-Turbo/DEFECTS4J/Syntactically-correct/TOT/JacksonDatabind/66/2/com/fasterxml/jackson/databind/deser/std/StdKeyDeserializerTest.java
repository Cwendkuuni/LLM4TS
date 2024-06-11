package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void deserializeKey_whenKeyIsNull_shouldReturnNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsTrueForBooleanType_shouldReturnBooleanTrue() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsFalseForBooleanType_shouldReturnBooleanFalse() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsNotTrueOrFalseForBooleanType_shouldHandleWeirdKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertEquals("not a valid representation", keyDeserializer.deserializeKey("invalid", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsIntegerForIntegerType_shouldReturnIntegerObject() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
        assertEquals(10, keyDeserializer.deserializeKey("10", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsOutOfByteRangeForByteType_shouldHandleWeirdKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        assertEquals("overflow, value can not be represented as 8-bit value", keyDeserializer.deserializeKey("300", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsSingleCharacterForCharType_shouldReturnCharacterObject() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_CHAR, Character.class);
        assertEquals('A', keyDeserializer.deserializeKey("A", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsNotSingleCharacterForCharType_shouldHandleWeirdKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_CHAR, Character.class);
        assertEquals("can only convert 1-character Strings", keyDeserializer.deserializeKey("AB", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsDoubleForDoubleType_shouldReturnDoubleObject() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_DOUBLE, Double.class);
        assertEquals(3.14, keyDeserializer.deserializeKey("3.14", null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsNullForEnumType_shouldReturnNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_ENUM, MyEnum.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }
    
    @Test
    public void deserializeKey_whenKeyIsNotAValidRepresentationForEnumType_shouldHandleWeirdKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_ENUM, MyEnum.class);
        assertEquals("not a valid representation", keyDeserializer.deserializeKey("Invalid", null));
    }

    enum MyEnum { VALUE_ONE, VALUE_TWO }

}