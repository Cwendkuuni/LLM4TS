package org.mockito.internal.util;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.internal.util.Primitives.*;

public class PrimitivesTest {
    

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass() {
        Class<Byte> byteClass = Byte.TYPE;
        Class<Byte> expectedType = byteClass;
        Class<Byte> actualType = primitiveTypeOf(byteClass);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expectedType = integerClass;
        Class<Integer> actualType = primitiveTypeOf(integerClass);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOf_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        primitiveTypeOf(stringClass);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expected = true;
        boolean actual = isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveClass() {
        Class<Byte> byteClass = Byte.TYPE;
        boolean expected = false;
        boolean actual = isPrimitiveWrapper(byteClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf_BooleanWrapperClass() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean expected = Boolean.FALSE;
        Boolean actual = primitiveWrapperOf(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf_BooleanPrimitiveType() {
        Class<Boolean> booleanType = Boolean.TYPE;
        boolean expected = false;
        boolean actual = ((Boolean) primitiveValueOrNullFor(booleanType)).booleanValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_NullParameter() {
        Class<String> stringClass = null;
        Object expected = null;
        Object actual = primitiveValueOrNullFor(stringClass);
        assertNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_BooleanPrimitiveType() {
        Class<Boolean> booleanType = Boolean.TYPE;
        Boolean expected = Boolean.FALSE;
        Boolean actual = (Boolean) primitiveValueOrNullFor(booleanType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_CharacterPrimitiveType() {
        Class<Character> charType = Character.TYPE;
        Character expected = '\u0000';
        Character actual = (Character) primitiveValueOrNullFor(charType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_BytePrimitiveType() {
        Class<Byte> byteType = Byte.TYPE;
        Byte expected = Byte.MIN_VALUE;
        Byte actual = (Byte) primitiveValueOrNullFor(byteType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_ShortPrimitiveType() {
        Class<Short> shortType = Short.TYPE;
        Short expected = Short.MIN_VALUE;
        Short actual = (Short) primitiveValueOrNullFor(shortType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_IntegerPrimitiveType() {
        Class<Integer> intType = Integer.TYPE;
        Integer expected = Integer.MIN_VALUE;
        Integer actual = (Integer) primitiveValueOrNullFor(intType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_LongPrimitiveType() {
        Class<Long> longType = Long.TYPE;
        Long expected = Long.MIN_VALUE;
        Long actual = (Long) primitiveValueOrNullFor(longType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_FloatPrimitiveType() {
        Class<Float> floatType = Float.TYPE;
        Float expected = Float.NEGATIVE_INFINITY;
        Float actual = (Float) primitiveValueOrNullFor(floatType);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testPrimitiveValueOrNullFor_DoublePrimitiveType() {
        Class<Double> doubleType = Double.TYPE;
        Double expected = Double.NEGATIVE_INFINITY;
        Double actual = (Double) primitiveValueOrNullFor(doubleType);
        assertEquals(expected, actual, 0.0);
    }

    
}