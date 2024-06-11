
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void primitiveTypeOf_shouldReturnPrimitiveTypeIfClassIsPrimitive() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(byte.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(char.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(int.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(double.class));
    }

    @Test
    public void primitiveTypeOf_shouldReturnWrapperTypeIfClassIsWrapperType() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.class));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(Short.class));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(Long.class));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(Float.class));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(Double.class));
    }

    @Test
    public void isPrimitiveWrapper_shouldReturnTrueIfTypeIsWrapperType() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
    }

    @Test
    public void isPrimitiveWrapper_shouldReturnFalseIfTypeIsNotWrapperType() {
        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void primitiveWrapperOf_shouldReturnWrapperInstanceIfTypeIsWrapperType() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(Byte.class));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(Character.class));
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(Short.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(Long.valueOf(0L), Primitives.primitiveWrapperOf(Long.class));
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(Float.class));
        assertEquals(Double.valueOf(0D), Primitives.primitiveWrapperOf(Double.class));
    }

    @Test
    public void primitiveWrapperOf_shouldReturnNullIfTypeIsNotWrapperType() {
        assertNull(Primitives.primitiveWrapperOf(boolean.class));
        assertNull(Primitives.primitiveWrapperOf(byte.class));
        assertNull(Primitives.primitiveWrapperOf(char.class));
        assertNull(Primitives.primitiveWrapperOf(short.class));
        assertNull(Primitives.primitiveWrapperOf(int.class));
        assertNull(Primitives.primitiveWrapperOf(long.class));
        assertNull(Primitives.primitiveWrapperOf(float.class));
        assertNull(Primitives.primitiveWrapperOf(double.class));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void primitiveValueOrNullFor_shouldReturnPrimitiveValueIfTypeIsPrimitiveType() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.0);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0.0);
    }

    @Test
    public void primitiveValueOrNullFor_shouldReturnNullIfTypeIsNotPrimitiveType() {
        assertNull(Primitives.primitiveValueOrNullFor(Boolean.class));
        assertNull(Primitives.primitiveValueOrNullFor(Byte.class));
        assertNull(Primitives.primitiveValueOrNullFor(Character.class));
        assertNull(Primitives.primitiveValueOrNullFor(Short.class));
        assertNull(Primitives.primitiveValueOrNullFor(Integer.class));
        assertNull(Primitives.primitiveValueOrNullFor(Long.class));
        assertNull(Primitives.primitiveValueOrNullFor(Float.class));
        assertNull(Primitives.primitiveValueOrNullFor(Double.class));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}

