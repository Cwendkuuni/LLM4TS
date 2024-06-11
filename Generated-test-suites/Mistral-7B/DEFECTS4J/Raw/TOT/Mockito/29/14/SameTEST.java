import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testNotSameInstance() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testStringSame() {
        String wanted = "hello";
        Matcher<String> matcher = new Same(wanted);

        String actual = "hello";
        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testStringNotSame() {
        String wanted = "hello";
        Matcher<String> matcher = new Same(wanted);

        String actual = "world";
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testCharacterSame() {
        Character wanted = 'a';
        Matcher<Character> matcher = new Same(wanted);

        Character actual = 'a';
        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testCharacterNotSame() {
        Character wanted = 'a';
        Matcher<Character> matcher = new Same(wanted);

        Character actual = 'b';
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testNull() {
        Object wanted = null;
        Matcher<Object> matcher = new Same(wanted);

        Object actual = null;
        assertTrue(matcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        Object wanted = null;
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSerializableWanted() {
        Object wanted = new Object() {
            // Non-serializable class
        };
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object();
    }
}