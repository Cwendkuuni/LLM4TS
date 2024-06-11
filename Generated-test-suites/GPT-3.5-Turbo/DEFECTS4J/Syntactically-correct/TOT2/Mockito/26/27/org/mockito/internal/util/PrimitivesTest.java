import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals((Character) '\u0000', Primitives.primitiveWrapperOf(char.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.TYPE));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.TYPE));
    }

    // Additional test cases for edge cases and exceptions can be added here

}