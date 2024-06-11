import org.hamcrest.Description;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {
    
    @Test
    public void testMatches() {
        // Test case 1
        Object obj1 = new Object();
        Same same1 = new Same(obj1);
        assertTrue(same1.matches(obj1));
        
        // Test case 2
        Object obj2 = new Object();
        Same same2 = new Same(obj1);
        assertFalse(same2.matches(obj2));
        
        // Test case 3
        Same same3 = new Same(null);
        assertTrue(same3.matches(null));
    }
    
    @Test
    public void testDescribeTo() {
        // Test case 1
        Object obj1 = new Object();
        Same same1 = new Same(obj1);
        Description description1 = new Description();
        same1.describeTo(description1);
        assertEquals("same(\"" + obj1.toString() + "\")", description1.toString());
        
        // Test case 2
        Object obj2 = 'a';
        Same same2 = new Same(obj2);
        Description description2 = new Description();
        same2.describeTo(description2);
        assertEquals("same('" + obj2.toString() + "')", description2.toString());
    }
}