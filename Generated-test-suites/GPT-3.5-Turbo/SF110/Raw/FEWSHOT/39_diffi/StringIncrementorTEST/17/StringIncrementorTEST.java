
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abcd", "abc");
        assertEquals(1, result.linksCount());
        assertEquals("d", result.getAbsolute());
        
        result = StringIncrementor.diff("abc", "abcd");
        assertEquals(1, result.linksCount());
        assertEquals("", result.getAbsolute());
        
        result = StringIncrementor.diff("abc", "xyz");
        assertEquals(0, result.linksCount());
        assertEquals("xyz", result.getAbsolute());
        
        result = StringIncrementor.diff("abc", "abcd", true);
        assertEquals(1, result.linksCount());
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getPosNew());
        assertEquals(3, result.getLink(0).getLen());
    }
    
    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 3, 1));
        rs.setAbsolute("d");
        
        String result = StringIncrementor.patch("abc", rs);
        assertEquals("abcd", result);
        
        rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 0));
        rs.setAbsolute("xyz");
        
        result = StringIncrementor.patch("abc", rs);
        assertEquals("xyzabc", result);
        
        rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 0));
        rs.setAbsolute("");
        
        result = StringIncrementor.patch("abc", rs);
        assertEquals("abc", result);
        
        rs = new RelativeString();
        rs.addLink(new StringLink(1, 4, 2));
        rs.addLink(new StringLink(7, 5, 3));
        rs.setAbsolute("efg");
        
        result = StringIncrementor.patch("abcdhijk", rs);
        assertEquals("aefgbchijk", result);
        
        rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 1));
        rs.addLink(new StringLink(2, 0, 2));
        rs.addLink(new StringLink(5, 2, 1));
        rs.setAbsolute("lmnopq");
        
        result = StringIncrementor.patch("anbcdefg", rs);
        assertEquals("almnbcdofpgq", result);
    }
}
