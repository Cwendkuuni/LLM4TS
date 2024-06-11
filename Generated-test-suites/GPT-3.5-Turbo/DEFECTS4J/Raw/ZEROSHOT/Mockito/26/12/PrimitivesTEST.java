import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(char.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(byte.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(short.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(int.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(long.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(float.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(double.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
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
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(char.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(byte.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(short.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(int.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(long.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(float.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(double.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
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
    }
}