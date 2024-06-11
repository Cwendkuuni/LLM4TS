import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testMatches() {
        Object object1 = new Object();
        Same same = new Same(object1);

        assertTrue(same.matches(object1));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object object1 = new Object();
        Same same = new Same(object1);

        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(" + object1.toString() + ")", description.toString());
    }
}