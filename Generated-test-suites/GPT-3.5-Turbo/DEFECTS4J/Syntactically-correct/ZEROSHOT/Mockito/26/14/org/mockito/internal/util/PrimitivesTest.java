import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveType_ReturnsSameType() {
        Class<Integer> clazz = int.class;
        
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        
        Assert.assertSame(clazz, result);
    }
    
    @Test
    public void testPrimitiveTypeOf_WrapperType_ReturnsPrimitiveType() {
        Class<Integer> clazz = Integer.class;
        
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        
        Assert.assertEquals(int.class, result);
    }
    
    @Test
    public void testIsPrimitiveWrapper_PrimitiveType_ReturnsFalse() {
        Class<Integer> clazz = int.class;
        
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsPrimitiveWrapper_WrapperType_ReturnsTrue() {
        Class<Integer> clazz = Integer.class;
        
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testPrimitiveWrapperOf_PrimitiveType_ReturnsWrapperObject() {
        Class<Integer> clazz = int.class;
        
        Integer result = Primitives.primitiveWrapperOf(clazz);
        
        Assert.assertEquals(Integer.valueOf(0), result);
    }
    
    @Test
    public void testPrimitiveWrapperOf_WrapperType_ReturnsSameObject() {
        Class<Integer> clazz = Integer.class;
        
        Integer wrapperObject = new Integer(0);
        
        Integer result = Primitives.primitiveWrapperOf(clazz);
        
        Assert.assertSame(wrapperObject, result);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsDefaultValue() {
        Class<Boolean> clazz = boolean.class;
        
        boolean result = Primitives.primitiveValueOrNullFor(clazz);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_WrapperType_ReturnsNull() {
        Class<Boolean> clazz = Boolean.class;
        
        Boolean result = Primitives.primitiveValueOrNullFor(clazz);
        
        Assert.assertNull(result);
    }
    
}