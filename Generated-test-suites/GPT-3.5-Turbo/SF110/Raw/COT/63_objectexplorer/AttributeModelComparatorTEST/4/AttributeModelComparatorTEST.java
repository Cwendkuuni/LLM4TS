import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel("apples");
        AttributeModel attributeModel2 = new AttributeModel("bananas");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test
    public void testCompareBothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompareFirstNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel = new AttributeModel("apples");
        
        int result = comparator.compare(null, attributeModel);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompareSecondNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel = new AttributeModel("bananas");
        
        int result = comparator.compare(attributeModel, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompareBothA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompareFirstA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("bananas");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompareSecondA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel("apples");
        AttributeModel attributeModel2 = new AttributeModel("a");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }
}