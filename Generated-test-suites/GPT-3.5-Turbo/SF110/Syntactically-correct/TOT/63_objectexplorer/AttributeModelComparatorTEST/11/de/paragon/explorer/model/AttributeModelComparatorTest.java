
import junit.framework.TestCase;

public class AttributeModelComparatorTest extends TestCase {

    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // typical use cases
        AttributeModel model1 = new AttributeModel("name1");
        AttributeModel model2 = new AttributeModel("name2");
        assertEquals(-1, comparator.compare(model1, model2));
        
        AttributeModel model3 = new AttributeModel("name3");
        AttributeModel model4 = new AttributeModel("name3");
        assertEquals(0, comparator.compare(model3, model4));
        
        AttributeModel model5 = new AttributeModel("Name");
        AttributeModel model6 = new AttributeModel("name");
        assertTrue(comparator.compare(model5, model6) > 0);
        
        // edge cases
        AttributeModel model7 = new AttributeModel("a");
        AttributeModel model8 = new AttributeModel("A");
        assertTrue(comparator.compare(model7, model8) < 0);
        
        AttributeModel model9 = new AttributeModel("z");
        AttributeModel model10 = new AttributeModel("Z");
        assertTrue(comparator.compare(model9, model10) > 0);
        
        AttributeModel model11 = null;
        AttributeModel model12 = new AttributeModel("name");
        assertTrue(comparator.compare(model11, model12) < 0);
        
        AttributeModel model13 = new AttributeModel("name");
        AttributeModel model14 = null;
        assertTrue(comparator.compare(model13, model14) > 0);
        
        // error scenarios
        AttributeModel model15 = null;
        AttributeModel model16 = null;
        assertEquals(-1, comparator.compare(model15, model16));
    }

}
