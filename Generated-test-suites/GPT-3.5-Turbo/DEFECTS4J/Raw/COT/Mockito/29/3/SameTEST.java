import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void matches_sameObject_returnsTrue() {
        // Arrange
        Object object = new Object();
        Same same = new Same(object);

        // Act
        boolean result = same.matches(object);

        // Assert
        assertTrue(result);
    }

    @Test
    public void matches_differentObject_returnsFalse() {
        // Arrange
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        // Act
        boolean result = same.matches(object2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void describeToWithStringArgument_appendsQuotingToDescription() {
        // Arrange
        Description description = new Description() {
            private String description = "";

            @Override
            public Description appendText(String text) {
                description += text;
                return this;
            }

            @Override
            public String toString() {
                return description;
            }
        };
        Same same = new Same("test");

        // Act
        same.describeTo(description);

        // Assert
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void describeToWithCharacterArgument_appendsQuotingToDescription() {
        // Arrange
        Description description = new Description() {
            private String description = "";

            @Override
            public Description appendText(String text) {
                description += text;
                return this;
            }

            @Override
            public String toString() {
                return description;
            }
        };
        Same same = new Same('x');

        // Act
        same.describeTo(description);

        // Assert
        assertEquals("same('x')", description.toString());
    }
}