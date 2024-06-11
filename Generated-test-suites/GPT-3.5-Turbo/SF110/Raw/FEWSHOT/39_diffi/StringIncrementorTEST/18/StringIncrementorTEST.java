
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import java.io.*;
import org.junit.Test;

public class StringIncrementorTest {
  
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertEquals(result.getAbsolute(), "def");
        
        result = StringIncrementor.diff("abc", "abc");
        assertEquals(result.getAbsolute(), "");
        
        result = StringIncrementor.diff("abc", "abcdef");
        assertEquals(result.getAbsolute(), "def");
        
        result = StringIncrementor.diff("abc", "abcd");
        assertEquals(result.getAbsolute(), "d");
        
        result = StringIncrementor.diff("", "");
        assertEquals(result.getAbsolute(), "");
    }
    
    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "def", true);
        assertEquals(result.getAbsolute(), "def");
        
        result = StringIncrementor.diff("abc", "abc", true);
        assertEquals(result.getAbsolute(), "");
        
        result = StringIncrementor.diff("abc", "abcdef", true);
        assertEquals(result.getAbsolute(), "def");
        
        result = StringIncrementor.diff("abc", "abcd", true);
        assertEquals(result.getAbsolute(), "d");
        
        result = StringIncrementor.diff("", "", true);
        assertEquals(result.getAbsolute(), "");
    }
    
    @Test
    public void testPatch() {
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("def");
        
        String patchedString = StringIncrementor.patch("abc", relativeString);
        assertEquals(patchedString, "def");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 0));
        relativeString.setAbsolute("def");
        
        patchedString = StringIncrementor.patch("abc", relativeString);
        assertEquals(patchedString, "abc");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("");
        
        patchedString = StringIncrementor.patch("abc", relativeString);
        assertEquals(patchedString, "");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 1));
        relativeString.setAbsolute("d");
        
        patchedString = StringIncrementor.patch("abc", relativeString);
        assertEquals(patchedString, "d");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 0));
        relativeString.setAbsolute("");
        
        patchedString = StringIncrementor.patch("", relativeString);
        assertEquals(patchedString, "");
    }
    
    @Test
    public void testPatchVerbose() {
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("def");
        
        String patchedString = StringIncrementor.patch("abc", relativeString, true);
        assertEquals(patchedString, "def");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 0));
        relativeString.setAbsolute("def");
        
        patchedString = StringIncrementor.patch("abc", relativeString, true);
        assertEquals(patchedString, "abc");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("");
        
        patchedString = StringIncrementor.patch("abc", relativeString, true);
        assertEquals(patchedString, "");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 1));
        relativeString.setAbsolute("d");
        
        patchedString = StringIncrementor.patch("abc", relativeString, true);
        assertEquals(patchedString, "d");
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 0));
        relativeString.setAbsolute("");
        
        patchedString = StringIncrementor.patch("", relativeString, true);
        assertEquals(patchedString, "");
    }
}

