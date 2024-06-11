import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertNotNull(result);
        assertEquals(0, result.linksCount());
        assertEquals("def", result.getAbsolute());
    }

    @Test
    public void testDiffWithVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "abc", true);
        assertNotNull(result);
        assertEquals(0, result.linksCount());
        assertEquals("abc", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch("abc", r);
        assertEquals("xyzabc", result);
    }

    @Test
    public void testPatchWithVerbose() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch("abc", r, true);
        assertEquals("xyzabc", result);
    }
}