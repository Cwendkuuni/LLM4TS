import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.Arguments;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Same matcher = new Same(obj1);
        assertTrue(matcher.matches(obj2));
    }

    @Test
    public void testDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Same matcher = new Same(obj1);
        assertFalse(matcher.matches(obj2));
    }

    @Test
    public void testSameString() {
        String str1 = "test";
        String str2 = "test";

        Same matcher = new Same(str1);
        assertTrue(matcher.matches(str2));
    }

    @Test
    public void testDifferentStrings() {
        String str1 = "test";
        String str2 = "different";

        Same matcher = new Same(str1);
        assertFalse(matcher.matches(str2));
    }

    @Test
    public void testSameCharacter() {
        char c1 = 'a';
        char c2 = 'a';

        Same matcher = new Same(c1);
        assertTrue(matcher.matches(c2));
    }

    @Test
    public void testDifferentCharacter() {
        char c1 = 'a';
        char c2 = 'b';

        Same matcher = new Same(c1);
        assertFalse(matcher.matches(c2));
    }

    @Test(expected = AssertionError.class)
    public void testNullObject() {
        Same matcher = new Same(null);
        matcher.matches(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testNullActual() {
        Object wanted = new Object();
        Same matcher = new Same(wanted);
        matcher.matches(null);
    }
}