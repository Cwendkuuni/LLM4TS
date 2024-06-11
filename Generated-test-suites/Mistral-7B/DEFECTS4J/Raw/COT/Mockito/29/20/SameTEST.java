import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.same;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        assertTrue(same(wanted).matches(wanted));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();
        assertFalse(same(wanted).matches(actual));
    }

    @Test
    public void testNullWanted() {
        Object wanted = null;
        assertFalse(same(wanted).matches(new Object()));
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();
        assertFalse(same(wanted).matches(null));
    }

    @Test
    public void testSameString() {
        String wanted = "test";
        String actual = "test";
        assertTrue(same(wanted).matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test";
        String actual = "different";
        assertFalse(same(wanted).matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = 'a';
        assertTrue(same(wanted).matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        Character wanted = 'a';
        Character actual = 'b';
        assertFalse(same(wanted).matches(actual));
    }
}