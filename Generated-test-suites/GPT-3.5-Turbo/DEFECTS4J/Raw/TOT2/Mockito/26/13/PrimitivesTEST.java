import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, booleanClass);
        
        Class<Integer> integerClass = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, integerClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        boolean booleanValue = Primitives.primitiveWrapperOf(Boolean.class);
        assertEquals(false, booleanValue);
        
        int intValue = Primitives.primitiveWrapperOf(Integer.class);
        assertEquals(0, intValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, booleanValue);
        
        Integer integerValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, integerValue.intValue());
    }
    
    @Test
    public void testExceptionHandling() {
        try {
            Primitives.primitiveWrapperOf(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
}