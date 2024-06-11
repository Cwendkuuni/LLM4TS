import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertEquals;

public class SameTest {

    @Test
    public void testMatches() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = wanted;

        // Act
        boolean result = same.matches(actual);

        // Assert
        assertEquals(true,result);
    }

    @Test
    public void testMatches_NotEqual() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = new Object();

        // Act
        boolean result = same.matches(actual);

        // Assert
        assertEquals(false,result);
    }

    @Test
    public void testDescribeTo_String() {
        // Arrange
        Object wanted = "test";
        Same same = new Same(wanted);
        Description description = new Description();

        // Act
        same.describeTo(description);
        String result = description.toString();

        // Assert
        assertEquals("same(\"test\")", result);
    }

    @Test
    public void testDescribeTo_Character() {
        // Arrange
        Object wanted = 'a';
        Same same = new Same(wanted);
        Description description = new Description();

        // Act
        same.describeTo(description);
        String result = description.toString();

        // Assert
        assertEquals("same('a')", result);
    }
}