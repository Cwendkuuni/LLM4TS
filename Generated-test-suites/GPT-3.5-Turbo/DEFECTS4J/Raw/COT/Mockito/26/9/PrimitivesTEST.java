import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf_primitiveType() {
        Class<Integer> clazz = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(clazz, result);
    }
    
    @Test
    public void testPrimitiveTypeOf_wrapperType() {
        Class<Integer> clazz = Integer.class;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(Integer.TYPE, result);
    }
    
    @Test
    public void testIsPrimitiveWrapper_true() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);
    }
    
    @Test
    public void testIsPrimitiveWrapper_false() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result);
    }
    
    @Test
    public void testPrimitiveWrapperOf_primitiveType() {
        Class<Integer> type = Integer.TYPE;
        Integer result = Primitives.primitiveWrapperOf(type);
        assertEquals(Integer.valueOf(0), result);
    }
    
    @Test
    public void testPrimitiveWrapperOf_wrapperType() {
        Class<Integer> type = Integer.class;
        Integer result = Primitives.primitiveWrapperOf(type);
        assertEquals(Integer.valueOf(0), result);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_primitiveType() {
        Class<Integer> primitiveType = Integer.TYPE;
        Integer result = Primitives.primitiveValueOrNullFor(primitiveType);
        assertEquals(Integer.valueOf(0), result);
    }
    
    @Test
    public void testWrapperReturnValues_containsKey() {
        boolean result = Primitives.wrapperReturnValues.containsKey(Integer.class);
        assertTrue(result);
    }
    
    @Test
    public void testWrapperReturnValues_notContainsKey() {
        boolean result = Primitives.wrapperReturnValues.containsKey(String.class);
        assertFalse(result);
    }
    
    @Test
    public void testPrimitiveValues_containsKey() {
        boolean result = Primitives.primitiveValues.containsKey(boolean.class);
        assertTrue(result);
    }
    
    @Test
    public void testPrimitiveValues_notContainsKey() {
        boolean result = Primitives.primitiveValues.containsKey(String.class);
        assertFalse(result);
    }
}

