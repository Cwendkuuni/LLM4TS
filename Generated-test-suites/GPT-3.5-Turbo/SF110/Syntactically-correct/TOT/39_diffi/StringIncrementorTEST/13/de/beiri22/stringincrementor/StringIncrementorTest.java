
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_withTwoEqualStrings() {
        String a = "Hello";
        String b = "Hello";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("");
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_withDifferentStrings() {
        String a = "Hello";
        String b = "World";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("World");
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_withVerboseTrue() {
        String a = "Hello";
        String b = "HelloWorld";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("World");
        expected.addLink(new StringLink(0, 5, 5));
        assertEquals(expected, StringIncrementor.diff(a, b, true));
    }

    @Test
    public void testPatch_withEmptyRelativeString() {
        String a = "Hello";
        RelativeString r = new RelativeString();
        r.setAbsolute("");
        assertEquals(a, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_withNonEmptyRelativeString() {
        String a = "Hello";
        RelativeString r = new RelativeString();
        r.setAbsolute("World");
        r.addLink(new StringLink(0, 5, 5));
        assertEquals("HelloWorld", StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_withVerboseTrue() {
        String a = "Hello";
        RelativeString r = new RelativeString();
        r.setAbsolute("World");
        r.addLink(new StringLink(0, 5, 5));
        assertEquals("HelloWorld", StringIncrementor.patch(a, r, true));
    }
}
