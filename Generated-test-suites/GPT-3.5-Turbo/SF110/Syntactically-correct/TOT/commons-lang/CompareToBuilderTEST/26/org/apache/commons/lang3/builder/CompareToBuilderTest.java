import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_sameObject_returnsZero() {
        Object obj = new Object();
        int result = CompareToBuilder.reflectionCompare(obj, obj);
        assertEquals(0, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_nullObject_returnsNullPointer() {
        CompareToBuilder.reflectionCompare(null, null);
    }
    
    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_differentClasses_returnsClassCastException() {
        Object obj1 = new Object();
        Object obj2 = new ArrayList<>();
        CompareToBuilder.reflectionCompare(obj1, obj2);
    }
    
    @Test
    public void testReflectionCompare_compareTransientsIsTrue_returnsComparison() {
        Object obj1 = new TestObject(2);
        Object obj2 = new TestObject(3);
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, true);
        assertEquals(-1, result);
    }
    
    @Test
    public void testReflectionCompare_compareTransientsIsFalse_returnsComparison() {
        Object obj1 = new TestObject(2);
        Object obj2 = new TestObject(3);
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, false);
        assertEquals(1, result);
    }
    
    @Test
    public void testReflectionCompare_excludeFields_returnsComparison() {
        Object obj1 = new TestObject(2);
        Object obj2 = new TestObject(3);
        List<String> excludeFields = new ArrayList<>();
        excludeFields.add("id");
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, excludeFields);
        assertEquals(0, result);
    }
    
    @Test
    public void testReflectionCompare_excludeFieldsArray_returnsComparison() {
        Object obj1 = new TestObject(2);
        Object obj2 = new TestObject(3);
        String[] excludeFields = {"id"};
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, excludeFields);
        assertEquals(0, result);
    }
    
    // Other test methods for appendSuper, append, append with different types of arguments

    private class TestObject {
        private int id;
        
        public TestObject(int id) {
            this.id = id;
        }
    }
}