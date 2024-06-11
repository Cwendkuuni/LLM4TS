import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        // Arrange
        Object obj = new Object();
        Same same = new Same(obj);
        
        // Act
        boolean result = same.matches(obj);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_differentObject_returnsFalse() {
        // Arrange
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        // Act
        boolean result = same.matches(obj2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testDescribeTo_stringObject_appendsQuoting() {
        // Arrange
        Object obj = "test";
        Description description = new DescriptionStub();
        Same same = new Same(obj);
        
        // Act
        same.describeTo(description);
        
        // Assert
        assertEquals("same(\"test\")", description.toString());
    }
    
    @Test
    public void testDescribeTo_charObject_appendsQuoting() {
        // Arrange
        Object obj = 'c';
        Description description = new DescriptionStub();
        Same same = new Same(obj);
        
        // Act
        same.describeTo(description);
        
        // Assert
        assertEquals("same('c')", description.toString());
    }
    
    private class DescriptionStub implements Description {
        
        private StringBuilder description = new StringBuilder();
        
        @Override
        public Description appendText(CharSequence text) {
            description.append(text);
            return this;
        }
        
        @Override
        public String toString() {
            return description.toString();
        }
        
        // Other methods of Description interface are not necessary for the test
    }
}