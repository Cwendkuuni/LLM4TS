import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abxye";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(2, result.linksCount());
    }

    @Test
    public void testDiffWithVerbose() {
        String a = "abcde";
        String b = "abxye";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertNotNull(result);
    }

    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch(a, r);
        assertEquals("xyzde", result);
    }

    @Test
    public void testPatchWithVerbose() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch(a, r, true);
        assertNotNull(result);
    }
}