import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        
        // Edge case
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        
        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.TYPE));
        
        // Error scenario
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        
        // Edge case
        assertEquals(null, Primitives.primitiveValueOrNullFor(String.class));
    }

}