import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> result1 = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(Boolean.TYPE, result1);

        Class<Character> result2 = Primitives.primitiveTypeOf(Character.class);
        assertEquals(Character.TYPE, result2);

        Class<Byte> result3 = Primitives.primitiveTypeOf(Byte.class);
        assertEquals(Byte.TYPE, result3);

        Class<Short> result4 = Primitives.primitiveTypeOf(Short.class);
        assertEquals(Short.TYPE, result4);

        Class<Integer> result5 = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(Integer.TYPE, result5);

        Class<Long> result6 = Primitives.primitiveTypeOf(Long.class);
        assertEquals(Long.TYPE, result6);

        Class<Float> result7 = Primitives.primitiveTypeOf(Float.class);
        assertEquals(Float.TYPE, result7);

        Class<Double> result8 = Primitives.primitiveTypeOf(Double.class);
        assertEquals(Double.TYPE, result8);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean result1 = Primitives.isPrimitiveWrapper(Boolean.class);
        assertEquals(true, result1);

        boolean result2 = Primitives.isPrimitiveWrapper(Character.class);
        assertEquals(true, result2);

        boolean result3 = Primitives.isPrimitiveWrapper(Byte.class);
        assertEquals(true, result3);

        boolean result4 = Primitives.isPrimitiveWrapper(Short.class);
        assertEquals(true, result4);

        boolean result5 = Primitives.isPrimitiveWrapper(Integer.class);
        assertEquals(true, result5);

        boolean result6 = Primitives.isPrimitiveWrapper(Long.class);
        assertEquals(true, result6);

        boolean result7 = Primitives.isPrimitiveWrapper(Float.class);
        assertEquals(true, result7);

        boolean result8 = Primitives.isPrimitiveWrapper(Double.class);
        assertEquals(true, result8);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean result1 = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(false, result1);

        Character result2 = Primitives.primitiveWrapperOf(char.class);
        assertEquals('\u0000', result2);

        Byte result3 = Primitives.primitiveWrapperOf(byte.class);
        assertEquals((byte) 0, result3);

        Short result4 = Primitives.primitiveWrapperOf(short.class);
        assertEquals((short) 0, result4);

        Integer result5 = Primitives.primitiveWrapperOf(int.class);
        assertEquals(0, result5);

        Long result6 = Primitives.primitiveWrapperOf(long.class);
        assertEquals(0L, result6);

        Float result7 = Primitives.primitiveWrapperOf(float.class);
        assertEquals(0F, result7, 0.0);

        Double result8 = Primitives.primitiveWrapperOf(double.class);
        assertEquals(0D, result8, 0.0);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        boolean result1 = Primitives.primitiveValueOrNullFor(Boolean.TYPE);
        assertEquals(false, result1);

        char result2 = Primitives.primitiveValueOrNullFor(Character.TYPE);
        assertEquals('\u0000', result2);

        byte result3 = Primitives.primitiveValueOrNullFor(Byte.TYPE);
        assertEquals((byte) 0, result3);

        short result4 = Primitives.primitiveValueOrNullFor(Short.TYPE);
        assertEquals((short) 0, result4);

        int result5 = Primitives.primitiveValueOrNullFor(Integer.TYPE);
        assertEquals(0, result5);

        long result6 = Primitives.primitiveValueOrNullFor(Long.TYPE);
        assertEquals(0L, result6);

        float result7 = Primitives.primitiveValueOrNullFor(Float.TYPE);
        assertEquals(0F, result7, 0.0);

        double result8 = Primitives.primitiveValueOrNullFor(Double.TYPE);
        assertEquals(0D, result8, 0.0);
    }
}