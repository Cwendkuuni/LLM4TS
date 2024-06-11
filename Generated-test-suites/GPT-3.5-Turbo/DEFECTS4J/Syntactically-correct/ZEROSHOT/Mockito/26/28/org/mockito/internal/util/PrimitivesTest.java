import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, result);

        Class<Double> result2 = Primitives.primitiveTypeOf(Double.class);
        assertEquals(double.class, result2);
        // Add more test cases for other wrapper classes here
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        // Add more test cases for other wrapper classes here
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.class, result);

        Double result2 = Primitives.primitiveWrapperOf(double.class);
        assertEquals(Double.class, result2);
        // Add more test cases for other primitive types here
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, result);

        Character result2 = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', result2);
        // Add more test cases for other primitive types here
    }
}