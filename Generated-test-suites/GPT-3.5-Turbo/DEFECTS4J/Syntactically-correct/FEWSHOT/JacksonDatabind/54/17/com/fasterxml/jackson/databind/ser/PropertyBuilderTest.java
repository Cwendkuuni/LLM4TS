package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Test with null type
        assertNull(PropertyBuilder.findSerializationType(null, false, null));
        assertNull(PropertyBuilder.findSerializationType(null, true, null));
    }

    @Test
    public void testGetDefaultValue() {
        // Test with primitive type
        assertEquals(0, PropertyBuilder.getDefaultValue(JavaType.primitiveType(int.class)));
        assertEquals(0L, PropertyBuilder.getDefaultValue(JavaType.primitiveType(long.class)));
        assertEquals(0.0f, PropertyBuilder.getDefaultValue(JavaType.primitiveType(float.class)), 0.0f);
        assertEquals(0.0, PropertyBuilder.getDefaultValue(JavaType.primitiveType(double.class)), 0.0);
        assertEquals(false, PropertyBuilder.getDefaultValue(JavaType.primitiveType(boolean.class)));
        assertEquals((byte) 0, PropertyBuilder.getDefaultValue(JavaType.primitiveType(byte.class)));
        assertEquals((short) 0, PropertyBuilder.getDefaultValue(JavaType.primitiveType(short.class)));
        assertEquals('0', PropertyBuilder.getDefaultValue(JavaType.primitiveType(char.class)));

        // Test with wrapper type for primitives
        assertEquals(Integer.valueOf(0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Integer.class)));
        assertEquals(Long.valueOf(0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Long.class)));
        assertEquals(Float.valueOf(0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Float.class)));
        assertEquals(Double.valueOf(0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Double.class)));
        assertEquals(Boolean.FALSE, PropertyBuilder.getDefaultValue(JavaType.wrapperType(Boolean.class)));
        assertEquals(Byte.valueOf((byte) 0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Byte.class)));
        assertEquals(Short.valueOf((short) 0), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Short.class)));
        assertEquals(Character.valueOf('0'), PropertyBuilder.getDefaultValue(JavaType.wrapperType(Character.class)));

        // Test with String type
        assertEquals("", PropertyBuilder.getDefaultValue(JavaType.construct(String.class)));
        
        // Test with other container type
        assertEquals(JsonInclude.Include.NON_EMPTY, PropertyBuilder.getDefaultValue(JavaType.construct(Map.class)));
        assertEquals(JsonInclude.Include.NON_EMPTY, PropertyBuilder.getDefaultValue(JavaType.construct(Collection.class)));
        assertEquals(JsonInclude.Include.NON_EMPTY, PropertyBuilder.getDefaultValue(JavaType.construct(Array.class)));
        assertEquals(JsonInclude.Include.NON_EMPTY, PropertyBuilder.getDefaultValue(JavaType.construct(Object.class)));
    }
}