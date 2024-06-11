
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abcdefg", "abcdfg");
        assertEquals("abcdefg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(3, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getPosNew());
        assertEquals(1, result.getLink(0).getLen());

        result = StringIncrementor.diff("abcdefg", "bcd");
        assertEquals("aefg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(1, result.getLink(0).getLen());

        result = StringIncrementor.diff("abcdefg", "hijklmnopqrs");
        assertEquals("abcdefgh", result.getAbsolute());
        assertEquals(0, result.linksCount());

        result = StringIncrementor.diff("", "");
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        }

    @Test
    public void testPatch() {
        RelativeString r = new RelativeString();
        r.setAbsolute("abcdefg");
        String result = StringIncrementor.patch("abcdfg", r);
        assertEquals("abcdefg", result);

        r = new RelativeString();
        r.setAbsolute("aefg");
        Link link = new Link(0, 0, 1);
        r.addLink(link);
        result = StringIncrementor.patch("bcd", r);
        assertEquals("abcdefg", result);

        r = new RelativeString();
        r.setAbsolute("abcdefgh");
        result = StringIncrementor.patch("abcdefg", r);
        assertEquals("hijklmnopqrs", result);

        r = new RelativeString();
        r.setAbsolute("");
        result = StringIncrementor.patch("", r);
        assertEquals("", result);
    }
}
