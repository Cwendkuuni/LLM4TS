package de.beiri22.stringincrementor;

import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Test case 1: When verbose is false
        RelativeString result1 = StringIncrementor.diff("abc", "def");
        assertEquals("abc", result1.getAbsolute());
        
        // Test case 2: When verbose is true
        RelativeString result2 = StringIncrementor.diff("abc", "def", true);
        assertEquals("def", result2.getAbsolute());
        
        // Test case 3: When a and b have same characters
        RelativeString result3 = StringIncrementor.diff("abc", "abc");
        assertEquals("", result3.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        // Test case 1: When verbose is false
        String result1 = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", result1);
        
        // Test case 2: When verbose is true
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 2, 3));
        String result2 = StringIncrementor.patch("abc", relativeString, true);
        assertEquals("abc", result2);
        
        // Test case 3: When relativeString has no links
        String result3 = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", result3);
    }
}