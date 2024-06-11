package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals("xyz", result.getAbsolute());
        
        a = "abcde";
        b = "xyzabc";
        result = StringIncrementor.diff(a, b);
        assertEquals("xyz", result.getAbsolute());
        
        a = "abcde";
        b = "abcdef";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testDiffWithVerbose() {
        String a = "abcde";
        String b = "abcxyz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals("xyz", result.getAbsolute());
        
        a = "abcde";
        b = "xyzabc";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("xyz", result.getAbsolute());
        
        a = "abcde";
        b = "abcdef";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("", result.getAbsolute());
        
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch(a, r);
        assertEquals("xyzde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(2, 4, 2));
        r.setAbsolute("xyz");
        result = StringIncrementor.patch(a, r);
        assertEquals("abxycde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.setAbsolute("xyz");
        result = StringIncrementor.patch(a, r);
        assertEquals("xyzbcde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 4, 5));
        r.setAbsolute("");
        result = StringIncrementor.patch(a, r);
        assertEquals("abcde", result);
    }
    
    @Test
    public void testPatchWithVerbose() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        r.setAbsolute("xyz");
        String result = StringIncrementor.patch(a, r, true);
        assertEquals("xyzde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(2, 4, 2));
        r.setAbsolute("xyz");
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abxycde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        r.setAbsolute("xyz");
        result = StringIncrementor.patch(a, r, true);
        assertEquals("xyzbcde", result);
        
        a = "abcde";
        r = new RelativeString();
        r.addLink(new StringLink(0, 4, 5));
        r.setAbsolute("");
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abcde", result);
    }
}