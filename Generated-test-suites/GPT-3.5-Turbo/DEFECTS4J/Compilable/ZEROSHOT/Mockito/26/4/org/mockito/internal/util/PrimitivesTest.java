package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, booleanClass);

        Class<Character> characterClass = Primitives.primitiveTypeOf(Character.class);
        assertEquals(char.class, characterClass);

        // Repeat the same for other primitive types

        // Uncomment the following code to test an invalid class
        // Class<String> stringClass = Primitives.primitiveTypeOf(String.class);
        // assertNull(stringClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean isBooleanWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(isBooleanWrapper);

        boolean isCharacterWrapper = Primitives.isPrimitiveWrapper(Character.class);
        assertTrue(isCharacterWrapper);

        // Repeat the same for other wrapper types

        // Uncomment the following code to test an invalid class
        // boolean isStringWrapper = Primitives.isPrimitiveWrapper(String.class);
        // assertFalse(isStringWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.FALSE, booleanWrapper);

        Character characterWrapper = Primitives.primitiveWrapperOf(char.class);
        assertEquals(Character.valueOf('\u0000'), characterWrapper);

        // Repeat the same for other primitive types

        // Uncomment the following code to test an invalid class
        // String stringWrapper = Primitives.primitiveWrapperOf(String.class);
        // assertNull(stringWrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(Boolean.FALSE, booleanValue);

        Character characterValue = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals(Character.valueOf('\u0000'), characterValue);

        // Repeat the same for other primitive types

        // Uncomment the following code to test an invalid class
        // String stringValue = Primitives.primitiveValueOrNullFor(String.class);
        // assertNull(stringValue);
    }
}