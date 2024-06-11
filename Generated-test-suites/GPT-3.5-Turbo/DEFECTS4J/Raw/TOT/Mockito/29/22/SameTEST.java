import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

public class SameTest {

    private Same same;

    @Before
    public void setUp() {
        Object wanted = new Object();
        same = new Same(wanted);
    }

    @Test
    public void testMatches_WhenObjectsAreSame_ThenReturnTrue() {
        // Arrange
        Object actual = same.wanted;

        // Act
        boolean result = same.matches(actual);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void testMatches_WhenObjectsAreNotSame_ThenReturnFalse() {
        // Arrange
        Object actual = new Object();

        // Act
        boolean result = same.matches(actual);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void testDescribeTo_WhenObjectIsString_ThenDescriptionIncludesQuotes() {
        // Arrange
        Description description = new StringDescription();
        same = new Same("Hello");

        // Act
        same.describeTo(description);

        // Assert
        assertThat(description.toString(), containsString("same(\"Hello\")"));
    }

    @Test
    public void testDescribeTo_WhenObjectIsCharacter_ThenDescriptionIncludesQuotes() {
        // Arrange
        Description description = new StringDescription();
        same = new Same('A');

        // Act
        same.describeTo(description);

        // Assert
        assertThat(description.toString(), containsString("same('A')"));
    }

    @Test
    public void testDescribeTo_WhenObjectIsNotStringOrCharacter_ThenDescriptionDoesNotIncludeQuotes() {
        // Arrange
        Description description = new StringDescription();
        same = new Same(123);

        // Act
        same.describeTo(description);

        // Assert
        assertThat(description.toString(), containsString("same(123)"));
    }

}