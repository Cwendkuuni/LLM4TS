import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(0, Primitives.primitiveWrapperOf(int.class));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(Boolean.FALSE, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }

    // Edge cases and exception handling scenarios can be added here

}