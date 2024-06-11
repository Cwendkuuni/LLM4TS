import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {
    
    @Test
    public void testMatches_sameObject_returnsTrue() {
        // Arrange
        Same same = new Same("hello");
        Object object = "hello";
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_differentObject_returnsFalse() {
        // Arrange
        Same same = new Same("hello");
        Object object = "world";
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_sameIntegerObject_returnsTrue() {
        // Arrange
        Same same = new Same(5);
        Object object = 5;
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_differentIntegerObject_returnsFalse() {
        // Arrange
        Same same = new Same(5);
        Object object = 10;
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_sameNullObject_returnsTrue() {
        // Arrange
        Same same = new Same(null);
        Object object = null;
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testDescribeTo_stringObject_appendsQuoting() {
        // Arrange
        Same same = new Same("hello");
        Description description = new StringDescription();
        
        // Act
        same.describeTo(description);
        
        // Assert
        assertEquals("same(\"hello\")", description.toString());
    }
    
    @Test
    public void testDescribeTo_charObject_appendsQuoting() {
        // Arrange
        Same same = new Same('a');
        Description description = new StringDescription();
        
        // Act
        same.describeTo(description);
        
        // Assert
        assertEquals("same('a')", description.toString());
    }
    
    @Test
    public void testDescribeTo_integerObject_doesNotAppendQuoting() {
        // Arrange
        Same same = new Same(5);
        Description description = new StringDescription();
        
        // Act
        same.describeTo(description);
        
        // Assert
        assertEquals("same(5)", description.toString());
    }
}