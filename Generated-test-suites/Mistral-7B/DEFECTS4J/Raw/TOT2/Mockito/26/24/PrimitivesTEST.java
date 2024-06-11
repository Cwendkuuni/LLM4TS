
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitivesTypeOf() {
        Class<Integer> intClass = int.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        assertTrue(Integer.TYPE.isAssignableFrom(intType));
    }

    @Test
    public void testIsPrimitiveWrapperBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        assertTrue(Primitives.isPrimitiveWrapper(booleanClass));
    }

    @Test
    public void testIsPrimitiveWrapperString() {
        Class<String> stringClass = String.class;
        assertFalse(Primitives.isPrimitiveWrapper(stringClass));
    }

    @Test
    public void testPrimitiveWrapperOfBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanClass);
        assertTrue(booleanWrapper instanceof Boolean);
    }

    @Test
    public void testPrimitiveWrapperOfString() {
        Class<String> stringClass = String.class;
        assertNull(Primitives.primitiveWrapperOf(stringClass));
    }

    @Test
    public void testPrimitiveValueOrNullForBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(booleanValue, false);
    }

    @Test
    public void testPrimitiveValueOrNullForString() {
        Class<String> stringClass = String.class;
        assertNull(Primitives.primitiveValueOrNullFor(stringClass));
    }

    @Test
    public void testPrimitiveTypeOfNull() {
        assertNull(Primitives.primitiveTypeOf(null));
    }

    @Test
    public void testPrimitiveTypeOfNonPrimitive() {
        Class<String> stringClass = String.class;
        assertNull(Primitives.primitiveTypeOf(stringClass));
    }

    @Test
    public void testIsPrimitiveWrapperNull() {
        assertFalse(Primitives.isPrimitiveWrapper(null));
    }

    @Test
    public void testIsPrimitiveWrapperNonWrapper() {
        Class<Integer> integerClass = Integer.class;
        assertFalse(Primitives.isPrimitiveWrapper(integerClass));
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveWrapperOfNull() {
        Class<Object> objectClass = Object.class;
        Primitives.primitiveWrapperOf(objectClass);
    }

    @Test
    public void testPrimitiveWrapperOfNonWrapper() {
        Class<Integer> integerClass = Integer.class;
        assertNull(Primitives.primitiveWrapperOf(integerClass));
    }

    @Test
    public void testPrimitiveValueOrNullForNull() {
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }

    @Test
    public void testPrimitiveValueOrNullForNonPrimitive() {
        Class<String> stringClass = String.class;
        assertNull(Primitives.primitiveValueOrNullFor(stringClass));
    }
}
