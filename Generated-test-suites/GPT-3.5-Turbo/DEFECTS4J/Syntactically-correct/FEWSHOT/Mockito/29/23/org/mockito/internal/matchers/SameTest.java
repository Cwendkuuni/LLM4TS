import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        assertTrue(same.matches(obj1));
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo() {
        Description description = mock(Description.class);
        Same same = new Same("foo");
        
        same.describeTo(description);
        
        verify(description).appendText("same(");
        verify(description).appendText("\"");
        verify(description).appendText("foo");
        verify(description).appendText("\"");
        verify(description).appendText(")");
    }
}