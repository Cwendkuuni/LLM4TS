import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObject() {
        // Arrange
        Object obj = new Object();
        Same same = new Same(obj);

        // Act
        boolean result = same.matches(obj);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_differentObjects() {
        // Arrange
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        // Act
        boolean result = same.matches(obj2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatches_nullObject() {
        // Arrange
        Object obj = null;
        Same same = new Same(obj);

        // Act
        boolean result = same.matches(obj);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDescribeTo_string() {
        // Arrange
        String str = "test";
        Same same = new Same(str);

        // Act
        Description description = new Description();
        same.describeTo(description);

        // Assert
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_character() {
        // Arrange
        char ch = 'a';
        Same same = new Same(ch);

        // Act
        Description description = new Description();
        same.describeTo(description);

        // Assert
        assertEquals("same('a')", description.toString());
    }

}