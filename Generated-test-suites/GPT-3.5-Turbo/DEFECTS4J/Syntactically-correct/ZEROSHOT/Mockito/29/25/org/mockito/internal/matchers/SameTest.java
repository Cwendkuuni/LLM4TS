import org.hamcrest.Description;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        boolean result = same.matches(wanted);

        assertTrue(result);
    }

    @Test
    public void testMatches_notSame() {
        Object wanted = new Object();
        Object actual = new Object();
        Same same = new Same(wanted);

        boolean result = same.matches(actual);

        assertFalse(result);
    }

    @Test
    public void testDescribeTo_String() {
        String wanted = "test";
        Description description = new Description();
        Same same = new Same(wanted);

        same.describeTo(description);
        String descriptionString = description.toString();

        assertEquals("same(\"test\")", descriptionString);
    }

    @Test
    public void testDescribeTo_Character() {
        Character wanted = 'a';
        Description description = new Description();
        Same same = new Same(wanted);

        same.describeTo(description);
        String descriptionString = description.toString();

        assertEquals("same('a')", descriptionString);
    }

    @Test
    public void testDescribeTo_other() {
        Integer wanted = 10;
        Description description = new Description();
        Same same = new Same(wanted);

        same.describeTo(description);
        String descriptionString = description.toString();

        assertEquals("same(10)", descriptionString);
    }
}