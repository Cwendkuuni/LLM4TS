import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("Apple");
        AttributeModel model2 = new AttributeModel("Banana");

        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
        
        result = comparator.compare(model2, model1);
        assertTrue(result > 0);
        
        AttributeModel model3 = new AttributeModel("apple");
        result = comparator.compare(model1, model3);
        assertEquals(result, 0);
        
        AttributeModel model4 = new AttributeModel("aardvark");
        result = comparator.compare(model1, model4);
        assertTrue(result < 0);
        
        AttributeModel model5 = new AttributeModel("Zebra");
        result = comparator.compare(model1, model5);
        assertTrue(result < 0);
        
        AttributeModel model6 = new AttributeModel(null);
        result = comparator.compare(model1, model6);
        assertEquals(result, -1);
    }
}