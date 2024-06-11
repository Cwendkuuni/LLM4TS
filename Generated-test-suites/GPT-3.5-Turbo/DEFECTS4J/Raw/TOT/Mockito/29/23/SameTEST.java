import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import static org.junit.Assert.*;

public class SameTest {

    private Same same;

    @Before
    public void setup() {
        same = new Same("test");
    }

    @Test
    public void testMatches_SameObject_ReturnsTrue() {
        Object actual = "test";
        assertTrue(same.matches(actual));
    }

    @Test
    public void testMatches_DifferentObject_ReturnsFalse() {
        Object actual = "different";
        assertFalse(same.matches(actual));
    }

    @Test
    public void testDescribeTo_StringObject_AppendQuoting() {
        Description description = new Description();
        same.describeTo(description);
        String expected = "same(\"test\")";
        assertEquals(expected, description.toString());
    }

    @Test
    public void testDescribeTo_CharacterObject_AppendQuoting() {
        same = new Same('a');
        Description description = new Description();
        same.describeTo(description);
        String expected = "same('a')";
        assertEquals(expected, description.toString());
    }
}