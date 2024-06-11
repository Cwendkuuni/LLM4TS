import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare_equalObjects_returnsZero() {
        // Arrange
        Object lhs = new Object();
        Object rhs = lhs;
        
        // Act
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testReflectionCompare_nullObject_throwsNullPointerException() {
        // Arrange
        Object lhs = null;
        Object rhs = new Object();
        
        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            CompareToBuilder.reflectionCompare(lhs, rhs);
        });
    }
    
    @Test
    public void testReflectionCompare_differentClassObjects_throwsClassCastException() {
        // Arrange
        Object lhs = new Integer(5);
        Object rhs = new Long(5);
        
        // Act and Assert
        assertThrows(ClassCastException.class, () -> {
            CompareToBuilder.reflectionCompare(lhs, rhs);
        });
    }
    
    // TODO: Add more test cases for reflectionCompare method
    
    @Test
    public void testAppendSuper_positiveArgument_setsComparisonValue() {
        // Arrange
        CompareToBuilder builder = new CompareToBuilder();
        int superCompareTo = 1;
        
        // Act
        CompareToBuilder result = builder.appendSuper(superCompareTo);
        
        // Assert
        assertEquals(superCompareTo, builder.toComparison());
        assertSame(builder, result);
    }
    
    // TODO: Add more test cases for appendSuper method
    
    @Test
    public void testAppend_equalObjects_noComparison() {
        // Arrange
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        int initialComparison = builder.toComparison();
        
        // Act
        CompareToBuilder result = builder.append(lhs, rhs);
        
        // Assert
        assertEquals(initialComparison, builder.toComparison());
        assertSame(builder, result);
    }
    
    // TODO: Add more test cases for append method
    
    // TODO: Add more test cases for other methods in CompareToBuilder class
    
}