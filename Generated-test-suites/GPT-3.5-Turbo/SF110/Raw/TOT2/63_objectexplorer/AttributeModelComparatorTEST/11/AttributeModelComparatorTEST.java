import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_sameName() {
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel("Test");
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1Null() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test");
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o2Null() {
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_o1BeforeA() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Banana");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(o1, o2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_o2BeforeA() {
        AttributeModel o1 = new AttributeModel("Banana");
        AttributeModel o2 = new AttributeModel("Apple");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(o1, o2);

        assertEquals(1, result);
    }

}