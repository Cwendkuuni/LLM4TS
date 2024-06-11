import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullInputs_ShouldReturnNegative() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_EqualNames_ShouldReturnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("ABC");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1Null_ShouldReturnNegative() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("XYZ");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O2Null_ShouldReturnPositive() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = null;
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_O1NameBeforeA_O2NameEqualToA_ShouldReturnPositive() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("aaa");
        AttributeModel o2 = new AttributeModel("Aaa");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_O1NameEqualToA_O2NameBeforeA_ShouldReturnNegative() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Aaa");
        AttributeModel o2 = new AttributeModel("aaa");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O1AndO2NullNames_ShouldReturnZero() {
        // Arrange
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel(null);
        AttributeModel o2 = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(0, result);
    }
}