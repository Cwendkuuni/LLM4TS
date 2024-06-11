import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjects() {
        Same same = new Same(new Integer(1));
        Integer actual = new Integer(1);

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Same same = new Same(new Integer(1));
        String actual = "one";

        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullWanted() {
        Same same = new Same(null);
        Integer actual = new Integer(1);

        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullActual() {
        Same same = new Same(new Integer(1));
        String wanted = "one";

        assertFalse(same.matches(null));
    }

    @Test
    public void testSameString() {
        Same same = new Same("hello");
        String actual = "hello";

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        Same same = new Same("hello");
        String actual = "world";

        assertFalse(same.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Same same = new Same('a');
        Character actual = 'a';

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentCharacter() {
        Same same = new Same('a');
        Character actual = 'b';

        assertFalse(same.matches(actual));
    }
}