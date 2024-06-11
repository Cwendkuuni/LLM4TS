import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        // Typical use case: wanted object is same as actual object
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatches_NotSame() {
        // Typical use case: wanted object is different from actual object
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    @Test
    public void testMatches_Null() {
        // Edge case: actual object is null
        Object obj = null;
        Same same = new Same(obj);
        assertFalse(same.matches(null));
    }

    @Test
    public void testDescribeTo_String() {
        // Edge case: wanted object is a String
        String str = "example";
        Same same = new Same(str);
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same(\"example\")");
        verifyNoMoreInteractions(description);
    }

    @Test
    public void testDescribeTo_Character() {
        // Edge case: wanted object is a Character
        char ch = 'a';
        Same same = new Same(ch);
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same('a')");
        verifyNoMoreInteractions(description);
    }
}