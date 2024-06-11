import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullWanted() {
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(null);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(null));
    }

    @Test
    public void testDifferentTypes() {
        Integer wanted = 1;
        String actual = "one";

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testStringQuoting() {
        String wanted = "hello";

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Mockito.verify(Mockito.mock(Description.class), Mockito.times(1)).appendText("same(\"hello\")");
    }

    @Test
    public void testCharacterQuoting() {
        Character wanted = 'a';

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Mockito.verify(Mockito.mock(Description.class), Mockito.times(1)).appendText("'a'");
    }
}