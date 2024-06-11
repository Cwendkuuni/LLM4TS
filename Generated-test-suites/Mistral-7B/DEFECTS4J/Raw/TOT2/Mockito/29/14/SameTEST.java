import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;
        boolean result = sameMatcher.matches(actual);
        assertTrue(result);
    }
}