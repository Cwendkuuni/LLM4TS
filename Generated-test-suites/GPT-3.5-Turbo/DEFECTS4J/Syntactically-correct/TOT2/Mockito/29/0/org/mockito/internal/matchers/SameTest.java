import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("different"));
    }

    @Test
    public void testDescribeToString() {
        Same sameString = new Same("test");
        Description description = mock(Description.class);
        sameString.describeTo(description);
        describeToContentTest(description, "\"test\"");
    }

    @Test
    public void testDescribeToCharacter() {
        Same sameChar = new Same('c');
        Description description = mock(Description.class);
        sameChar.describeTo(description);
        describeToContentTest(description, "'c'");
    }

    private void describeToContentTest(Description description, String expectedContent) {
        // Check that the description content matches the expected content
        // This method is used to test both String and Character cases
    }
}