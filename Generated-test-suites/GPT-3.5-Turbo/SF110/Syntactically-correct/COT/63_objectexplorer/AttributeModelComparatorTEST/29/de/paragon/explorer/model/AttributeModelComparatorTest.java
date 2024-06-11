import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullInput() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_EqualInput() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("test");
        AttributeModel attrModel2 = new AttributeModel("test");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_FirstInputNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel2 = new AttributeModel("test");
        int result = comparator.compare(null, attrModel2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_SecondInputNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("test");
        int result = comparator.compare(attrModel1, null);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_FirstInputNameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("apple");
        AttributeModel attrModel2 = new AttributeModel("banana");
        int result = comparator.compare(attrModel1, attrModel2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_SecondInputNameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("banana");
        AttributeModel attrModel2 = new AttributeModel("apple");
        int result = comparator.compare(attrModel1, attrModel2);
        assertTrue(result > 0);
    }
}