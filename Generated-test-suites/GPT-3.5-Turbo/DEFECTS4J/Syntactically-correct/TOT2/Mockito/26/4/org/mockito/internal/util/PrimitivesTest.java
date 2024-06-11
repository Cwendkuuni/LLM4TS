import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        // Add more test cases for other wrapper classes
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        // Add more test cases for other types
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        // Add more test cases for other primitive types
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        // Add more test cases for other primitive types
    }

    // Add more test cases for edge cases and exception handling scenarios

}