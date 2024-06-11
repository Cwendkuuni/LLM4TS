import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("apple");
        AttributeModel model2 = new AttributeModel("banana");
        AttributeModel model3 = new AttributeModel("cherry");
        
        assertEquals(0, comparator.compare(model1, model1)); // typical use case with equal names
        assertEquals(-1, comparator.compare(model1, model2)); // typical use case with different names
        assertEquals(1, comparator.compare(model3, model1)); // typical use case with different names
        
        // edge case: comparing against "a" should return 1
        assertEquals(1, comparator.compare(model1, new AttributeModel("zebra")));
        assertEquals(1, comparator.compare(model1, new AttributeModel("Aardvark")));
        
        // edge case: comparing against "a" should return -1
        assertEquals(-1, comparator.compare(new AttributeModel("zebra"), model1));
        assertEquals(-1, comparator.compare(new AttributeModel("Aardvark"), model1));
        
        // error scenario: comparing null objects
        assertEquals(-1, comparator.compare(null, model1));
        assertEquals(-1, comparator.compare(model1, null));
        assertEquals(-1, comparator.compare(null, null));
    }
}