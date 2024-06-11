
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Both strings are empty
        RelativeString result1 = StringIncrementor.diff("", "", false);
        assertEquals("", result1.getAbsolute());
        assertEquals(0, result1.linksCount());

        // Test case 2: One string is empty, other is not
        RelativeString result2 = StringIncrementor.diff("", "abc", false);
        assertEquals("abc", result2.getAbsolute());
        assertEquals(0, result2.linksCount());

        // Test case 3: Both strings have the same content
        RelativeString result3 = StringIncrementor.diff("abc", "abc", false);
        assertEquals("", result3.getAbsolute());
        assertEquals(0, result3.linksCount());

        // Test case 4: Different strings with no common substring
        RelativeString result4 = StringIncrementor.diff("abc", "def", false);
        assertEquals("def", result4.getAbsolute());
        assertEquals(0, result4.linksCount());

        // Test case 5: Different strings with common substring
        RelativeString result5 = StringIncrementor.diff("abcde", "defgh", false);
        assertEquals("gh", result5.getAbsolute());
        assertEquals(1, result5.linksCount());
        assertNotNull(result5.getLink(0));
        assertEquals(0, result5.getLink(0).getPosOrig());
        assertEquals(3, result5.getLink(0).getPosNew());
        assertEquals(3, result5.getLink(0).getLen());

        // Test case 6: Verbose mode enabled
        RelativeString result6 = StringIncrementor.diff("abc", "def", true);
        assertEquals("def", result6.getAbsolute());
        assertEquals(0, result6.linksCount());
    }

    @Test
    public void testPatch() {
        // Test case 1: Patch with empty RelativeString
        String result1 = StringIncrementor.patch("abc", new RelativeString(), false);
        assertEquals("abc", result1);

        // Test case 2: Patch with RelativeString containing links
        RelativeString relativeString = new RelativeString();
        StringLink link = new StringLink(0, 3, 3);
        relativeString.addLink(link);
        String result2 = StringIncrementor.patch("abxyz", relativeString, false);
        assertEquals("abcxyz", result2);

        // Test case 3: Patch with RelativeString containing links and absolute string
        RelativeString relativeString2 = new RelativeString();
        StringLink link2 = new StringLink(0, 3, 3);
        relativeString2.addLink(link2);
        relativeString2.setAbsolute("def");
        String result3 = StringIncrementor.patch("abxyz", relativeString2, false);
        assertEquals("defcxyz", result3);

        // Test case 4: Verbose mode enabled
        RelativeString relativeString3 = new RelativeString();
        StringLink link3 = new StringLink(0, 3, 3);
        relativeString3.addLink(link3);
        String result4 = StringIncrementor.patch("abxyz", relativeString3, true);
        assertEquals("abcxyz", result4);
    }
}
