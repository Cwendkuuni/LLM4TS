import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical use case
        AttributeModel o1 = new AttributeModel("apple");
        AttributeModel o2 = new AttributeModel("banana");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
        
        // Edge case
        o1 = new AttributeModel("banana");
        o2 = new AttributeModel("apple");
        result = comparator.compare(o1, o2);
        assertEquals(1, result);
        
        // Error scenario
        o1 = null;
        o2 = new AttributeModel("pear");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}