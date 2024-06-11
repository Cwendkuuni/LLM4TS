import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothObjectsNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals("Both objects are null, return value should be -1", -1, result);
    }
    
    @Test
    public void testCompare_Object1IsNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object2 = new AttributeModel("Apple");
        int result = comparator.compare(null, object2);
        assertEquals("Object 1 is null, return value should be -1", -1, result);
    }
    
    @Test
    public void testCompare_Object2IsNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("Apple");
        int result = comparator.compare(object1, null);
        assertEquals("Object 2 is null, return value should be -1", -1, result);
    }

    @Test
    public void testCompare_BothObjectsHaveSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("Apple");
        AttributeModel object2 = new AttributeModel("Apple");
        int result = comparator.compare(object1, object2);
        assertEquals("Both objects have the same name, return value should be 0", 0, result);
    }
    
    @Test
    public void testCompare_Object1ComesBeforeObject2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("Apple");
        AttributeModel object2 = new AttributeModel("Banana");
        int result = comparator.compare(object1, object2);
        assertEquals("Object 1 comes before Object 2, return value should be -1", -1, result);
    }
    
    @Test
    public void testCompare_Object2ComesBeforeObject1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel object1 = new AttributeModel("Banana");
        AttributeModel object2 = new AttributeModel("Apple");
        int result = comparator.compare(object1, object2);
        assertEquals("Object 2 comes before Object 1, return value should be 1", 1, result);
    }
}