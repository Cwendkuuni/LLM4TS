
import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_sameName_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Test", "value");
        AttributeModel o2 = new AttributeModel("Test", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1Before_o2_returnsNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("TestA", "value");
        AttributeModel o2 = new AttributeModel("TestB", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_o2Before_o1_returnsPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("TestB", "value");
        AttributeModel o2 = new AttributeModel("TestA", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_o1Null_returnsNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_o2Null_returnsPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Test", "value");
        AttributeModel o2 = null;
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_bothNull_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1NameBeforeA_returnsPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Test", "value");
        AttributeModel o2 = new AttributeModel("A", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_o2NameBeforeA_returnsNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("A", "value");
        AttributeModel o2 = new AttributeModel("Test", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_bothNamesBeforeA_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("B", "value");
        AttributeModel o2 = new AttributeModel("C", "value");
        
        int result = comparator.compare(o1, o2);
        
        assertEquals(0, result);
    }
}
