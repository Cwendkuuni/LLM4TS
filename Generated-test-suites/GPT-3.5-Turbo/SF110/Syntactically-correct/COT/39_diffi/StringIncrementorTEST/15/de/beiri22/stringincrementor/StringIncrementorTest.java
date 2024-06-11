
import org.junit.Test;

import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcfgh";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(1, result.linksCount());
        assertEquals(2, result.getLink(0).getPosOrig());
        assertEquals(2, result.getLink(0).getPosNew());
        assertEquals(1, result.getLink(0).getLen());
    }
    
    @Test
    public void testDiffWithVerbose() {
        String a = "x";
        String b = "y";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertEquals(0, result.linksCount());
        assertEquals("y", result.getAbsolute());
    }
    
    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 2, 1));
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abedce", result);
    }
    
    @Test
    public void testPatchWithVerbose() {
        String a = "This is a test";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        r.addLink(new StringLink(5, 13, 9));
        
        String result = StringIncrementor.patch(a, r, true);
        
        assertEquals("ThisThis is a test test", result);
    }
}
