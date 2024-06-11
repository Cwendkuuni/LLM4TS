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
        assertEquals(Boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(Character.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(Byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(Short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(Integer.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(Long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(Float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(Double.class, Primitives.primitiveTypeOf(Double.class));
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
        assertEquals(Double.valueOf(0), Primitives.primitiveWrapperOf(double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.01);
        assertEquals(0, Primitives.primitiveValueOrNullFor(double.class), 0.01);
    }
}