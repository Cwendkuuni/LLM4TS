import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test with primitive types
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(char.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(int.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(double.class));

        // Test with wrapper types
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
        
        // Test with other classes
        assertNull(Primitives.primitiveTypeOf(String.class));
        assertNull(Primitives.primitiveTypeOf(Object.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test with wrapper types
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));

        // Test with primitive types
        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));

        // Test with other classes
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test with wrapper types
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));

        // Test with other classes
        assertNull(Primitives.primitiveWrapperOf(String.class));
        assertNull(Primitives.primitiveWrapperOf(Object.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test with primitive types
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));

        // Test with other classes
        assertNull(Primitives.primitiveValueOrNullFor(Boolean.class));
        assertNull(Primitives.primitiveValueOrNullFor(Character.class));
        assertNull(Primitives.primitiveValueOrNullFor(Byte.class));
        assertNull(Primitives.primitiveValueOrNullFor(Short.class));
        assertNull(Primitives.primitiveValueOrNullFor(Integer.class));
        assertNull(Primitives.primitiveValueOrNullFor(Long.class));
        assertNull(Primitives.primitiveValueOrNullFor(Float.class));
        assertNull(Primitives.primitiveValueOrNullFor(Double.class));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
        assertNull(Primitives.primitiveValueOrNullFor(Object.class));
    }
}