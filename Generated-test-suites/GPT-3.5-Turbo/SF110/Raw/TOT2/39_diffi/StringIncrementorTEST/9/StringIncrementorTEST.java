import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abcde", "abxde");
        assertEquals("[[2, 2, 3]]", result.toString());
    }

    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abcde", "abxde", true);
        assertEquals("[[2, 2, 3]]", result.toString());
    }

    @Test
    public void testPatch() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 2, 3));
        String result = StringIncrementor.patch("abcde", r);
        assertEquals("abxde", result);
    }

    @Test
    public void testPatchVerbose() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 2, 3));
        String result = StringIncrementor.patch("abcde", r, true);
        assertEquals("abxde", result);
    }
}