package org.mockito.internal.util;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, booleanClass);

        Class<Character> characterClass = Primitives.primitiveTypeOf(char.class);
        assertEquals(char.class, characterClass);

        Class<Byte> byteClass = Primitives.primitiveTypeOf(byte.class);
        assertEquals(byte.class, byteClass);

        Class<Short> shortClass = Primitives.primitiveTypeOf(short.class);
        assertEquals(short.class, shortClass);

        Class<Integer> integerClass = Primitives.primitiveTypeOf(int.class);
        assertEquals(int.class, integerClass);

        Class<Long> longClass = Primitives.primitiveTypeOf(long.class);
        assertEquals(long.class, longClass);

        Class<Float> floatClass = Primitives.primitiveTypeOf(float.class);
        assertEquals(float.class, floatClass);

        Class<Double> doubleClass = Primitives.primitiveTypeOf(double.class);
        assertEquals(double.class, doubleClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean isBooleanWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(isBooleanWrapper);

        boolean isCharacterWrapper = Primitives.isPrimitiveWrapper(Character.class);
        assertTrue(isCharacterWrapper);

        boolean isByteWrapper = Primitives.isPrimitiveWrapper(Byte.class);
        assertTrue(isByteWrapper);

        boolean isShortWrapper = Primitives.isPrimitiveWrapper(Short.class);
        assertTrue(isShortWrapper);

        boolean isIntegerWrapper = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(isIntegerWrapper);

        boolean isLongWrapper = Primitives.isPrimitiveWrapper(Long.class);
        assertTrue(isLongWrapper);

        boolean isFloatWrapper = Primitives.isPrimitiveWrapper(Float.class);
        assertTrue(isFloatWrapper);

        boolean isDoubleWrapper = Primitives.isPrimitiveWrapper(Double.class);
        assertTrue(isDoubleWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.FALSE, booleanWrapper);

        Character characterWrapper = Primitives.primitiveWrapperOf(char.class);
        assertEquals('\u0000', characterWrapper.charValue());

        Byte byteWrapper = Primitives.primitiveWrapperOf(byte.class);
        assertEquals((byte) 0, byteWrapper.byteValue());

        Short shortWrapper = Primitives.primitiveWrapperOf(short.class);
        assertEquals((short) 0, shortWrapper.shortValue());

        Integer integerWrapper = Primitives.primitiveWrapperOf(int.class);
        assertEquals(0, integerWrapper.intValue());

        Long longWrapper = Primitives.primitiveWrapperOf(long.class);
        assertEquals(0L, longWrapper.longValue());

        Float floatWrapper = Primitives.primitiveWrapperOf(float.class);
        assertEquals(0F, floatWrapper.floatValue(), 0.0);

        Double doubleWrapper = Primitives.primitiveWrapperOf(double.class);
        assertEquals(0D, doubleWrapper.doubleValue(), 0.0);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, booleanValue);

        Character characterValue = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', characterValue.charValue());

        Byte byteValue = Primitives.primitiveValueOrNullFor(byte.class);
        assertEquals((byte) 0, byteValue.byteValue());

        Short shortValue = Primitives.primitiveValueOrNullFor(short.class);
        assertEquals((short) 0, shortValue.shortValue());

        Integer integerValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, integerValue.intValue());

        Long longValue = Primitives.primitiveValueOrNullFor(long.class);
        assertEquals(0L, longValue.longValue());

        Float floatValue = Primitives.primitiveValueOrNullFor(float.class);
        assertEquals(0F, floatValue.floatValue(), 0.0);

        Double doubleValue = Primitives.primitiveValueOrNullFor(double.class);
        assertEquals(0D, doubleValue.doubleValue(), 0.0);
    }
}