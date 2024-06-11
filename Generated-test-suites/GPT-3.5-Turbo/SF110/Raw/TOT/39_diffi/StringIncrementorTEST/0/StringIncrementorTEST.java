import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Typical use case with equal strings
        RelativeString result = StringIncrementor.diff("abc", "abc");
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());

        // Test case 2: Typical use case with different strings
        result = StringIncrementor.diff("abc", "def");
        assertEquals(0, result.linksCount());
        assertEquals("def", result.getAbsolute());

        // Test case 3: Edge case with strings containing numbers
        result = StringIncrementor.diff("a123", "a456");
        assertEquals(0, result.linksCount());
        assertEquals("a456", result.getAbsolute());

        // Test case 4: Edge case with empty string
        result = StringIncrementor.diff("", "abc");
        assertEquals(0, result.linksCount());
        assertEquals("abc", result.getAbsolute());

        // Test case 5: Error scenario with null input
        result = StringIncrementor.diff(null, "abc");
        assertNull(result);

        // Test case 6: Error scenario with null input
        result = StringIncrementor.diff("abc", null);
        assertNull(result);
    }

    @Test
    public void testDiffVerbose() {
        // Test case 1: Typical use case with verbose output
        RelativeString result = StringIncrementor.diff("abc", "def", true);
        assertEquals(0, result.linksCount());
        assertEquals("def", result.getAbsolute());

        // Test case 2: Edge case with verbose output
        result = StringIncrementor.diff("a123", "a456", true);
        assertEquals(0, result.linksCount());
        assertEquals("a456", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        // Test case 1: Typical use case with no links
        String result = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", result);

        // Test case 2: Typical use case with one link
        RelativeString relativeString = new RelativeString();
        StringLink link = new StringLink(0, 0, 3);
        relativeString.addLink(link);
        result = StringIncrementor.patch("abc", relativeString);
        assertEquals("abc", result);

        // Test case 3: Typical use case with multiple links
        relativeString = new RelativeString();
        link = new StringLink(0, 0, 1);
        relativeString.addLink(link);
        link = new StringLink(1, 1, 1);
        relativeString.addLink(link);
        result = StringIncrementor.patch("abc", relativeString);
        assertEquals("abc", result);

        // Test case 4: Edge case with empty string
        relativeString = new RelativeString();
        link = new StringLink(0, 0, 1);
        relativeString.addLink(link);
        result = StringIncrementor.patch("", relativeString);
        assertEquals("a", result);

        // Test case 5: Error scenario with null input
        result = StringIncrementor.patch(null, relativeString);
        assertNull(result);

        // Test case 6: Error scenario with null input
        result = StringIncrementor.patch("abc", null);
        assertNull(result);
    }

    @Test
    public void testPatchVerbose() {
        // Test case 1: Typical use case with verbose output
        RelativeString relativeString = new RelativeString();
        StringLink link = new StringLink(0, 0, 3);
        relativeString.addLink(link);
        String result = StringIncrementor.patch("abc", relativeString, true);
        assertEquals("abc", result);

        // Test case 2: Edge case with verbose output
        relativeString = new RelativeString();
        link = new StringLink(0, 0, 1);
        relativeString.addLink(link);
        result = StringIncrementor.patch("abc", relativeString, true);
        assertEquals("bc", result);
    }
}