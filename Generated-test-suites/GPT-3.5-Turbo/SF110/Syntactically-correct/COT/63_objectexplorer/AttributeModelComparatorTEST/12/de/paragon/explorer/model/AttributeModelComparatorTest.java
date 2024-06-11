import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test when both AttributeModel objects are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test when o1 is null and o2 is not null
        AttributeModel o2 = new AttributeModel("Test", 1);
        assertEquals(-1, comparator.compare(null, o2));
        
        // Test when o1 is not null and o2 is null
        AttributeModel o1 = new AttributeModel("Test", 1);
        assertEquals(1, comparator.compare(o1, null));
        
        // Test when o1 name is "a" and o2 name is not "a"
        AttributeModel o3 = new AttributeModel("Test", 1);
        AttributeModel o4 = new AttributeModel("b", 2);
        assertEquals(-1, comparator.compare(o3, o4));
        
        // Test when o2 name is "a" and o1 name is not "a"
        AttributeModel o5 = new AttributeModel("b", 2);
        AttributeModel o6 = new AttributeModel("Test", 1);
        assertEquals(1, comparator.compare(o5, o6));
        
        // Test when o1 and o2 names are both "a"
        AttributeModel o7 = new AttributeModel("a", 1);
        AttributeModel o8 = new AttributeModel("a", 2);
        assertEquals(0, comparator.compare(o7, o8));
        
        // Test when o1 name is "a" and o2 name is "A"
        AttributeModel o9 = new AttributeModel("a", 1);
        AttributeModel o10 = new AttributeModel("A", 2);
        assertEquals(0, comparator.compare(o9, o10));
        
        // Test when o1 name is "A" and o2 name is "a"
        AttributeModel o11 = new AttributeModel("A", 1);
        AttributeModel o12 = new AttributeModel("a", 2);
        assertEquals(0, comparator.compare(o11, o12));
    }
    
    // Additional test cases for edge cases and exception handling can be added here
    
    // 
    
    // Test cases go here
    
    // 
}