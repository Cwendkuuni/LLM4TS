import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(Integer.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
    }

    @Test
    public void testEdgeCases() {
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
    }

    @Test
    public void testExceptions() {
        try {
            Primitives.primitiveTypeOf(String.class);
            fail("Expected ClassCastException not thrown");
        } catch(ClassCastException e) {
            // Expected exception
        }
    }
}