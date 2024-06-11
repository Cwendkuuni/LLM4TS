import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;
import org.mockito.MatchArgument;

public class SameTest {

    @Test
    public void testMatches_WhenEqualObjects_ReturnsTrue() {
        Object obj1 = new Object();
        Same same = new Same(obj1);
        
        assertTrue(same.matches(obj1));
    }
    
    @Test
    public void testMatches_WhenDifferentObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testMatches_WhenSameObjectInstance_ReturnsTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1; // same object instance
        Same same = new Same(obj1);
        
        assertTrue(same.matches(obj2));
    }
    
    @Test
    public void testMatches_WhenNullObjects_ReturnsFalse() {
        Object obj1 = null;
        Object obj2 = null;
        Same same = new Same(obj1);
        
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testMatches_WhenDifferentTypes_ReturnsFalse() {
        Object obj = new Object();
        String str = "mockito";
        Same same = new Same(obj);
        
        assertFalse(same.matches(str));
    }
    
    @Test
    public void testConstructor_WithNullObject_DoesNotThrowException() {
        Object obj = null;
        Same same = new Same(obj);
        
        assertNotNull(same);
    }
    
    @Test
    public void testDescribeTo_WithEmptyDescription_DoesNotAppendText() {
        Description description = new StringDescription();
        Object obj = new Object();
        Same same = new Same(obj);
        
        same.describeTo(description);
        
        assertEquals("", description.toString());
    }
    
    @Test
    public void testDescribeTo_WithNonEmptyDescription_AppendsText() {
        Description description = new StringDescription();
        Character character = 'a';
        Same same = new Same(character);
        
        same.describeTo(description);
        
        assertEquals("same('a')", description.toString());
    }
}