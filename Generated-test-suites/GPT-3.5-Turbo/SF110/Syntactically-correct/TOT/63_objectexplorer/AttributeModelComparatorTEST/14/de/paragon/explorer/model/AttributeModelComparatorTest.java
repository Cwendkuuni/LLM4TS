import org.junit.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_equalObjects_returnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("A");
        AttributeModel obj2 = new AttributeModel("a");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_firstObjectNull_returnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = null;
        AttributeModel obj2 = new AttributeModel("a");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_secondObjectNull_returnPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("a");
        AttributeModel obj2 = null;
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothObjectsNull_returnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = null;
        AttributeModel obj2 = null;
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_nameComparisonZero_returnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("abc");
        AttributeModel obj2 = new AttributeModel("ABC");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_firstObjectBeforeA_returnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("abc");
        AttributeModel obj2 = new AttributeModel("bcd");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_secondObjectBeforeA_returnPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("bcd");
        AttributeModel obj2 = new AttributeModel("abc");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_sameNameAfterA_returnNegativeOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("def");
        AttributeModel obj2 = new AttributeModel("abc");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_sameNameBeforeA_returnPositiveOne() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("abc");
        AttributeModel obj2 = new AttributeModel("def");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothObjectsWithNameNotNull_sameNameAsA_returnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("abc");
        AttributeModel obj2 = new AttributeModel("A");
        
        // Act
        int result = comparator.compare(obj1, obj2);
        
        // Assert
        assertEquals(0, result);
    }
}