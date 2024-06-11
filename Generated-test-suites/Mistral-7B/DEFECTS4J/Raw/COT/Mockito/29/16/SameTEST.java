import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Same sameInstance = new Same(new Object());
        Object actual = new Object();
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Same sameInstance = new Same(new Object());
        Object differentInstance = new Object();
        assertFalse(sameInstance.matches(differentInstance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        new Same(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullActual() {
        Same sameInstance = new Same(new Object());
        assertNull(sameInstance.matches(null));
    }

    @Test
    public void testStringMatchesString() {
        Same sameInstance = new Same("hello");
        String actual = "hello";
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testStringDoesNotMatchDifferentString() {
        Same sameInstance = new Same("hello");
        String differentString = "world";
        assertFalse(sameInstance.matches(differentString));
    }

    @Test
    public void testCharacterMatchesCharacter() {
        Same sameInstance = new Same('a');
        Character actual = 'a';
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testCharacterDoesNotMatchDifferentCharacter() {
        Same sameInstance = new Same('a');
        Character differentCharacter = 'b';
        assertFalse(sameInstance.matches(differentCharacter));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWantedIsAnInstanceOfDifferentType() {
        Same sameInstance = new Same("hello");
        Integer number = 10;
        assertFalse(sameInstance.matches(number));
    }
}