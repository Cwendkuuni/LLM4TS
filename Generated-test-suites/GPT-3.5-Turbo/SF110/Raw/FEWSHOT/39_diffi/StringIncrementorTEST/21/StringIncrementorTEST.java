import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Test with verbose = false
        RelativeString result1 = StringIncrementor.diff("abc", "def");
        assertEquals(0, result1.linksCount());
        assertEquals("def", result1.getAbsolute());
        
        RelativeString result2 = StringIncrementor.diff("abc", "abc");
        assertEquals(0, result2.linksCount());
        assertEquals("", result2.getAbsolute());
        
        RelativeString result3 = StringIncrementor.diff("abc", "abcdef");
        assertEquals(1, result3.linksCount());
        assertEquals("def", result3.getAbsolute());
        assertEquals(0, result3.getLink(0).getPosOrig());
        assertEquals(3, result3.getLink(0).getPosNew());
        assertEquals(3, result3.getLink(0).getLen());
        
        RelativeString result4 = StringIncrementor.diff("abc", "abca");
        assertEquals(1, result4.linksCount());
        assertEquals("", result4.getAbsolute());
        assertEquals(3, result4.getLink(0).getPosOrig());
        assertEquals(3, result4.getLink(0).getPosNew());
        assertEquals(0, result4.getLink(0).getLen());
        
        // Test with verbose = true
        RelativeString result5 = StringIncrementor.diff("abc", "def", true);
        assertEquals(0, result5.linksCount());
        assertEquals("def", result5.getAbsolute());
        
        RelativeString result6 = StringIncrementor.diff("abc", "abc", true);
        assertEquals(0, result6.linksCount());
        assertEquals("", result6.getAbsolute());
        
        RelativeString result7 = StringIncrementor.diff("abc", "abcdef", true);
        assertEquals(1, result7.linksCount());
        assertEquals("def", result7.getAbsolute());
        assertEquals(0, result7.getLink(0).getPosOrig());
        assertEquals(3, result7.getLink(0).getPosNew());
        assertEquals(3, result7.getLink(0).getLen());
        
        RelativeString result8 = StringIncrementor.diff("abc", "abca", true);
        assertEquals(1, result8.linksCount());
        assertEquals("", result8.getAbsolute());
        assertEquals(3, result8.getLink(0).getPosOrig());
        assertEquals(3, result8.getLink(0).getPosNew());
        assertEquals(0, result8.getLink(0).getLen());
    }
    
    @Test
    public void testPatch() {
        // Test with verbose = false
        String result1 = StringIncrementor.patch("abc", new RelativeString(), false);
        assertEquals("abc", result1);
        
        String result2 = StringIncrementor.patch("abc", new RelativeString("def"), false);
        assertEquals("abcdef", result2);
        
        StringLink link1 = new StringLink(0, 0, 3);
        StringLink link2 = new StringLink(3, 3, 0);
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(link1);
        relativeString.addLink(link2);
        
        String result3 = StringIncrementor.patch("abc", relativeString, false);
        assertEquals("abc", result3);
        
        // Test with verbose = true
        String result4 = StringIncrementor.patch("abc", new RelativeString(), true);
        assertEquals("abc", result4);
        
        String result5 = StringIncrementor.patch("abc", new RelativeString("def"), true);
        assertEquals("abcdef", result5);
        
        String result6 = StringIncrementor.patch("abc", relativeString, true);
        assertEquals("abc", result6);
    }
}