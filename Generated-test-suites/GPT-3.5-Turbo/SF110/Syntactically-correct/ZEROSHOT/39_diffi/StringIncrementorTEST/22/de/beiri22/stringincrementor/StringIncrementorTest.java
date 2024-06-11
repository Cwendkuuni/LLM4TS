package de.beiri22.stringincrementor;

import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "abc");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "abcd");
        assertNotNull(result);
        assertEquals("d", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "ab");
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("", "abc");
        assertNotNull(result);
        assertEquals("abc", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testDiffWithVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "def", true);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "abc", true);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "abcd", true);
        assertNotNull(result);
        assertEquals("d", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "ab", true);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("", "abc", true);
        assertNotNull(result);
        assertEquals("abc", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        String result = StringIncrementor.patch("abc", new RelativeString());
        assertNotNull(result);
        assertEquals("abc", result);
        
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        r.addLink(new StringLink(0, 0, 3));
        result = StringIncrementor.patch("abc", r);
        assertNotNull(result);
        assertEquals("def", result);
    }
    
    @Test
    public void testPatchWithVerbose() {
        String result = StringIncrementor.patch("abc", new RelativeString(), true);
        assertNotNull(result);
        assertEquals("abc", result);
        
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        r.addLink(new StringLink(0, 0, 3));
        result = StringIncrementor.patch("abc", r, true);
        assertNotNull(result);
        assertEquals("def", result);
    }
}