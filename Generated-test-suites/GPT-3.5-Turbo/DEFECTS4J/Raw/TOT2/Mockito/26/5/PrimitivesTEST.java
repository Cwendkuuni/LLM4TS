import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Primitives.primitiveTypeOf(int.class);
        assertEquals(Integer.TYPE, intClass);
    }
    
    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        Integer value = Primitives.primitiveWrapperOf(int.class);
        assertEquals(0, value.intValue());
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        Integer value = Primitives.primitiveValueOrNullFor(Integer.class);
        assertEquals(0, value.intValue());
    }
    
    // Edge Cases and Error Scenarios
    
    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOfNull() {
        Primitives.primitiveTypeOf(null);
    }
    
    @Test
    public void testIsPrimitiveWrapperNonPrimitive() {
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOfNull() {
        assertNull(Primitives.primitiveWrapperOf(null));
    }

    @Test
    public void testPrimitiveValueOrNullForNull() {
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }

}