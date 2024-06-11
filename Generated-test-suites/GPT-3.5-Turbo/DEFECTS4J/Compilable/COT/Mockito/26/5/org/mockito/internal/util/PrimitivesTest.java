package org.mockito.internal.util;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType_returnPrimitiveType() {
        Class<Boolean> result = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperType_returnPrimitiveType() {
        Class<Boolean> result = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveType_returnFalse() {
        boolean result = Primitives.isPrimitiveWrapper(boolean.class);
        assertFalse(result);
    }

    @Test
    public void testIsPrimitiveWrapper_wrapperType_returnTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveType_returnWrapperObject() {
        Boolean result = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPrimitiveWrapperOf_wrapperType_returnWrapperObject() {
        Boolean result = Primitives.primitiveWrapperOf(Boolean.class);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_returnPrimitiveValue() {
        boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_wrapperType_returnPrimitiveValue() {
        boolean result = Primitives.primitiveValueOrNullFor(Boolean.class);
        assertFalse(result);
    }
}