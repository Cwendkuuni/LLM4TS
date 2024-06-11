import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_sameName_returnsZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("name1");
        AttributeModel attribute2 = new AttributeModel("name1");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_nullValues_returnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_nullValueFirstArgument_returnsPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("name2");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_nullValueSecondArgument_returnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("name1");
        AttributeModel attribute2 = null;
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_name1LexicographicallyLessThanA_returnsPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("a");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_name1LexicographicallyGreaterThanA_returnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("z");
        AttributeModel attribute2 = new AttributeModel("a");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_name1SameAsA_name2GreaterThanA_returnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("z");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_name1SameAsA_name2LessThanA_returnsPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("aa");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(1, result);
    }
}