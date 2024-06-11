import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches_WhenObjectIsSame_ReturnsTrue() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        // Act
        boolean result = same.matches(wanted);
        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WhenObjectIsDifferent_ReturnsFalse() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        // Act
        boolean result = same.matches(new Object());
        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_WhenNull_ReturnsFalse() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        // Act
        boolean result = same.matches(null);
        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeTo_WhenObjectIsString_AppendsQuotingWithString() {
        // Arrange
        Object wanted = "Hello world";
        Same same = new Same(wanted);
        Description description = mock(Description.class);
        // Act
        same.describeTo(description);
        // Assert
        verify(description).appendText("same(\"");
        verify(description).appendText(wanted.toString());
        verify(description).appendText("\")");
    }

    @Test
    public void testDescribeTo_WhenObjectIsCharacter_AppendsQuotingWithCharacter() {
        // Arrange
        Object wanted = 'a';
        Same same = new Same(wanted);
        Description description = mock(Description.class);
        // Act
        same.describeTo(description);
        // Assert
        verify(description).appendText("same('");
        verify(description).appendText(wanted.toString());
        verify(description).appendText("')");
    }

    @Test
    public void testDescribeTo_WhenObjectIsNotStringOrCharacter_DoesNotAppendQuoting() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);
        Description description = mock(Description.class);
        // Act
        same.describeTo(description);
        // Assert
        verify(description).appendText("same(");
        verify(description).appendText(wanted.toString());
        verify(description).appendText(")");
        verify(description, never()).appendText("\"");
        verify(description, never()).appendText("'");
    }
}