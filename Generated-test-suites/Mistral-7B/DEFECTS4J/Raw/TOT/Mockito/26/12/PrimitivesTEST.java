package org.mockito.internal.util.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @BeforeClass
    public static void initMaps() {
        Primitives.initMaps();
    }

    @Test
    public void testPrimitiveTypeOf() {
        // Test with primitive types
        Class<Boolean> booleanType = Boolean.TYPE;
        Class<Boolean> booleanWrapperType = Boolean.class;
        Class<Boolean> actual = Primitives.primitiveTypeOf(booleanWrapperType);
        assertTrue(booleanType.isAssignableFrom(actual));

        // Test with wrapper types
        Class<Integer> integerType = Integer.TYPE;
        Class<Integer> integerWrapperType = Integer.class;
        actual = Primitives.primitiveTypeOf(integerWrapperType);
        assertTrue(integerType.isAssignableFrom(actual));

        // Test with non-primitive types
        Class<String> stringType = String.class;
        actual = Primitives.primitiveTypeOf(stringType);
        assertNull(actual);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test with primitive wrapper types
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));

        // Test with non-primitive wrapper types
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperAndValue() {
        // Test primitiveWrapperOf()
        Class<Boolean> booleanType = Boolean.class;
        Object booleanWrapper = Primitives.primitiveWrapperOf(booleanType);
        assertTrue(booleanWrapper instanceof Boolean);
        assertEquals(false, ((Boolean) booleanWrapper).booleanValue());

        Class<Character> characterType = Character.class;
        Object characterWrapper = Primitives.primitiveWrapperOf(characterType);
        assertTrue(characterWrapper instanceof Character);
        assertEquals('\u0000', ((Character) characterWrapper).charValue());

        // Test primitiveValueOrNullFor()
        Class<Boolean> booleanPrimitiveType = Boolean.TYPE;
        Object booleanValue = Primitives.primitiveValueOrNullFor(booleanPrimitiveType);
        assertNull(booleanValue);

        Class<Integer> integerPrimitiveType = Integer.TYPE;
        Object integerValue = Primitives.primitiveValueOrNullFor(integerPrimitiveType);
        assertEquals(0, ((Number) integerValue).intValue());

        Class<Float> floatPrimitiveType = Float.TYPE;
        Object floatValue = Primitives.primitiveValueOrNullFor(floatPrimitiveType);
        assertNull(floatValue);
    }
}