import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SameTest {
    @Mock
    private Description description;

    @Test
    public void testMatches() {
        Same same = new Same("test");

        assertTrue(same.matches("test"));
        assertTrue(same.matches(null));
        assertTrue(same.matches(new String("test")));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");

        same.describeTo(description);
        Mockito.verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testAppendQuoting() {
        Same same = new Same("test");

        same.appendQuoting(description);
        Mockito.verify(description).appendText("\"");
    }
}