import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches_WhenObjectsAreSame_ReturnsTrue() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(wanted);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WhenObjectsAreNotSame_ReturnsFalse() {
        // Arrange
        Object wanted = new Object();
        Object actual = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeTo_WhenObjectIsString_AppendsQuotingWithDoubleQuotes() {
        // Arrange
        String wanted = "test";
        Description description = new Description.NullDescription();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_WhenObjectIsCharacter_AppendsQuotingWithSingleQuotes() {
        // Arrange
        char wanted = 't';
        Description description = new Description.NullDescription();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same('t')", description.toString());
    }

    @Test
    public void testDescribeTo_WhenObjectIsNotStringOrCharacter_DoesNotAppendQuoting() {
        // Arrange
        int wanted = 10;
        Description description = new Description.NullDescription();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(10)", description.toString());
    }

}