import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical use case
        AttributeModel model1 = new AttributeModel("apple");
        AttributeModel model2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(model1, model2));
        
        // Edge case: comparing with "a"
        model1 = new AttributeModel("apple");
        model2 = new AttributeModel("aardvark");
        assertEquals(1, comparator.compare(model1, model2));
        
        // Edge case: comparing with "A"
        model1 = new AttributeModel("Apple");
        model2 = new AttributeModel("aardvark");
        assertEquals(-1, comparator.compare(model1, model2));
        
        // Error scenario: comparing null objects
        model1 = null;
        model2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(model1, model2));
        
        model1 = new AttributeModel("apple");
        model2 = null;
        assertEquals(-1, comparator.compare(model1, model2));
    }
}