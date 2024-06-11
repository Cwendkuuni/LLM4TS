import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesNull() {
        Object wanted = null;
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentClasses() {
        Object wanted = new String("hello");
        Object actual = new Integer(123);
        assertFalse(new Same(wanted).matches(actual));
    }

    @Test
    public void testMatchesPrimitiveType() {
        int wanted = 10;
        Same sameMatcher = new Same((Object) wanted);
        int actual = 10;
        assertTrue(sameMatcher.matches(actual));
    }

}