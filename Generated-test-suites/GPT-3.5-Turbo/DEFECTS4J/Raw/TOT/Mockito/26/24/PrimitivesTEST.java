import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
    
    @Test
    public void primitiveTypeOf_PrimitiveClass_ReturnsSameClass() {
        Class<Integer> clazz = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(clazz, result);
    }
    
    @Test
    public void primitiveTypeOf_WrapperClass_ReturnsPrimitiveClass() {
        Class<Integer> clazz = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(expected, result);
    }
    
    @Test
    public void primitiveTypeOf_UnsupportedClass_ReturnsNull() {
        Class<String> clazz = String.class;
        Class<String> result = Primitives.primitiveTypeOf(clazz);
        assertNull(result);
    }
    
    @Test
    public void isPrimitiveWrapper_PrimitiveWrapperClass_ReturnsTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);
    }
    
    @Test
    public void isPrimitiveWrapper_PrimitiveClass_ReturnsFalse() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.TYPE);
        assertFalse(result);
    }
    
    @Test
    public void isPrimitiveWrapper_UnsupportedClass_ReturnsFalse() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result);
    }
    
    @Test
    public void primitiveWrapperOf_PrimitiveWrapperClass_ReturnsWrapperObject() {
        Integer expectedResult = 0;
        Integer result = Primitives.primitiveWrapperOf(Integer.class);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void primitiveWrapperOf_PrimitiveClass_ReturnsNull() {
        Integer result = Primitives.primitiveWrapperOf(Integer.TYPE);
        assertNull(result);
    }
    
    @Test
    public void primitiveValueOrNullFor_PrimitiveClass_ReturnsPrimitiveValue() {
        int expectedResult = 0;
        Integer result = Primitives.primitiveValueOrNullFor(Integer.TYPE);
        assertEquals(expectedResult, result.intValue());
    }
    
    @Test
    public void primitiveValueOrNullFor_PrimitiveWrapperClass_ReturnsNull() {
        Integer result = Primitives.primitiveValueOrNullFor(Integer.class);
        assertNull(result);
    }
    
    @Test
    public void primitiveValueOrNullFor_UnsupportedClass_ReturnsNull() {
        String result = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(result);
    }
}