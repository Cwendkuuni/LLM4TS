import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;
import org.junit.Test;

public class SameTest {
    
    @Test
    public void testMatches() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same1 = new Same(object1);
        
        assertTrue(same1.matches(object1));
        assertFalse(same1.matches(object2));
    }
    
    @Test
    public void testDescribeTo() {
        Object object = new Object();
        Same same = new Same(object);
        Description description = new Description();
        
        same.describeTo(description);
        assertEquals("same(" + object.toString() + ")", description.toString());
    }
    
    @Test
    public void testAppendQuoting() {
        Same same1 = new Same("test");
        Same same2 = new Same('a');
        Description description1 = new Description();
        Description description2 = new Description();
        
        same1.appendQuoting(description1);
        same2.appendQuoting(description2);
        
        assertEquals("\"", description1.toString());
        assertEquals("'", description2.toString());
    }
}