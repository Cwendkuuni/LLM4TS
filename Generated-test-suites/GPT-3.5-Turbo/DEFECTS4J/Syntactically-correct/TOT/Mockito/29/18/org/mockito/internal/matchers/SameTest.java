
import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches_True() {
        // Typical Use Case: same object is passed
        String input = "Test";
        Same same = new Same(input);
        assertTrue(same.matches(input));
    }

    @Test
    public void testMatches_False() {
        // Typical Use Case: different object is passed
        String input1 = "Test";
        String input2 = new String("Test");
        Same same = new Same(input1);
        assertFalse(same.matches(input2));
    }

    @Test
    public void testMatches_Null() {
        // Edge Case: null input
        Same same = new Same(null);
        assertFalse(same.matches(null));
    }

    @Test
    public void testDescribeTo_String() {
        // Typical Use Case: string input
        String input = "Test";
        Same same = new Same(input);
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description, times(1)).appendText("same(\"Test\")");
    }

    @Test
    public void testDescribeTo_Character() {
        // Typical Use Case: character input
        Character input = 'a';
        Same same = new Same(input);
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description, times(1)).appendText("same('a')");
    }
}
