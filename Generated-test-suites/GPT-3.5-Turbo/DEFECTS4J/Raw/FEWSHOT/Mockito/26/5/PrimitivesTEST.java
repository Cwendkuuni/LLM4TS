import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        assertEquals(null, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(null, Primitives.primitiveTypeOf(Character.class));
        assertEquals(null, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(null, Primitives.primitiveTypeOf(Short.class));
        assertEquals(null, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(null, Primitives.primitiveTypeOf(Long.class));
        assertEquals(null, Primitives.primitiveTypeOf(Float.class));
        assertEquals(null, Primitives.primitiveTypeOf(Double.class));
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
        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(char.class));
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(byte.class));
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(short.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        assertEquals(Long.valueOf(0L), Primitives.primitiveWrapperOf(long.class));
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(float.class));
        assertEquals(Double.valueOf(0D), Primitives.primitiveWrapperOf(double.class));
        assertNull(Primitives.primitiveWrapperOf(Boolean.class));
        assertNull(Primitives.primitiveWrapperOf(Character.class));
        assertNull(Primitives.primitiveWrapperOf(Byte.class));
        assertNull(Primitives.primitiveWrapperOf(Short.class));
        assertNull(Primitives.primitiveWrapperOf(Integer.class));
        assertNull(Primitives.primitiveWrapperOf(Long.class));
        assertNull(Primitives.primitiveWrapperOf(Float.class));
        assertNull(Primitives.primitiveWrapperOf(Double.class));
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
        assertNull(Primitives.primitiveValueOrNullFor(Boolean.class));
        assertNull(Primitives.primitiveValueOrNullFor(Character.class));
        assertNull(Primitives.primitiveValueOrNullFor(Byte.class));
        assertNull(Primitives.primitiveValueOrNullFor(Short.class));
        assertNull(Primitives.primitiveValueOrNullFor(Integer.class));
        assertNull(Primitives.primitiveValueOrNullFor(Long.class));
        assertNull(Primitives.primitiveValueOrNullFor(Float.class));
        assertNull(Primitives.primitiveValueOrNullFor(Double.class));
    }
}