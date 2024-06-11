import static org.junit.Assert.*;
import org.junit.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertEquals(0, result.linksCount());
        
        // Edge case
        result = StringIncrementor.diff("aaaaaabbbbb", "aaaaaabbbbb");
        assertEquals(1, result.linksCount());
        
        // Error scenario
        result = StringIncrementor.diff(null, "def");
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch() {
        // Typical use case
        RelativeString relString = StringIncrementor.diff("abc", "def");
        String patchedString = StringIncrementor.patch("abc", relString);
        assertEquals("abc", patchedString);
        
        // Edge case
        relString = StringIncrementor.diff("aaaaaabbbbb", "aaaaaabbbbb");
        patchedString = StringIncrementor.patch("aaaaaabbbbb", relString);
        assertEquals("aaaaaabbbbb", patchedString);
        
        // Error scenario
        patchedString = StringIncrementor.patch(null, relString);
        assertNull(patchedString);
    }
}