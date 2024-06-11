
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches_SameObject_ReturnsTrue() {
        // Arrange
        Object object = new Object();
        Same same = new Same(object);

        // Act
        boolean result = same.matches(object);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_DifferentObject_ReturnsFalse() {
        // Arrange
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        // Act
        boolean result = same.matches(object2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeTo_AppendTextWithQuoting() {
        // Arrange
        Description description = new Description.NullDescription();
        Same same = new Same("test");

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_AppendTextWithoutQuoting() {
        // Arrange
        Description description = new Description.NullDescription();
        Same same = new Same(123);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(123)", description.toString());
    }

}
