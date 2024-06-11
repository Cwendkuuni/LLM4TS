import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.arguments.Arguments;
import static org.mockito.Mockito.*;

import java.util.Objects;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameStrings() {
        String wanted = "mockito";
        String actual = "mockito";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "mockito";
        String actual = "junit";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = 'a';

        ArgumentMatcher<Character> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacter() {
        Character wanted = 'a';
        Character actual = 'b';

        ArgumentMatcher<Character> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullAndNonnullObject() {
        Object wanted = new Object();
        Object actual = null;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameCollectionOfObjects() {
        Object wanted = new Object();
        Object[] actual = {wanted};

        ArgumentMatcher<Object[]> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCollectionOfObjects() {
        Object wanted = new Object();
        Object[] actual = {new Object()};

        ArgumentMatcher<Object[]> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameArrayOfPrimitiveTypes() {
        int wanted = 10;
        int[] actual = {wanted};

        ArgumentMatcher<int[]> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentArrayOfPrimitiveTypes() {
        int wanted = 10;
        int[] actual = {5};

        ArgumentMatcher<int[]> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMatcher() {
        ArgumentMatcher<Object> nullMatcher = null;
        ArgumentMatcher<Object> sameMatcher = new Same(new Object());
        sameMatcher.matches(null, nullMatcher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullActual() {
        Object wanted = new Object();
        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        sameMatcher.matches(null, wanted);
    }
}