import org.hamcrest.Description;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Same;
import org.junit.Test;

import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_whenObjectsAreSame_thenReturnTrue() {
        // Arrange
        Object input = new Object();
        Same same = new Same(input);

        // Act
        boolean result = same.matches(input);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_whenObjectsAreDifferent_thenReturnFalse() {
        // Arrange
        Object input = new Object();
        Same same = new Same(input);
        Object differentInput = new Object();

        // Act
        boolean result = same.matches(differentInput);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_whenInputIsString_thenAppendQuotingWithDoubleQuotes() {
        // Arrange
        Object input = "Test";
        Same same = new Same(input);
        Description description = Mockito.mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        Mockito.verify(description).appendText("same(\"Test\")");
        Mockito.verify(description).appendText("\"");
    }

    @Test
    public void testDescribeTo_whenInputIsCharacter_thenAppendQuotingWithSingleQuote() {
        // Arrange
        Object input = 'c';
        Same same = new Same(input);
        Description description = Mockito.mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        Mockito.verify(description).appendText("same('c')");
        Mockito.verify(description).appendText("'");
    }

    @Test
    public void testDescribeTo_whenInputIsNotStringOrCharacter_thenDoNotAppendQuoting() {
        // Arrange
        Object input = 100;
        Same same = new Same(input);
        Description description = Mockito.mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        Mockito.verify(description).appendText("same(100)");
        Mockito.verify(description, Mockito.never()).appendText("\"");
        Mockito.verify(description, Mockito.never()).appendText("'");
    }
}