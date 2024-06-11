package org.mockito.internal.util.test;

import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> primitiveType = Primitives.primitiveTypeOf(intClass);
        assertSame(Integer.TYPE, primitiveType);

        Class<String> stringClass = String.class;
        Class<String> primitiveTypeString = Primitives.primitiveTypeOf(stringClass);
        assertNull(primitiveTypeString);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean isWrapper = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertTrue(isWrapper);

        Class<Integer> intClass = Integer.class;
        isWrapper = Primitives.isPrimitiveWrapper(intClass);
        assertFalse(isWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertTrue(booleanWrapper instanceof Boolean);
        assertFalse(booleanWrapper instanceof Byte);

        Class<Byte> byteClass = Byte.class;
        Byte bytePrimitive = Primitives.primitiveValueOrNullFor(byteClass);
        assertNotNull(bytePrimitive);
        assertEquals((byte) 0, bytePrimitive);

        byte testByte = 10;
        Byte byteWrapper = Primitives.primitiveWrapperOf(byteClass);
        assertNotNull(byteWrapper);
        assertEquals((byte) testByte, byteWrapper);
    }
}