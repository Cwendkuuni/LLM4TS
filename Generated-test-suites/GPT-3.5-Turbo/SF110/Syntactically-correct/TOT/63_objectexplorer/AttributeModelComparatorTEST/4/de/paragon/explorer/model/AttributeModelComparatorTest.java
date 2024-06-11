import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;

public class AttributeModelComparatorTest {

    @Test
    public void compare_NullObjects_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertTrue(result < 0);
    }
    
    @Test
    public void compare_FirstObjectNull_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object2 = new AttributeModel("example", "value");
        
        int result = comparator.compare(null, object2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void compare_SecondObjectNull_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("example", "value");
        
        int result = comparator.compare(object1, null);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void compare_BothObjectsNull_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(null, null);
        
        assertEquals(0, result);
    }
    
    @Test
    public void compare_EqualNames_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("example", "value");
        AttributeModel object2 = new AttributeModel("example", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void compare_FirstObjectBeforeA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("apple", "value");
        AttributeModel object2 = new AttributeModel("banana", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void compare_SecondObjectBeforeA_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("banana", "value");
        AttributeModel object2 = new AttributeModel("apple", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void compare_FirstObjectBeforeAButSecondObjectEqualToA_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("apple", "value");
        AttributeModel object2 = new AttributeModel("aardvark", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void compare_SecondObjectBeforeAButFirstObjectEqualToA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("aardvark", "value");
        AttributeModel object2 = new AttributeModel("apple", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void compare_BothObjectsEqualToA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("a", "value1");
        AttributeModel object2 = new AttributeModel("A", "value2");
        
        int result = comparator.compare(object1, object2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void compare_BothObjectsEqualToA_CaseInsensitive_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("A", "value1");
        AttributeModel object2 = new AttributeModel("a", "value2");
        
        int result = comparator.compare(object1, object2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void compare_FirstObjectAfterA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("banana", "value");
        AttributeModel object2 = new AttributeModel("apple", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void compare_SecondObjectAfterA_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("apple", "value");
        AttributeModel object2 = new AttributeModel("banana", "value");
        
        int result = comparator.compare(object1, object2);
        
        assertTrue(result < 0);
    }
}