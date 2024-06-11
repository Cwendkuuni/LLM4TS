import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches_sameObject() {
        // Arrange
        Same same = new Same("test");

        // Act
        boolean result = same.matches("test");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_differentObject() {
        // Arrange
        Same same = new Same("test");

        // Act
        boolean result = same.matches("different");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_string() {
        // Arrange
        Same same = new Same("test");
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_character() {
        // Arrange
        Same same = new Same('a');
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same('a')");
    }
}