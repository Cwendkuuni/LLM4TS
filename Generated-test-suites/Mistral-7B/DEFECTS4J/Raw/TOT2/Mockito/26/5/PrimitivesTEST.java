package org.mockito.internal.util.test; // Add your test package here

import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOfInteger() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expectedPrimitiveClass = Integer.TYPE;
        Class<Integer> actualPrimitiveClass = Primitives.primitiveTypeOf(integerClass);
        assertEquals(expectedPrimitiveClass, actualPrimitiveClass);
    }

    @Test
    public void testIsPrimitiveWrapperForBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOfString() {
        Class<String> stringClass = String.class;
        Object expectedNull = null;
        Object actualResult = Primitives.primitiveWrapperOf(stringClass);
        assertEquals(expectedNull, actualResult);
    }

    @Test
    public void testPrimitiveValueOrNullForFloat() {
        Class<Float> floatClass = Float.class;
        Float expectedNull = null;
        Float actualResult = Primitives.primitiveValueOrNullFor(floatClass);
        assertEquals(expectedNull, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOfNullClass() {
        Class<?> nullClass = null;
        Primitives.primitiveTypeOf(nullClass);
    }

    @Test
    public void testPrimitiveTypeOfObjectClass() {
        Class<Object> objectClass = Object.class;
        Class<?> expectedNull = null;
        Class<?> actualResult = Primitives.primitiveTypeOf(objectClass);
        assertEquals(expectedNull, actualResult);
    }

    @Test
    public void testIsPrimitiveWrapperForString() {
        Class<String> stringClass = String.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(stringClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOfString() {
        Class<String> stringClass = String.class;
        String expectedString = "Test String";
        Object actualResult = Primitives.primitiveWrapperOf(stringClass);
        assertNotNull(actualResult);
        assertEquals(expectedString, actualResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveValueOrNullForNullClass() {
        Class<?> nullClass = null;
        Primitives.primitiveValueOrNullFor(nullClass);
    }

    @Test
    public void testPrimitiveValueOrNullForBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        boolean expectedResult = false;
        Object actualResult = Primitives.primitiveValueOrNullFor(booleanClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveValueOrNullForCharacter() {
        Class<Character> characterClass = Character.class;
        char expectedResult = '\u0000';
        Object actualResult = Primitives.primitiveValueOrNullFor(characterClass);
        assertEquals(expectedResult, actualResult);
    }
}