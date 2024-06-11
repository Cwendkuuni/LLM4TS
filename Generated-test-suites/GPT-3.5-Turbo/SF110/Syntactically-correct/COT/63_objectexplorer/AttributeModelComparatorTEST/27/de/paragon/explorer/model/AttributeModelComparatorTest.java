import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test case 1: Both objects are null
        int result1 = comparator.compare(null, null);
        assertEquals(0, result1);
        
        // Test case 2: One object is null
        AttributeModel obj1 = new AttributeModel("Name1");
        int result2 = comparator.compare(obj1, null);
        assertEquals(-1, result2);
        
        // Test case 3: Different names, first name is "a"
        AttributeModel obj2 = new AttributeModel("Name2");
        int result3 = comparator.compare(obj1, obj2);
        assertEquals(-1, result3);
        
        // Test case 4: Different names, second name is "a"
        AttributeModel obj3 = new AttributeModel("aName");
        int result4 = comparator.compare(obj1, obj3);
        assertEquals(1, result4);
        
        // Test case 5: Same names
        AttributeModel obj4 = new AttributeModel("Name1");
        int result5 = comparator.compare(obj1, obj4);
        assertEquals(0, result5);
    }
}