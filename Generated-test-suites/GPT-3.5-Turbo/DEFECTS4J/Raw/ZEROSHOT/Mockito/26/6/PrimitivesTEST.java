import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> result1 = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, result1);

        Class<Character> result2 = Primitives.primitiveTypeOf(Character.class);
        assertEquals(char.class, result2);

        Class<Byte> result3 = Primitives.primitiveTypeOf(Byte.class);
        assertEquals(byte.class, result3);

        Class<Short> result4 = Primitives.primitiveTypeOf(Short.class);
        assertEquals(short.class, result4);

        Class<Integer> result5 = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, result5);

        Class<Long> result6 = Primitives.primitiveTypeOf(Long.class);
        assertEquals(long.class, result6);

        Class<Float> result7 = Primitives.primitiveTypeOf(Float.class);
        assertEquals(float.class, result7);

        Class<Double> result8 = Primitives.primitiveTypeOf(Double.class);
        assertEquals(double.class, result8);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean result1 = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(result1);

        boolean result2 = Primitives.isPrimitiveWrapper(Character.class);
        assertTrue(result2);

        boolean result3 = Primitives.isPrimitiveWrapper(Byte.class);
        assertTrue(result3);

        boolean result4 = Primitives.isPrimitiveWrapper(Short.class);
        assertTrue(result4);

        boolean result5 = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result5);

        boolean result6 = Primitives.isPrimitiveWrapper(Long.class);
        assertTrue(result6);

        boolean result7 = Primitives.isPrimitiveWrapper(Float.class);
        assertTrue(result7);

        boolean result8 = Primitives.isPrimitiveWrapper(Double.class);
        assertTrue(result8);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean result1 = Primitives.primitiveWrapperOf(boolean.class);
        assertNotNull(result1);

        Character result2 = Primitives.primitiveWrapperOf(char.class);
        assertNotNull(result2);

        Byte result3 = Primitives.primitiveWrapperOf(byte.class);
        assertNotNull(result3);

        Short result4 = Primitives.primitiveWrapperOf(short.class);
        assertNotNull(result4);

        Integer result5 = Primitives.primitiveWrapperOf(int.class);
        assertNotNull(result5);

        Long result6 = Primitives.primitiveWrapperOf(long.class);
        assertNotNull(result6);

        Float result7 = Primitives.primitiveWrapperOf(float.class);
        assertNotNull(result7);

        Double result8 = Primitives.primitiveWrapperOf(double.class);
        assertNotNull(result8);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean result1 = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, result1);

        Character result2 = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', result2);

        Byte result3 = Primitives.primitiveValueOrNullFor(byte.class);
        assertEquals((byte) 0, result3);

        Short result4 = Primitives.primitiveValueOrNullFor(short.class);
        assertEquals((short) 0, result4);

        Integer result5 = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, result5);

        Long result6 = Primitives.primitiveValueOrNullFor(long.class);
        assertEquals(0L, result6);

        Float result7 = Primitives.primitiveValueOrNullFor(float.class);
        assertEquals(0F, result7, 0);

        Double result8 = Primitives.primitiveValueOrNullFor(double.class);
        assertEquals(0, result8, 0);
    }

}