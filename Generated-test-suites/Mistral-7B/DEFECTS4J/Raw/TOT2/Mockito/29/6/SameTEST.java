import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Same same = new Same(new Object());
        Object actual = new Object();
        assertTrue(same.matches(actual));
    }
}