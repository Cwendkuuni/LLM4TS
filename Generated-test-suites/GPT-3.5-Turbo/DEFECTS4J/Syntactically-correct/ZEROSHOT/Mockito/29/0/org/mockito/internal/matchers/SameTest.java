import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
        
        Object anotherObj = new Object();
        assertFalse(same.matches(anotherObj));
    }
    
    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same same = new Same(obj);
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same(");
        verify(description).appendText(obj.toString());
        verify(description).appendText(")");
    }
}