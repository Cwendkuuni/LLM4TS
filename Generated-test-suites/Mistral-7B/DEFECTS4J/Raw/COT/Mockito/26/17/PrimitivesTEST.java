package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {
    

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass() {
        Class<Boolean> booleanClass = Boolean.TYPE;
        Class<Boolean> expected = Boolean.TYPE;
        Class<Boolean> result = Primitives.primitiveTypeOf(booleanClass);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOf_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Class<String> result = Primitives.primitiveTypeOf(stringClass);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testIsPrimitiveWrapper_BooleanWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean wrapperValue = false;
        Object result = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertEquals(wrapperValue, result);
        assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
    }

    @Test
    public void testIsPrimitiveWrapper_NonWrapperClass() {
        Class<Integer> integerClass = Integer.class;
        assertFalse(Primitives.isPrimitiveWrapper(integerClass));
    }

    @Test
    public void testPrimitiveValueOrNullFor_BooleanPrimitive() {
        Boolean booleanValue = false;
        Object result = Primitives.primitiveValueOrNullFor(booleanValue.getClass());
        assertEquals(booleanValue, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_NullInput() {
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }

    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveClass() {
        Class<Integer> integerClass = Integer.class;
        assertNull(Primitives.primitiveValueOrNullFor(integerClass));
    }

    @Test
    public void testWrapperTypesMapIsPopulatedCorrectly() {
        Map<Class<?>, Class<?>> map = Primitives.wrapperTypes;
        Class<Byte> byteWrapperClass = Byte.class;
        Class<Byte> bytePrimitiveClass = Byte.TYPE;
        assertEquals(bytePrimitiveClass, map.get(byteWrapperClass));
    }

    @Test
    public void testPrimitiveTypesMapIsPopulatedCorrectly() {
        Map<Class<?>, Class<?>> map = Primitives.primitiveTypes;
        Class<Character> charClass = Character.class;
        Class<Character> charType = Character.TYPE;
        assertEquals(charType, map.get(charClass));
    }

    @Test
    public void testWrapperReturnValuesMapIsPopulatedCorrectly() {
        Map<Class<?>, Object> map = Primitives.wrapperReturnValues;
        Class<Byte> byteWrapperClass = Byte.class;
        Byte byteWrapperValue = (byte) 1;
        assertEquals(byteWrapperValue, map.get(byteWrapperClass));
    }

    @Test
    public void testPrimitiveValuesMapIsPopulatedCorrectly() {
        Map<Class<?>, Object> map = Primitives.primitiveValues;
        Class<Byte> bytePrimitiveClass = Byte.TYPE;
        Byte bytePrimitiveValue = (byte) 1;
        assertEquals(bytePrimitiveValue, map.get(bytePrimitiveClass.getClass()));
    }

    
}