import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(Character.class, Primitives.primitiveTypeOf(char.class));
        // Add more test cases covering all primitive types
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        // Add more test cases for different types
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.TYPE));
        // Add more test cases for other primitive types
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        // Add more test cases for different types
    }

    // Add more test cases for edge cases and error scenarios if needed

}