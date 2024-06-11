
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("", "");
        assertNotNull(result);
        assertEquals(0, result.getLength());
        assertEquals("", result.getAbsolute());
        
        result = StringIncrementor.diff("", "test");
        assertNotNull(result);
        assertEquals(0, result.getLength());
        assertEquals("test", result.getAbsolute());
        
        result = StringIncrementor.diff("test", "");
        assertNotNull(result);
        assertEquals(4, result.getLength());
        assertEquals("", result.getAbsolute());
        
        result = StringIncrementor.diff("test", "test");
        assertNotNull(result);
        assertEquals(0, result.getLength());
        assertEquals("", result.getAbsolute());
        
        result = StringIncrementor.diff("hello", "hella");
        assertNotNull(result);
        assertEquals(1, result.getLength());
        assertEquals("a", result.getAbsolute());
        
        result = StringIncrementor.diff("hello", "hellish");
        assertNotNull(result);
        assertEquals(2, result.getLength());
        assertEquals("ish", result.getAbsolute());
        
        result = StringIncrementor.diff("hello", "hein");
        assertNotNull(result);
        assertEquals(3, result.getLength());
        assertEquals("in", result.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        String result = StringIncrementor.patch("", new RelativeString());
        assertNotNull(result);
        assertEquals("", result);
        
        RelativeString r = new RelativeString();
        r.setAbsolute("test");
        result = StringIncrementor.patch("", r);
        assertNotNull(result);
        assertEquals("test", result);
        
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.patch("", r);
        assertNotNull(result);
        assertEquals("test", result);
        
        r = new RelativeString();
        r.setAbsolute("test");
        r.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.patch("abcd", r);
        assertNotNull(result);
        assertEquals("testabcd", result);
        
        r = new RelativeString();
        r.setAbsolute("ish");
        r.addLink(new StringLink(0, 4, 2));
        result = StringIncrementor.patch("hell", r);
        assertNotNull(result);
        assertEquals("hellish", result);
        
        r = new RelativeString();
        r.setAbsolute("in");
        r.addLink(new StringLink(0, 4, 3));
        result = StringIncrementor.patch("hell", r);
        assertNotNull(result);
        assertEquals("hein", result);
    }
}
