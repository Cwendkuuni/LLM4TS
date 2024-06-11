import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class SameTest {

    @Test
    public void testMatches() {
        Object object = new Object();
        Same same = new Same(object);
        
        assertTrue(same.matches(object));
    }

    @Test
    public void testNotMatches() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);
        
        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeTo_String() {
        Same same = new Same("test");
        Description description = mock(Description.class);
        
        same.describeTo(description);
        
        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_Character() {
        Same same = new Same('c');
        Description description = mock(Description.class);
        
        same.describeTo(description);
        
        verify(description).appendText("same('c')");
    }
    
    @Test
    public void testDescribeTo_Other() {
        Same same = new Same(123);
        Description description = mock(Description.class);
        
        same.describeTo(description);
        
        verify(description).appendText("same(123)");
    }

    @Test
    public void testDescribeTo_Null() {
        Same same = new Same(null);
        Description description = mock(Description.class);
        
        same.describeTo(description);
        
        verify(description).appendText("same(null)");
    }
}