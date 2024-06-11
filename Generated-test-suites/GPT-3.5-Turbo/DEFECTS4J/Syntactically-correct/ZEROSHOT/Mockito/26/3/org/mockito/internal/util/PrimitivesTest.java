
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

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
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test with primitive types
        assertEquals(Boolean.class, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Character.class, Primitives.primitiveWrapperOf(char.class));
        assertEquals(Byte.class, Primitives.primitiveWrapperOf(byte.class));
        assertEquals(Short.class, Primitives.primitiveWrapperOf(short.class));
        assertEquals(Integer.class, Primitives.primitiveWrapperOf(int.class));
        assertEquals(Long.class, Primitives.primitiveWrapperOf(long.class));
        assertEquals(Float.class, Primitives.primitiveWrapperOf(float.class));
        assertEquals(Double.class, Primitives.primitiveWrapperOf(double.class));

        // Test with wrapper types
        assertEquals(Boolean.class, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals(Character.class, Primitives.primitiveWrapperOf(Character.class));
        assertEquals(Byte.class, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals(Short.class, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(Integer.class, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(Long.class, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(Float.class, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(Double.class, Primitives.primitiveWrapperOf(Double.class));
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
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0);

        // Test with wrapper types
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.class), 0);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.class), 0);
    }
}

