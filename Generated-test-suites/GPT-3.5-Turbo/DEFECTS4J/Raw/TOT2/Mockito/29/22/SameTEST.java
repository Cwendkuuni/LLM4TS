import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatches_True() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(same.matches(obj));
    }
    
    @Test
    public void testMatches_False() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo_String() {
        Description description = mock(Description.class);
        Same same = new Same("test");
        
        same.describeTo(description);
        
        // Verify the description
    }
    
    @Test
    public void testDescribeTo_Character() {
        Description description = mock(Description.class);
        Same same = new Same('c');
        
        same.describeTo(description);
        
        // Verify the description
    }
    
    @Test
    public void testDescribeTo_Object() {
        Description description = mock(Description.class);
        Same same = new Same(new Object());
        
        same.describeTo(description);
        
        // Verify the description
    }
}