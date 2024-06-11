
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

        // Test with wrapper classes
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
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(new Character('\u0000'), Primitives.primitiveWrapperOf(char.class));
        assertEquals(new Byte((byte) 0), Primitives.primitiveWrapperOf(byte.class));
        assertEquals(new Short((short) 0), Primitives.primitiveWrapperOf(short.class));
        assertEquals(new Integer(0), Primitives.primitiveWrapperOf(int.class));
        assertEquals(new Long(0), Primitives.primitiveWrapperOf(long.class));
        assertEquals(new Float(0), Primitives.primitiveWrapperOf(float.class));
        assertEquals(new Double(0), Primitives.primitiveWrapperOf(double.class));
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

    @Test
    public void testWrapperReturnValues() {
        assertTrue(Primitives.wrapperReturnValues.containsKey(Boolean.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Character.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Byte.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Short.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Integer.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Long.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Float.class));
        assertTrue(Primitives.wrapperReturnValues.containsKey(Double.class));
        assertFalse(Primitives.wrapperReturnValues.containsKey(String.class));
        assertFalse(Primitives.wrapperReturnValues.containsKey(Object.class));
    }
}