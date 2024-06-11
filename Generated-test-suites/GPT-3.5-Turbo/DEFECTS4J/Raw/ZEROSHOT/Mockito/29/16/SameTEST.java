import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("different"));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testAppendQuoting() {
        Same sameString = new Same("test");
        Same sameChar = new Same('c');
        Description descriptionString = mock(Description.class);
        Description descriptionChar = mock(Description.class);
        sameString.appendQuoting(descriptionString);
        sameChar.appendQuoting(descriptionChar);
        verify(descriptionString).appendText("\"");
        verify(descriptionChar).appendText("'");
    }
}