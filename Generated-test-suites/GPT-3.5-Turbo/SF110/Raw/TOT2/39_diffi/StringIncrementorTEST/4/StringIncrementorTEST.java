import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abcde", "abxyz");
        assertFalse(result.getLinks().isEmpty());
        assertEquals("cde", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 2, 3));
        relativeString.setAbsolute("def");
        String result = StringIncrementor.patch("abc", relativeString);
        assertEquals("defbc", result);
    }

    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abcde", "abxyz", true);
        assertFalse(result.getLinks().isEmpty());
        assertEquals("cde", result.getAbsolute());
    }

    @Test
    public void testPatchVerbose() {
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 2, 3));
        relativeString.setAbsolute("def");
        String result = StringIncrementor.patch("abc", relativeString, true);
        assertEquals("defbc", result);
    }

}