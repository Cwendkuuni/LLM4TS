import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf_primitiveType_shouldReturnSameClass() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, booleanClass);
    }
    
    @Test
    public void testPrimitiveTypeOf_wrapperType_shouldReturnPrimitiveType() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, booleanClass);
    }
    
    @Test
    public void testIsPrimitiveWrapper_primitiveType_shouldReturnFalse() {
        boolean result = Primitives.isPrimitiveWrapper(boolean.class);
        assertFalse(result);
    }
    
    @Test
    public void testIsPrimitiveWrapper_wrapperType_shouldReturnTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(result);
    }
    
    @Test
    public void testPrimitiveWrapperOf_primitiveType_shouldReturnWrapperObject() {
        Boolean wrapperBoolean = Primitives.primitiveWrapperOf(boolean.class);
        assertNotNull(wrapperBoolean);
    }
    
    @Test
    public void testPrimitiveWrapperOf_wrapperType_shouldReturnNull() {
        Boolean wrapperBoolean = Primitives.primitiveWrapperOf(Boolean.class);
        assertNull(wrapperBoolean);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_shouldReturnValue() {
        Boolean value = Primitives.primitiveValueOrNullFor(boolean.class);
        assertNotNull(value);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_nonPrimitiveType_shouldReturnNull() {
        Boolean value = Primitives.primitiveValueOrNullFor(Boolean.class);
        assertNull(value);
    }
}