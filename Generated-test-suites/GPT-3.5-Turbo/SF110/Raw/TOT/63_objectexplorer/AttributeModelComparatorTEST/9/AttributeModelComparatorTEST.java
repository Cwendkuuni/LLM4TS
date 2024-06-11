
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testNameComparison() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("b");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testNullComparison() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("b");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testEqualAttributeNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("A");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAttributeComparisonWithA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("b");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testAttributeComparisonWithoutA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = new AttributeModel("b");
        AttributeModel attribute2 = new AttributeModel("a");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testBothAttributesNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
}
