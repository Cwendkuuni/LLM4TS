package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType_expectSameClass() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, booleanClass);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperType_expectSameClass() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(Boolean.TYPE, booleanClass);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveType_expectFalse() {
        boolean isWrapper = Primitives.isPrimitiveWrapper(boolean.class);
        assertFalse(isWrapper);
    }

    @Test
    public void testIsPrimitiveWrapper_wrapperType_expectTrue() {
        boolean isWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(isWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveType_expectNull() {
        Boolean wrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertNull(wrapper);
    }

    @Test
    public void testPrimitiveWrapperOf_wrapperType_expectWrapperObject() {
        Boolean wrapper = Primitives.primitiveWrapperOf(Boolean.class);
        assertNotNull(wrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_expectNull() {
        Boolean value = Primitives.primitiveValueOrNullFor(boolean.class);
        assertNull(value);
    }

    @Test
    public void testPrimitiveValueOrNullFor_wrapperType_expectDefaultValue() {
        Boolean value = Primitives.primitiveValueOrNullFor(Boolean.class);
        assertEquals(false, (boolean) value);
    }
}