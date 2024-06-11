import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "abc");
        assertTrue(result.isEmpty());
        
        result = StringIncrementor.diff("abc", "def");
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "abc", true);
        assertTrue(result.isEmpty());
        
        result = StringIncrementor.diff("abc", "def", true);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testPatch() {
        String patch = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", patch);
        
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        patch = StringIncrementor.patch("abc", r);
        assertEquals("abc", patch);
        
        r.addLink(new StringLink(0, 0, 3));
        patch = StringIncrementor.patch("abc", r);
        assertEquals("abcabc", patch);
    }
    
    @Test
    public void testPatchVerbose() {
        String patch = StringIncrementor.patch("abc", new RelativeString(), true);
        assertEquals("abc", patch);
        
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        patch = StringIncrementor.patch("abc", r, true);
        assertEquals("abc", patch);
        
        r.addLink(new StringLink(0, 0, 3));
        patch = StringIncrementor.patch("abc", r, true);
        assertEquals("abcabc", patch);
    }
}