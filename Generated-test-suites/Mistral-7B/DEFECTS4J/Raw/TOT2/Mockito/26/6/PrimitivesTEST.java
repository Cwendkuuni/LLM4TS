package org.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<String> stringClass = String.class;
        Class<Boolean> booleanClass = Boolean.class;
        Class<Integer> integerClass = Integer.class;

        Class<Boolean> actualBooleanType = Primitives.primitiveTypeOf(booleanClass);
        assertTrue(actualBooleanType.isPrimitive());
        assertEquals(Boolean.TYPE, actualBooleanType);

        Class<Integer> actualIntegerType = Primitives.primitiveTypeOf(integerClass);
        assertTrue(actualIntegerType.isPrimitive());
        assertEquals(Integer.TYPE, actualIntegerType);

        Class<String> actualStringType = Primitives.primitiveTypeOf(stringClass);
        assertFalse(actualStringType.isPrimitive());
        assertNull(actualStringType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapper = Boolean.class;
        Class<Integer> integerWrapper = Integer.class;

        assertTrue(Primitives.isPrimitiveWrapper(booleanWrapper));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));

        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanType = Boolean.TYPE;
        Class<Character> characterType = Character.TYPE;

        Boolean actualBoolean = Primitives.primitiveWrapperOf(booleanType);
        assertTrue(actualBoolean instanceof Boolean);
        assertEquals(false, actualBoolean);

        Character actualCharacter = Primitives.primitiveWrapperOf(characterType);
        assertTrue(actualCharacter instanceof Character);
        assertEquals('\u0000', actualCharacter);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Byte> byteClass = Byte.class;
        Class<Short> shortClass = Short.class;

        Byte actualByte = Primitives.primitiveValueOrNullFor(byteClass);
        assertTrue(actualByte instanceof Byte);
        assertEquals((byte) 0, actualByte);

        Short actualShort = Primitives.primitiveValueOrNullFor(shortClass);
        assertTrue(actualShort instanceof Short);
        assertEquals((short) 0, actualShort);

        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOfWithNull() {
        Class<Object> nullClass = null;
        Primitives.primitiveTypeOf(nullClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPrimitiveWrapperWithNonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Primitives.isPrimitiveWrapper(stringClass);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveWrapperOfWithNull() {
        Class<Object> nullClass = null;
        Primitives.primitiveWrapperOf(nullClass);
    }

    @Test(expected = ClassCastException.class)
    public void testPrimitiveWrapperOfWithNonPrimitiveWrapper() {
        Class<String> stringClass = String.class;
        Primitives.primitiveWrapperOf(stringClass);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveValueOrNullForWithNull() {
        Class<Object> nullClass = null;
        Primitives.primitiveValueOrNullFor(nullClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveValueOrNullForWithNonPrimitiveType() {
        Class<String> stringClass = String.class;
        Primitives.primitiveValueOrNullFor(stringClass);
    }
}