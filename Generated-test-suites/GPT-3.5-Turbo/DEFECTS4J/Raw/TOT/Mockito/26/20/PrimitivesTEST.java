import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveType_ReturnsSameClass() {
        Class<Integer> clazz = int.class;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        
        assertSame(clazz, result);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperType_ReturnsPrimitiveClass() {
        Class<Integer> clazz = Integer.class;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        
        assertSame(int.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapper_ReturnsTrue() {
        Class<Integer> clazz = Integer.class;
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        
        assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveWrapper_ReturnsFalse() {
        Class<String> clazz = String.class;
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveType_ReturnsWrapperInstance() {
        Class<Integer> clazz = int.class;
        Integer result = Primitives.primitiveWrapperOf(clazz);
        
        assertEquals(Integer.valueOf(0), result);
    }

    @Test
    public void testPrimitiveWrapperOf_NonPrimitiveType_ReturnsNull() {
        Class<String> clazz = String.class;
        String result = Primitives.primitiveWrapperOf(clazz);
        
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsPrimitiveValue() {
        Class<Integer> clazz = int.class;
        int result = Primitives.primitiveValueOrNullFor(clazz);
        
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveType_ReturnsNull() {
        Class<String> clazz = String.class;
        String result = Primitives.primitiveValueOrNullFor(clazz);
        
        assertNull(result);
    }
}