
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        // Add more test cases here for all supported types
    }
    
    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        // Add more test cases here for all supported types
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(char.class));
        // Add more test cases here for all supported types
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
        // Add more test cases here for all supported types
    }
}
