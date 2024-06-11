import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test basic functionality
        RelativeString result = StringIncrementor.diff("abc", "abdefg");
        assertEquals("defg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(0, 3, 3), result.getLink(0));
        
        // Test case with no links
        result = StringIncrementor.diff("abc", "def");
        assertEquals("def", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testDiffVerbose() {
        // Test verbose mode
        RelativeString result = StringIncrementor.diff("abc", "abdefg", true);
        assertEquals("defg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(0, 3, 3), result.getLink(0));
    }
    
    @Test
    public void testPatch() {
        // Test basic functionality
        String result = StringIncrementor.patch("abc", new RelativeString("defg"));
        assertEquals("abcdefg", result);
        
        // Test case with no links
        result = StringIncrementor.patch("abc", new RelativeString("def"));
        assertEquals("abcdef", result);
    }
    
    @Test
    public void testPatchVerbose() {
        // Test verbose mode
        String result = StringIncrementor.patch("abc", new RelativeString("defg"), true);
        assertEquals("abcdefg", result);
    }
}