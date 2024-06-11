import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(byte.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(char.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(int.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(double.class));
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
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.class, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Byte.class, Primitives.primitiveWrapperOf(byte.class));
        assertEquals(Character.class, Primitives.primitiveWrapperOf(char.class));
        assertEquals(Short.class, Primitives.primitiveWrapperOf(short.class));
        assertEquals(Integer.class, Primitives.primitiveWrapperOf(int.class));
        assertEquals(Long.class, Primitives.primitiveWrapperOf(long.class));
        assertEquals(Float.class, Primitives.primitiveWrapperOf(float.class));
        assertEquals(Double.class, Primitives.primitiveWrapperOf(double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.0);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0.0);
    }
}