import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompareEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReflectionCompareNullObject() {
        Object obj1 = null;
        Object obj2 = new Object();
        
        try {
            int result = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testReflectionCompareDifferentObjects() {
        Object obj1 = new Integer(10);
        Object obj2 = new Double(10.0);
        
        try {
            int result = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("ClassCastException should be thrown.");
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);
        
        int result = builder.toComparison();
        
        assertEquals(10, result);
    }
    
    @Test
    public void testAppendEqualObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        
        builder.append(10, 10);
        int result1 = builder.toComparison();
        
        builder.append("abc", "abc");
        int result2 = builder.toComparison();
        
        assertTrue(result1 == 0 && result2 == 0);
    }
    
    @Test
    public void testAppendNullObject() {
        CompareToBuilder builder = new CompareToBuilder();
        
        builder.append(null, "abc");
        int result1 = builder.toComparison();
        
        builder.append("abc", null);
        int result2 = builder.toComparison();
        
        assertTrue(result1 == -1 && result2 == 1);
    }
    
    @Test
    public void testAppendArrayWithDifferentLengths() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3, 4};
        
        builder.append(array1, array2);
        int result = builder.toComparison();
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testAppendArrayWithDifferentElements() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        
        builder.append(array1, array2);
        int result = builder.toComparison();
        
        assertEquals(-1, result);
    }

}