import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test");
        AttributeModel attribute2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NullAttributes_ReturnsNegativeOne() {
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
    public void testCompare_NullFirstAttribute_ReturnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_NullSecondAttribute_ReturnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test");
        AttributeModel attribute2 = null;
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeHasLowerName_ReturnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("A");
        AttributeModel attribute2 = new AttributeModel("B");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeHasHigherName_ReturnsOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("B");
        AttributeModel attribute2 = new AttributeModel("A");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_AttributeNamesAreCaseInsensitive_ReturnsZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("test");
        AttributeModel attribute2 = new AttributeModel("TEST");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_FirstAttributeIsBeforeA_SecondAttributeIsAfterA_ReturnsOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Aa");
        AttributeModel attribute2 = new AttributeModel("B");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeIsBeforeA_FirstAttributeIsAfterA_ReturnsNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("B");
        AttributeModel attribute2 = new AttributeModel("Aa");
        
        // Act
        int result = comparator.compare(attribute1, attribute2);
        
        // Assert
        assertEquals(-1, result);
    }
    
}