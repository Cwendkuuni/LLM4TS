
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abc";
        String b = "def";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        a = "abc";
        b = "abc";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        a = "abc";
        b = "abcd";
        result = StringIncrementor.diff(a, b);
        assertEquals("d", result.getAbsolute());
        assertEquals(0, result.linksCount());

        a = "abcd";
        b = "a";
        result = StringIncrementor.diff(a, b);
        assertEquals("bcd", result.getAbsolute());
        assertEquals(0, result.linksCount());

        a = "abcd";
        b = "efgabcdhij";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(4, 3, 4), result.getLink(0));

        a = "abcd";
        b = "abcdef";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(0, 0, 4), result.getLink(0));

        a = "abcd";
        b = "efgabcdefgh";
        result = StringIncrementor.diff(a, b);
        assertEquals("efg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(4, 3, 4), result.getLink(0));
        
        a = "abcd";
        b = "efgabcdijklmnop";
        result = StringIncrementor.diff(a, b);
        assertEquals("ijklmnop", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(4, 3, 4), result.getLink(0));
    }
    
    @Test
    public void testPatch() {
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("abc", result);

        a = "abc";
        r = new RelativeString();
        r.setAbsolute("def");
        r.addLink(new StringLink(0, 3, 3));
        result = StringIncrementor.patch(a, r);
        assertEquals("def", result);

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        r.setAbsolute("def");
        result = StringIncrementor.patch(a, r);
        assertEquals("defabc", result);

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.addLink(new StringLink(2, 2, 1));
        r.setAbsolute("def");
        result = StringIncrementor.patch(a, r);
        assertEquals("dacf", result);
    }
}
