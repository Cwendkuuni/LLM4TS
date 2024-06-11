import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameObject.matches(actual));
    }

    @Test
    public void testSameNull() {
        Same sameNull = new Same(null);
        Object actual = null;
        assertFalse(sameNull.matches(actual));
    }

    @Test
    public void testClassCastException() {
        Same sameObject = new Same(new Integer(1));
        Object actual = new String("Test");
        assertThrows(ClassCastException.class, () -> sameObject.matches(actual));
    }

    // Next steps: Test other methods and potential edge cases
}