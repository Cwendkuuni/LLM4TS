import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType_shouldReturnSameType() {
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
    public void testPrimitiveTypeOf_wrapperType_shouldReturnPrimitiveType() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveType_shouldReturnFalse() {
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
    public void testIsPrimitiveWrapper_wrapperType_shouldReturnTrue() {
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
    public void testPrimitiveWrapperOf_primitiveType_shouldReturnWrapper() {
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
    public void testPrimitiveValueOrNullFor_primitiveType_shouldReturnValueOrNull() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
    }
}


public class PrimitivesTest {
    
    // testPrimitiveTypeOf tests
    
    @Test
    public void testPrimitiveTypeOf_primitiveType_shouldReturnSameType() {
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
    public void testPrimitiveTypeOf_wrapperType_shouldReturnPrimitiveType() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
    }

    // testIsPrimitiveWrapper tests
    
    @Test
    public void testIsPrimitiveWrapper_primitiveType_shouldReturnFalse() {
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
    public void testIsPrimitiveWrapper_wrapperType_shouldReturnTrue() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
    }

    // testPrimitiveWrapperOf tests
    
    @Test
    public void testPrimitiveWrapperOf_primitiveType_shouldReturnWrapper() {
        assertEquals(Boolean.class, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Byte.class, Primitives.primitiveWrapperOf(byte.class));
        assertEquals(Character.class, Primitives.primitiveWrapperOf(char.class));
        assertEquals(Short.class, Primitives.primitiveWrapperOf(short.class));
        assertEquals(Integer.class, Primitives.primitiveWrapperOf(int.class));
        assertEquals(Long.class, Primitives.primitiveWrapperOf(long.class));
        assertEquals(Float.class, Primitives.primitiveWrapperOf(float.class));
        assertEquals(Double.class, Primitives.primitiveWrapperOf(double.class));
    }

    // testPrimitiveValueOrNullFor tests
    
    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_shouldReturnValueOrNull() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
    }
}
