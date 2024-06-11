
import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same(5);
        assertTrue(same.matches(5));
        assertFalse(same.matches(10));
    }
    
    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }
}
