
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.relativestring.RelativeString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString diff = StringIncrementor.diff("abcde", "abcddefer");
        assertEquals(2, diff.linksCount());
        assertEquals("e", diff.getAbsolute());

        diff = StringIncrementor.diff("abcdefg", "abcdefgefghijk");
        assertEquals(2, diff.linksCount());
        assertEquals("efghijk", diff.getAbsolute());

        diff = StringIncrementor.diff("aabbaacc", "aabbcc");
        assertEquals(1, diff.linksCount());
        assertEquals("cc", diff.getAbsolute());

        diff = StringIncrementor.diff("abc", "abc");
        assertEquals(0, diff.linksCount());
        assertEquals("", diff.getAbsolute());
    }

    @Test
    public void testDiffVerbose() {
        RelativeString diff = StringIncrementor.diff("abcde", "abcddefer", true);
        assertEquals(2, diff.linksCount());
        assertEquals("e", diff.getAbsolute());

        diff = StringIncrementor.diff("abcdefg", "abcdefgefghijk", true);
        assertEquals(2, diff.linksCount());
        assertEquals("efghijk", diff.getAbsolute());

        diff = StringIncrementor.diff("aabbaacc", "aabbcc", true);
        assertEquals(1, diff.linksCount());
        assertEquals("cc", diff.getAbsolute());

        diff = StringIncrementor.diff("abc", "abc", true);
        assertEquals(0, diff.linksCount());
        assertEquals("", diff.getAbsolute());
    }

    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(2, 8, 3));
        rs.setAbsolute("efg");

        String patch = StringIncrementor.patch("abcddefer", rs);
        assertEquals("abefgdef", patch);

        rs = new RelativeString();
        rs.addLink(new StringLink(0, 4, 4));
        rs.addLink(new StringLink(7, 7, 1));
        rs.setAbsolute("ghjkl");

        patch = StringIncrementor.patch("abcdefgefghijk", rs);
        assertEquals("ghjklabcdefjhijk", patch);

        rs = new RelativeString();
        rs.addLink(new StringLink(4, 5, 1));
        rs.setAbsolute("");

        patch = StringIncrementor.patch("aabbcc", rs);
        assertEquals("aabbcc", patch);

        rs = new RelativeString();
        rs.setAbsolute("");

        patch = StringIncrementor.patch("abc", rs);
        assertEquals("abc", patch);
    }

    @Test
    public void testPatchVerbose() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(2, 8, 3));
        rs.setAbsolute("efg");

        String patch = StringIncrementor.patch("abcddefer", rs, true);
        assertEquals("abefgdef", patch);

        rs = new RelativeString();
        rs.addLink(new StringLink(0, 4, 4));
        rs.addLink(new StringLink(7, 7, 1));
        rs.setAbsolute("ghjkl");

        patch = StringIncrementor.patch("abcdefgefghijk", rs, true);
        assertEquals("ghjklabcdefjhijk", patch);

        rs = new RelativeString();
        rs.addLink(new StringLink(4, 5, 1));
        rs.setAbsolute("");

        patch = StringIncrementor.patch("aabbcc", rs, true);
        assertEquals("aabbcc", patch);

        rs = new RelativeString();
        rs.setAbsolute("");

        patch = StringIncrementor.patch("abc", rs, true);
        assertEquals("abc", patch);
    }
}
