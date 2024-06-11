
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> result1 = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result1);

        Class<Integer> result2 = Primitives.primitiveTypeOf(Integer.class);
        assertNull(result2);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean result1 = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result1);

        boolean result2 = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(result2);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean result1 = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.class, result1);

        Boolean result2 = Primitives.primitiveWrapperOf(int.class);
        assertNull(result2);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean result1 = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, result1);

        Boolean result2 = Primitives.primitiveValueOrNullFor(Integer.class);
        assertNull(result2);
    }
}
