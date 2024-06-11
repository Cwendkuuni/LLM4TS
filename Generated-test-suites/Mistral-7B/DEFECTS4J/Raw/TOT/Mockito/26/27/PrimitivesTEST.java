package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        assertTrue(intType.isPrimitive());
        assertEquals(Integer.TYPE, intType);

        Class<String> stringClass = String.class;
        Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
        assertFalse(stringType.isPrimitive());
        assertNull(stringType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
        assertEquals(Boolean.class, Primitives.primitiveTypeOf(booleanWrapperClass));

        Class<Integer> integerClass = Integer.class;
        assertFalse(Primitives.isPrimitiveWrapper(integerClass));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(integerClass));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertTrue(booleanWrapper instanceof Boolean);
        assertEquals(false, booleanWrapper);

        Class<Byte> byteWrapperClass = Byte.class;
        Byte byteWrapper = Primitives.primitiveWrapperOf(byteWrapperClass);
        assertTrue(byteWrapper instanceof Byte);
        assertEquals((byte) 0, byteWrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor_boolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(false, booleanValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_char() {
        Class<Character> charClass = Character.class;
        Character charValue = Primitives.primitiveValueOrNullFor(charClass);
        assertTrue(charValue instanceof Character);
        assertEquals('\u0000', charValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_byte() {
        Class<Byte> byteClass = Byte.class;
        Byte byteValue = Primitives.primitiveValueOrNullFor(byteClass);
        assertTrue(byteValue instanceof Byte);
        assertEquals((byte) 0, byteValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_short() {
        Class<Short> shortClass = Short.class;
        Short shortValue = Primitives.primitiveValueOrNullFor(shortClass);
        assertTrue(shortValue instanceof Short);
        assertEquals((short) 0, shortValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_int() {
        Class<Integer> intClass = Integer.class;
        Integer intValue = Primitives.primitiveValueOrNullFor(intClass);
        assertTrue(intValue instanceof Integer);
        assertEquals(0, intValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_long() {
        Class<Long> longClass = Long.class;
        Long longValue = Primitives.primitiveValueOrNullFor(longClass);
        assertTrue(longValue instanceof Long);
        assertEquals(0L, longValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_float() {
        Class<Float> floatClass = Float.class;
        Float floatValue = Primitives.primitiveValueOrNullFor(floatClass);
        assertTrue(floatValue instanceof Float);
        assertEquals(0F, floatValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_double() {
        Class<Double> doubleClass = Double.class;
        Double doubleValue = Primitives.primitiveValueOrNullFor(doubleClass);
        assertTrue(doubleValue instanceof Double);
        assertEquals(0D, doubleValue);
    }
}