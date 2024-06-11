import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        
        int result = builder.toComparison();
        
        assertEquals(5, result);
    }
    
    @Test
    public void testAppendIntArray() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        
        builder.append(arr1, arr2);
        
        int result = builder.toComparison();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAppendNullObject() {
        CompareToBuilder builder = new CompareToBuilder();
        
        builder.append(null, new Object());
        
        int result = builder.toComparison();
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testExceptionHandling() {
        CompareToBuilder builder = new CompareToBuilder();
        
        try {
            builder.append(new Object(), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}