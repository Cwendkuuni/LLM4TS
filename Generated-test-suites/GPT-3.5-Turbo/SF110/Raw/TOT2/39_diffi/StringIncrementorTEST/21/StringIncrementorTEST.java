import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abcde", "xyz");
        assertEquals(0, result.linksCount());
        assertEquals("xyz", result.getAbsolute());
    }

    @Test
    public void testDiffWithVerbose() {
        RelativeString result = StringIncrementor.diff("abcde", "xyz", true);
        assertEquals(0, result.linksCount());
        assertEquals("xyz", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 2));
        r.setAbsolute("xyz");
        String patched = StringIncrementor.patch("abcde", r);
        assertEquals("axyzcde", patched);
    }

    @Test
    public void testPatchWithVerbose() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 2));
        r.setAbsolute("xyz");
        String patched = StringIncrementor.patch("abcde", r, true);
        assertEquals("axyzcde", patched);
    }

}