import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        // Edge case
        assertEquals(void.class, Primitives.primitiveTypeOf(Void.class));
        // Error scenario
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        // Edge case
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        // Edge case
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }

}