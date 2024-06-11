import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(null, null));
    }

    @Test
    public void testCompare_Object1Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj2 = new AttributeModel("b");
        assertEquals(-1, comparator.compare(null, obj2));
    }

    @Test
    public void testCompare_Object2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("a");
        assertEquals(1, comparator.compare(obj1, null));
    }

    @Test
    public void testCompare_SameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("a");
        AttributeModel obj2 = new AttributeModel("a");
        assertEquals(0, comparator.compare(obj1, obj2));
    }

    // Edge Case Scenario
    @Test
    public void testCompare_EdgeCase1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("A");
        AttributeModel obj2 = new AttributeModel("Z");
        assertEquals(1, comparator.compare(obj1, obj2));
    }

    // Edge Case Scenario
    @Test
    public void testCompare_EdgeCase2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("Z");
        AttributeModel obj2 = new AttributeModel("A");
        assertEquals(-1, comparator.compare(obj1, obj2));
    }

    // Exception Handling Scenario
    @Test(expected = NullPointerException.class)
    public void testCompare_NullPointerException() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        comparator.compare(new AttributeModel("test"), null);
    }
}