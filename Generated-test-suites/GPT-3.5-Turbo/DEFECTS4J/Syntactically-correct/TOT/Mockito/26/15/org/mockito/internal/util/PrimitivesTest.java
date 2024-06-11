import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.TYPE));
        
        // Edge case
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.TYPE));
        
        // Error scenario
        assertNull(Primitives.primitiveTypeOf(String.class));
    }
    
    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        
        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        
        // Error scenario
        assertFalse(Primitives.isPrimitiveWrapper(null));
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        
        // Edge case
        assertNull(Primitives.primitiveWrapperOf(String.class));
        
        // Error scenario
        assertNull(Primitives.primitiveWrapperOf(null));
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
        
        // Edge case
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
        
        // Error scenario
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
    
}