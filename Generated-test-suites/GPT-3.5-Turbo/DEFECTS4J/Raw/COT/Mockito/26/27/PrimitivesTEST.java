
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_Primitive_ShouldReturnSameClass() {
        Class<Integer> expected = int.class;
        Class<Integer> actual = Primitives.primitiveTypeOf(int.class);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testPrimitiveTypeOf_WrapperClass_ShouldReturnPrimitiveClass() {
        Class<Integer> expected = int.class;
        Class<Integer> actual = Primitives.primitiveTypeOf(Integer.class);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass_ShouldReturnTrue() {
        boolean actual = Primitives.isPrimitiveWrapper(Integer.class);
        
        Assert.assertTrue(actual);
    }
    
    @Test
    public void testIsPrimitiveWrapper_NonWrapperClass_ShouldReturnFalse() {
        boolean actual = Primitives.isPrimitiveWrapper(String.class);
        
        Assert.assertFalse(actual);
    }
    
    @Test
    public void testPrimitiveWrapperOf_PrimitiveClass_ShouldReturnWrapperClass() {
        Integer expected = Integer.valueOf(0);
        Integer actual = Primitives.primitiveWrapperOf(int.class);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testPrimitiveWrapperOf_WrapperClass_ShouldReturnSameClass() {
        Integer expected = 0;
        Integer actual = Primitives.primitiveWrapperOf(Integer.class);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveClass_ShouldReturnDefaultPrimitiveValue() {
        int expected = 0;
        int actual = Primitives.primitiveValueOrNullFor(int.class);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveClass_ShouldReturnNull() {
        String actual = Primitives.primitiveValueOrNullFor(String.class);
        
        Assert.assertNull(actual);
    }
}
