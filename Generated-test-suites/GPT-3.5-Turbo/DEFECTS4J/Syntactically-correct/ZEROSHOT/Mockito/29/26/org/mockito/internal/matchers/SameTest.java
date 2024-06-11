import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {
    
    @Test
    public void testMatches_sameObject_returnsTrue() {
        // Arrange
        Object object = new Object();
        Same same = new Same(object);
        
        // Act
        boolean result = same.matches(object);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_differentObject_returnsFalse() {
        // Arrange
        Object object = new Object();
        Same same = new Same(object);
        
        // Act
        boolean result = same.matches(new Object());
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testDescribeTo_stringObject_appendsQuoting() {
        // Arrange
        Object object = "test";
        Same same = new Same(object);
        
        // Act
        Description description = mock(Description.class);
        same.describeTo(description);
        
        // Assert
        verify(description).appendText("same(\"test\")");
    }
    
    @Test
    public void testDescribeTo_characterObject_appendsQuoting() {
        // Arrange
        Object object = 'c';
        Same same = new Same(object);
        
        // Act
        Description description = mock(Description.class);
        same.describeTo(description);
        
        // Assert
        verify(description).appendText("same('c')");
    }
}