import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);

        assertTrue(same.matches(obj));
    }

    @Test
    public void testDescribeTo_String() {
        String str = "test";
        Same same = new Same(str);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Character c = 'c';
        Same same = new Same(c);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same('c')", description.toString());
    }

    @Test
    public void testDescribeTo_Null() {
        Object obj = null;
        Same same = new Same(obj);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(null)", description.toString());
    }

}