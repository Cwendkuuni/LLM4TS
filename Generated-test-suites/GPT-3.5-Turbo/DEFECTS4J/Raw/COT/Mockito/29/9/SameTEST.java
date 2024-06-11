import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object object = new Object();
        Same same = new Same(object);

        assertTrue(same.matches(object));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object object = new Object();
        Same same = new Same(object);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(" + object.toString() + ")", description.toString());
    }
}