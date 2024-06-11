
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_equalNames_shouldReturnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name");
        AttributeModel attributeModel2 = new AttributeModel("Name");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_firstNameBeforeSecondName_shouldReturnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("aaa");
        AttributeModel attributeModel2 = new AttributeModel("bbb");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_firstNameAfterSecondName_shouldReturnOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("bbb");
        AttributeModel attributeModel2 = new AttributeModel("aaa");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_nullFirstObject_shouldReturnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("Name");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_nullSecondObject_shouldReturnOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name");
        AttributeModel attributeModel2 = null;
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothObjectsNull_shouldReturnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_nameIsNullForFirstObjectAndNotForSecondObject_shouldReturnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel("Name");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_nameNotIsNullForFirstObjectAndIsNullForSecondObject_shouldReturnOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("Name");
        AttributeModel attributeModel2 = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothObjectNamesAreNull_shouldReturnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
}
