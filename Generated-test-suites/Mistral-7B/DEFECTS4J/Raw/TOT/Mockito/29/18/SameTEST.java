import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

public class SameTest {

    @Test
    public void testSameObjects() {
        Same same = new Same(new Object());
        Object actual = new Object();

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Same same = new Same(new Object());
        Object different = new Object();

        assertFalse(same.matches(different));
    }

    @Test
    public void testNullObject() {
        Same same = new Same(new Object());
        Object nullObject = null;

        assertFalse(same.matches(nullObject));
    }

    @Test
    public void testStringMatcher() {
        Same same = new Same("test string");
        String actual = "test string";

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentStringMatcher() {
        Same same = new Same("test string");
        String different = "different string";

        assertFalse(same.matches(different));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentException() {
        Same same = new Same(null);
        Object actual = new Object();

        // This test case should throw a NullPointerException because the want argument is null
        same.matches(actual);
    }

    @Test
    public void testCharacterMatcher() {
        Same same = new Same('a');
        Character actual = 'a';

        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentCharacterMatcher() {
        Same same = new Same('a');
        Character different = 'b';

        assertFalse(same.matches(different));
    }
}