
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class AttributeModelComparatorTest {

    private final AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void test_compare_bothNull() {
        // Arrange
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void test_compare_firstNull() {
        // Arrange
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void test_compare_secondNull() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = null;
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void test_compare_bothNonNull() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test1");
        AttributeModel o2 = new AttributeModel("Test2");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void test_compare_equalNames() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void test_compare_bothNamesStartWith_A() {
        // Arrange
        AttributeModel o1 = new AttributeModel("AAA");
        AttributeModel o2 = new AttributeModel("AAB");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void test_compare_firstNameStartsWith_A() {
        // Arrange
        AttributeModel o1 = new AttributeModel("AAA");
        AttributeModel o2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void test_compare_secondNameStartsWith_A() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel("AAA");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void test_compare_sameNameDifferentCases() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel("tEsT");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(-32, result);
    }
    
    @Test
    public void test_compare_firstNullNameSecondNonNullName() {
        // Arrange
        AttributeModel o1 = new AttributeModel(null);
        AttributeModel o2 = new AttributeModel("Test");
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void test_compare_firstNonNullNameSecondNullName() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(o1, o2);
        
        // Assert
        assertEquals(-1, result);
    }
}
