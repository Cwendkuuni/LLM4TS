import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(same.matches(obj));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = mock(Description.class);
        
        same.describeTo(description);
        
        verify(description).appendText("same(\"test\")");
    }
}