import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.assertTrue;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Same sameMatcher = new Same(new Object());
        Object actual = new Object();
        assertTrue(sameMatcher.matches(actual));
    }
}