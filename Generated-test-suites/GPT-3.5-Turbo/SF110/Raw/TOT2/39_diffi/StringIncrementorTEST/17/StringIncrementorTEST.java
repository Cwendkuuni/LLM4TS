import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result = StringIncrementor.diff("abcde", "ab123cde");
        assertEquals(result.toString(), "[ (0, 0, 6) ]");

        // Edge case - empty strings
        RelativeString result2 = StringIncrementor.diff("", "");
        assertEquals(result2.toString(), "");

        // Error scenario - null input
        try {
            StringIncrementor.diff(null, "abc");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testPatch() {
        // Typical use case
        RelativeString r = StringIncrementor.diff("abcde", "ab123cde");
        String result = StringIncrementor.patch("abcde", r);
        assertEquals(result, "ab123cde");

        // Edge case - empty RelativeString
        String result2 = StringIncrementor.patch("abcde", new RelativeString());
        assertEquals(result2, "abcde");

        // Error scenario - null input
        try {
            StringIncrementor.patch(null, r);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}