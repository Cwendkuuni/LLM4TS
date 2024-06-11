import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompareNullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompareEqualObjects() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("abc");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompareDifferentObjects() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("def");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompareWithEdgeCase() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel("z");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompareWithExceptionHandling() {
        AttributeModel a1 = new AttributeModel(null);
        AttributeModel a2 = new AttributeModel("xyz");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(a1, a2);
        assertTrue(result < 0);
    }
}