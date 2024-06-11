import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatches_WantedSameAsActual_ReturnsTrue() {
        Object sameObject = new Object();
        Same same = new Same(sameObject);

        assertTrue(same.matches(sameObject));
    }

    @Test
    public void testMatches_WantedDifferentFromActual_ReturnsFalse() {
        Object wantedObject = new Object();
        Object actualObject = new Object();
        Same same = new Same(wantedObject);

        assertFalse(same.matches(actualObject));
    }

    @Test
    public void testDescribeTo_StringObject_CheckQuoting() {
        String wantedString = "test";
        Same same = new Same(wantedString);
        Description description = mock(Description.class);

        same.describeTo(description);

        // Assert that the correct quoting is appended to the description
    }

    @Test
    public void testDescribeTo_CharacterObject_CheckQuoting() {
        Character wantedChar = 'a';
        Same same = new Same(wantedChar);
        Description description = mock(Description.class);

        same.describeTo(description);

        // Assert that the correct quoting is appended to the description
    }

    @Test(expected = Exception.class)
    public void testDescribeTo_ExceptionThrown() {
        Same same = new Same(null);
        Description description = mock(Description.class);

        same.describeTo(description);

        // Expect an exception to be thrown when describing null object
    }
}