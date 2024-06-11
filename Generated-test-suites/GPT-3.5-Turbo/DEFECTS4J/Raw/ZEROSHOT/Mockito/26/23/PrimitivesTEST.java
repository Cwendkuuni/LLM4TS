import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(char.class));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(byte.class));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(short.class));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(int.class));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(long.class));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(float.class));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(double.class));
        assertEquals(null, Primitives.primitiveTypeOf(Object.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
        assertEquals(null, Primitives.primitiveWrapperOf(Object.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
        assertEquals(null, Primitives.primitiveValueOrNullFor(Object.class));
    }
}