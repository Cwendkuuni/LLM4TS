import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test boolean
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.TYPE));
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));

        // Test char
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.TYPE));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));

        // Test byte
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.TYPE));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));

        // Test short
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.TYPE));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));

        // Test int
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.TYPE));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));

        // Test long
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.TYPE));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));

        // Test float
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.TYPE));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));

        // Test double
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.TYPE));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test boolean wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));

        // Test char wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));

        // Test byte wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));

        // Test short wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));

        // Test int wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));

        // Test long wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));

        // Test float wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));

        // Test double wrapper
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test boolean wrapper
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.class));
        assertNull(Primitives.primitiveWrapperOf(boolean.class));

        // Test char wrapper
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(Character.class));
        assertNull(Primitives.primitiveWrapperOf(char.class));

        // Test byte wrapper
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(Byte.class));
        assertNull(Primitives.primitiveWrapperOf(byte.class));

        // Test short wrapper
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(Short.class));
        assertNull(Primitives.primitiveWrapperOf(short.class));

        // Test int wrapper
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(Integer.class));
        assertNull(Primitives.primitiveWrapperOf(int.class));

        // Test long wrapper
        assertEquals(Long.valueOf(0L), Primitives.primitiveWrapperOf(Long.class));
        assertNull(Primitives.primitiveWrapperOf(long.class));

        // Test float wrapper
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(Float.class));
        assertNull(Primitives.primitiveWrapperOf(float.class));

        // Test double wrapper
        assertEquals(Double.valueOf(0D), Primitives.primitiveWrapperOf(Double.class));
        assertNull(Primitives.primitiveWrapperOf(double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test boolean primitive
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertNull(Primitives.primitiveValueOrNullFor(Boolean.class));

        // Test char primitive
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertNull(Primitives.primitiveValueOrNullFor(Character.class));

        // Test byte primitive
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertNull(Primitives.primitiveValueOrNullFor(Byte.class));

        // Test short primitive
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertNull(Primitives.primitiveValueOrNullFor(Short.class));

        // Test int primitive
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertNull(Primitives.primitiveValueOrNullFor(Integer.class));

        // Test long primitive
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertNull(Primitives.primitiveValueOrNullFor(Long.class));

        // Test float primitive
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertNull(Primitives.primitiveValueOrNullFor(Float.class));

        // Test double primitive
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
        assertNull(Primitives.primitiveValueOrNullFor(Double.class));
    }
}