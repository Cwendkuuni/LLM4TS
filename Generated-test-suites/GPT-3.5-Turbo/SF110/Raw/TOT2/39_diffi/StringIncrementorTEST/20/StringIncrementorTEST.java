import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "abcd");
        assertEquals("Result should have one link", 1, result.linksCount());
        assertEquals("Absolute value should be 'd'", "d", result.getAbsolute());
    }

    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abcde", "fghijkl", true);
        assertEquals("Result should have no links", 0, result.linksCount());
        assertEquals("Absolute value should be 'fghijkl'", "fghijkl", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        RelativeString relString = new RelativeString();
        StringLink link = new StringLink(0, 0, 3);
        relString.addLink(link);
        
        String result = StringIncrementor.patch("abc", relString);
        assertEquals("Patched string should be 'abc'", "abc", result);
    }

    @Test
    public void testPatchVerbose() {
        RelativeString relString = new RelativeString();
        StringLink link = new StringLink(0, 0, 3);
        relString.addLink(link);
        
        String result = StringIncrementor.patch("abc", relString, true);
        assertEquals("Patched string should be 'abc'", "abc", result);
    }
}