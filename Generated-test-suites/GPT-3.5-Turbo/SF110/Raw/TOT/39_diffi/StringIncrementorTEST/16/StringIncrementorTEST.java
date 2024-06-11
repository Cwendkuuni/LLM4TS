
import org.junit.Test;
import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_noVerbose() {
        // Test case 1: Both input strings are empty
        RelativeString result = StringIncrementor.diff("", "");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 2: First input string is empty
        result = StringIncrementor.diff("", "abcde");
        assertNotNull(result);
        assertEquals("abcde", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 3: Second input string is empty
        result = StringIncrementor.diff("abcde", "");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 4: Both input strings are equal
        result = StringIncrementor.diff("abcde", "abcde");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 5: First input string is a substring of the second input string
        result = StringIncrementor.diff("abc", "abcd");
        assertNotNull(result);
        assertEquals("d", result.getAbsolute());
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(0, link.getPosOrig());
        assertEquals(3, link.getPosNew());
        assertEquals(1, link.getLen());

        // Test case 6: Second input string is a substring of the first input string
        result = StringIncrementor.diff("abcd", "abc");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 7: Both input strings have different characters at each index
        result = StringIncrementor.diff("abcde", "fghij");
        assertNotNull(result);
        assertEquals("fghij", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 8: Both input strings have same characters at each index
        result = StringIncrementor.diff("abcde", "abcde");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testDiff_verbose() {
        // Test case 1: First input string is a substring of the second input string
        RelativeString result = StringIncrementor.diff("abc", "abcd", true);
        assertNotNull(result);
        assertEquals("d", result.getAbsolute());
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(0, link.getPosOrig());
        assertEquals(3, link.getPosNew());
        assertEquals(1, link.getLen());
        
        // Test case 2: Second input string is a substring of the first input string
        result = StringIncrementor.diff("abcd", "abc", true);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test case 3: Both input strings have same characters at each index
        result = StringIncrementor.diff("abcde", "abcde", true);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch_noVerbose() {
        // Test case 1: Patch empty string with empty relative string
        String result = StringIncrementor.patch("", new RelativeString());
        assertEquals("", result);

        // Test case 2: Patch string with empty relative string
        result = StringIncrementor.patch("abcde", new RelativeString());
        assertEquals("abcde", result);

        // Test case 3: Patch string with non-empty relative string
        StringLink link = new StringLink(0, 2, 3);
        RelativeString r = new RelativeString();
        r.addLink(link);
        r.setAbsolute("xyz");
        result = StringIncrementor.patch("abcde", r);
        assertEquals("xyzabcde", result);

        // Test case 4: Patch empty string with non-empty relative string
        result = StringIncrementor.patch("", r);
        assertEquals("xyz", result);

        // Test case 5: Patch string with non-empty relative string having links at the end
        link = new StringLink(3, 5, 2);
        r = new RelativeString();
        r.addLink(link);
        r.setAbsolute("def");
        result = StringIncrementor.patch("abc", r);
        assertEquals("abcdef", result);
    }

    @Test
    public void testPatch_verbose() {
        // Test case: Patch string with non-empty relative string and print verbose output
        StringLink link = new StringLink(0, 2, 3);
        RelativeString r = new RelativeString();
        r.addLink(link);
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch("abcde", r, true);
        assertEquals("xyzabcde", result);
    }

}

