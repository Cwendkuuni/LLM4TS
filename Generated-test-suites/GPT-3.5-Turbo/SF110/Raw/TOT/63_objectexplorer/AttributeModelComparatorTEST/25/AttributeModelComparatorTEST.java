import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attributeModel1 = new AttributeModel("Name1");
        attributeModel2 = new AttributeModel("Name2");
    }
    
    @Test
    public void testCompare_WhenBothParametersAreNull_ExpectZero() {
        // Arrange
        AttributeModel nullAttributeModel = null;
        
        // Act
        int result = comparator.compare(nullAttributeModel, nullAttributeModel);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterIsNull_ExpectNegativeOne() {
        // Arrange
        AttributeModel nullAttributeModel = null;
        
        // Act
        int result = comparator.compare(nullAttributeModel, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenSecondParameterIsNull_ExpectOne() {
        // Arrange
        AttributeModel nullAttributeModel = null;
        
        // Act
        int result = comparator.compare(attributeModel1, nullAttributeModel);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenBothParametersHaveNullName_ExpectZero() {
        // Arrange
        AttributeModel attributeModelNullName1 = new AttributeModel(null);
        AttributeModel attributeModelNullName2 = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(attributeModelNullName1, attributeModelNullName2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_WhenOneParameterHasNullName_ExpectNegativeOne() {
        // Arrange
        AttributeModel attributeModelNullName = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(attributeModelNullName, attributeModel1);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenOtherParameterHasNullName_ExpectOne() {
        // Arrange
        AttributeModel attributeModelNullName = new AttributeModel(null);
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModelNullName);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenBothNamesAreEqualIgnoreCase_ExpectZero() {
        // Arrange
        attributeModel1.setName("name");
        attributeModel2.setName("Name");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsBeforeSecondParameterNameIgnoreCase_ExpectNegativeOne() {
        // Arrange
        attributeModel1.setName("aaa");
        attributeModel2.setName("bbb");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsAfterSecondParameterNameIgnoreCase_ExpectOne() {
        // Arrange
        attributeModel1.setName("bbb");
        attributeModel2.setName("aaa");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsBeforeAAndSecondParameterNameIsAIgnoreCase_ExpectNegativeOne() {
        // Arrange
        attributeModel1.setName("aaa");
        attributeModel2.setName("A");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsAfterAAndSecondParameterNameIsAIgnoreCase_ExpectOne() {
        // Arrange
        attributeModel1.setName("bbb");
        attributeModel2.setName("A");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsAAndSecondParameterNameIsBeforeAIgnoreCase_ExpectOne() {
        // Arrange
        attributeModel1.setName("A");
        attributeModel2.setName("aaa");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenFirstParameterNameIsAAndSecondParameterNameIsAfterAIgnoreCase_ExpectNegativeOne() {
        // Arrange
        attributeModel1.setName("A");
        attributeModel2.setName("bbb");
        
        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);
        
        // Assert
        assertEquals(-1, result);
    }
    
}