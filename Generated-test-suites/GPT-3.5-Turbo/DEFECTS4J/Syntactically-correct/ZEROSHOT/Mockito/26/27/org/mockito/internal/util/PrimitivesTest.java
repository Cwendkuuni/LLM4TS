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
        assertEquals(null, Primitives.primitiveTypeOf(String.class));
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
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.TYPE));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.TYPE));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.TYPE));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.TYPE));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.TYPE));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.TYPE));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.TYPE));
        assertEquals(null, Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.TYPE));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.TYPE));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.TYPE));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.TYPE));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.TYPE));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.TYPE));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.TYPE));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.TYPE));
        assertEquals(null, Primitives.primitiveValueOrNullFor(String.class));
    }
}