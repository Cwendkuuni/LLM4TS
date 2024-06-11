import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        // Arrange
        Same same = new Same("testString");
        String actual = "testString";
        
        // Act
        boolean result = same.matches(actual);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_differentObject_sameValue_returnsFalse() {
        // Arrange
        Same same = new Same(10);
        Integer actual = new Integer(10);
        
        // Act
        boolean result = same.matches(actual);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatches_nullObject_returnsFalse() {
        // Arrange
        Same same = new Same(new Object());
        Object actual = null;
        
        // Act
        boolean result = same.matches(actual);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_stringValue_appendsQuoting() {
        // Arrange
        Same same = new Same("testString");
        Description description = new Description();

        // Act
        same.describeTo(description);

        // Assert
        assertEquals("same(\"testString\")", description.toString());
    }

    @Test
    public void testDescribeTo_characterValue_appendsQuoting() {
        // Arrange
        Same same = new Same('A');
        Description description = new Description();

        // Act
        same.describeTo(description);

        // Assert
        assertEquals("same('A')", description.toString());
    }

}