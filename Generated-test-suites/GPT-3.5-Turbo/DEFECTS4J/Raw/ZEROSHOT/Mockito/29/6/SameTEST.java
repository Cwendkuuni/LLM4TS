import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches_String() {
        // Arrange
        String wanted = "same";
        String actual = "same";
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_Char() {
        // Arrange
        char wanted = 's';
        char actual = 's';
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_Integer() {
        // Arrange
        int wanted = 10;
        int actual = 10;
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_Objects() {
        // Arrange
        Object wanted = new Object();
        Object actual = wanted;
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testDescribeTo_String() {
        // Arrange
        String wanted = "same";
        StringBuilder description = new StringBuilder();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(\"same\")", description.toString());
    }

    @Test
    public void testDescribeTo_Char() {
        // Arrange
        char wanted = 's';
        StringBuilder description = new StringBuilder();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same('s')", description.toString());
    }
}