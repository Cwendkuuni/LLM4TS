import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        assertTrue(same.matches(object1));
        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeTo_String() {
        String string = "test";
        Same same = new Same(string);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Character character = 'a';
        Same same = new Same(character);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same('a')", description.toString());
    }
}