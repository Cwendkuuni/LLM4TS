import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.TYPE));
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.TYPE));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.TYPE));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.TYPE));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.TYPE));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.TYPE));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.TYPE));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.TYPE));
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
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(Character.TYPE));
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(Byte.TYPE));
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(Short.TYPE));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(Integer.TYPE));
        assertEquals(Long.valueOf(0), Primitives.primitiveWrapperOf(Long.TYPE));
        assertEquals(Float.valueOf(0), Primitives.primitiveWrapperOf(Float.TYPE));
        assertEquals(Double.valueOf(0), Primitives.primitiveWrapperOf(Double.TYPE));
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
        assertEquals(0, Primitives.primitiveValueOrNullFor(Double.TYPE));
    }
}