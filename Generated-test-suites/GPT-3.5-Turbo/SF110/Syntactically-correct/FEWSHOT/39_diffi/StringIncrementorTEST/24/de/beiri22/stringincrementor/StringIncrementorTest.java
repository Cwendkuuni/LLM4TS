
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("", "");
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "");
        assertEquals("abc", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("", "abc");
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        result = StringIncrementor.diff("abc", "abc");
        assertEquals("", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        
        result = StringIncrementor.diff("abc", "def");
        assertEquals("def", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        
        result = StringIncrementor.diff("abc", "defg");
        assertEquals("defg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        
        result = StringIncrementor.diff("abc", "defgh");
        assertEquals("defgh", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        
        result = StringIncrementor.diff("abc", "abcdefgh");
        assertEquals("defgh", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(3, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        
        result = StringIncrementor.diff("abc", "abdefg");
        assertEquals("abdefg", result.getAbsolute());
        assertEquals(2, result.linksCount());
        assertEquals(2, result.getLink(0).getPosNew());
        assertEquals(2, result.getLink(0).getPosOrig());
        assertEquals(1, result.getLink(0).getLen());
        assertEquals(4, result.getLink(1).getPosNew());
        assertEquals(3, result.getLink(1).getPosOrig());
        assertEquals(2, result.getLink(1).getLen());
        
        result = StringIncrementor.diff("abc", "abcabcabc");
        assertEquals("abc", result.getAbsolute());
        assertEquals(3, result.linksCount());
        assertEquals(0, result.getLink(0).getPosNew());
        assertEquals(0, result.getLink(0).getPosOrig());
        assertEquals(3, result.getLink(0).getLen());
        assertEquals(3, result.getLink(1).getPosNew());
        assertEquals(3, result.getLink(1).getPosOrig());
        assertEquals(3, result.getLink(1).getLen());
        assertEquals(6, result.getLink(2).getPosNew());
        assertEquals(6, result.getLink(2).getPosOrig());
        assertEquals(3, result.getLink(2).getLen());
    }
    
    @Test
    public void testPatch() {
        String result = StringIncrementor.patch("", new RelativeString());
        assertEquals("", result);
        
        result = StringIncrementor.patch("", new RelativeString("abc"));
        assertEquals("abc", result);
        
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        result = StringIncrementor.patch("", relativeString);
        assertEquals("abc", result);
        
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.addLink(new StringLink(3, 3, 3));
        result = StringIncrementor.patch("", relativeString);
        assertEquals("abcabc", result);
        
        relativeString = new RelativeString("def");
        relativeString.addLink(new StringLink(0, 0, 3));
        result = StringIncrementor.patch("abc", relativeString);
        assertEquals("abcabc", result);
    }
}
