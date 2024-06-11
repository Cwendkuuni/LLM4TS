package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Testing primitive type
        Class<Integer> clazz = Integer.TYPE;
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(clazz));

        // Testing wrapper type
        clazz = Integer.class;
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(clazz));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Testing primitive wrapper type
        Class<Integer> clazz = Integer.class;
        assertTrue(Primitives.isPrimitiveWrapper(clazz));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Testing primitive wrapper type
        Class<Integer> clazz = Integer.class;
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(clazz));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Testing primitive type
        Class<Integer> clazz = Integer.TYPE;
        assertEquals(0, (int) Primitives.primitiveValueOrNullFor(clazz));

        // Testing wrapper type
        clazz = Integer.class;
        assertEquals(Integer.valueOf(0), Primitives.primitiveValueOrNullFor(clazz));
    }
}