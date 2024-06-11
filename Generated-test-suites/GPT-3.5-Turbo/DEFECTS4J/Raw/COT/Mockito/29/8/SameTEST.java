import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("not test"));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = Mockito.mock(Description.class);
        same.describeTo(description);
        Mockito.verify(description).appendText("same(\"test\")");

        same = new Same('a');
        same.describeTo(description);
        Mockito.verify(description).appendText("same('a')");
    }

}