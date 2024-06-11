import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(wanted);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_NotSame() {
        // Arrange
        Object wanted = new Object();
        Object actual = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_String() {
        // Arrange
        Object wanted = "test";
        Same same = new Same(wanted);

        // Act
        Description description = new Description();
        same.describeTo(description);

        // Assert
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        // Arrange
        Object wanted = 'a';
        Same same = new Same(wanted);

        // Act
        Description description = new Description();
        same.describeTo(description);

        // Assert
        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_NotStringOrCharacter() {
        // Arrange
        Object wanted = 10;
        Same same = new Same(wanted);

        // Act
        Description description = new Description();
        same.describeTo(description);

        // Assert
        assertEquals("same(10)", description.toString());
    }
}