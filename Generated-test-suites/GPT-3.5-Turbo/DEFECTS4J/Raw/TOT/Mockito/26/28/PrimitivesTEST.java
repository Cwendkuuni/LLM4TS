import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        
        // Edge case
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));

        // Error scenario
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        
        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(PrimitivesTest.class));
        
        // Error scenario
        assertFalse(Primitives.isPrimitiveWrapper(null));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        
        // Edge case
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(float.class));
        
        // Error scenario
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        
        // Edge case
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class).longValue());
        
        // Error scenario
        assertNull(Primitives.primitiveValueOrNullFor(PrimitivesTest.class));
    }
}