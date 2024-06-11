import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;


public class SameTest {

    @Test
    public void testMatches_whenObjectsAreSame() {
        // Arrange
        Object obj = new Object();
        Same same = new Same(obj);

        // Act
        boolean result = same.matches(obj);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_whenObjectsAreDifferent() {
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
    public void testDescribeTo_withoutQuoting() {
        // Arrange
        Object obj = new Object();
        Same same = new Same(obj);
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same(");
        verify(description).appendText(obj.toString());
        verify(description).appendText(")");
    }

    @Test
    public void testDescribeTo_withQuotingForString() {
        // Arrange
        String str = "Test";
        Same same = new Same(str);
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same(\"");
        verify(description).appendText(str);
        verify(description).appendText("\")");
    }

    @Test
    public void testDescribeTo_withQuotingForCharacter() {
        // Arrange
        char ch = 'a';
        Same same = new Same(ch);
        Description description = mock(Description.class);

        // Act
        same.describeTo(description);

        // Assert
        verify(description).appendText("same('");
        verify(description).appendText(Character.toString(ch));
        verify(description).appendText("')");
    }
}