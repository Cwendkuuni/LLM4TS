import org.junit.Test;
import org.mockito.Description;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        // Arrange
        Object obj = new Object();
        Same same = new Same(obj);

        // Act
        boolean result = same.matches(obj);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_differentObjects_returnsFalse() {
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
    public void testDescribeTo_stringObject_appendsQuoting() {
        // Arrange
        String str = "test";
        Same same = new Same(str);
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_characterObject_appendsQuoting() {
        // Arrange
        Character ch = 'a';
        Same same = new Same(ch);
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same('a')");
    }
}