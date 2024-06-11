
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        // Arrange
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        // Act
        boolean result = sameMatcher.matches(wanted);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_NotSame() {
        // Arrange
        Object wanted = new Object();
        Object actual = new Object();
        Same sameMatcher = new Same(wanted);

        // Act
        boolean result = sameMatcher.matches(actual);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_String() {
        // Arrange
        String wanted = "testString";
        Same sameMatcher = new Same(wanted);
        Description description = mock(Description.class);

        // Act
        sameMatcher.describeTo(description);

        // Assert
        verify(description).appendText("same(\"testString\")");
    }

    @Test
    public void testDescribeTo_Character() {
        // Arrange
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Description description = mock(Description.class);

        // Act
        sameMatcher.describeTo(description);

        // Assert
        verify(description).appendText("same('a')");
    }

    @Test
    public void testDescribeTo_Other() {
        // Arrange
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Description description = mock(Description.class);

        // Act
        sameMatcher.describeTo(description);

        // Assert
        verify(description).appendText("same(" + wanted.toString() + ")");
    }
}
