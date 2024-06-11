import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiffMethod_withEqualStrings() {
        String a = "abcde";
        String b = "abcde";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testDiffMethod_withDifferentStrings() {
        String a = "abcde";
        String b = "fghij";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertFalse(result.isEmpty());
        assertEquals(b, result.getAbsolute());
    }
    
    @Test
    public void testDiffMethod_withVerboseFalse() {
        String a = "abcde";
        String b = "aaabcdeaa";
        
        RelativeString result = StringIncrementor.diff(a, b, false);
        
        assertFalse(result.isEmpty());
        assertEquals(b, result.getAbsolute());
        assertNull(result.getLink(0));
    }
    
    @Test
    public void testDiffMethod_withVerboseTrue() {
        String a = "abcde";
        String b = "aaabcdeaa";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertFalse(result.isEmpty());
        assertEquals(b, result.getAbsolute());
        assertNotNull(result.getLink(0));
        assertEquals(2, result.linksCount());
    }
    
    @Test
    public void testDiffMethod_withLongString() {
        String a = "abcde";
        String b = "fghijklmnopqrstuvwxyzabcde";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertFalse(result.isEmpty());
        assertNotEquals(b, result.getAbsolute());
        assertNotNull(result.getLink(0));
    }
    
    @Test
    public void testPatchMethod_withEmptyRelativeString() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testPatchMethod_withNonEmptyRelativeString() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 2, 3);
        r.addLink(link);
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abcabcde", result);
    }
    
    @Test
    public void testPatchMethod_withVerboseTrue() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 2, 3);
        r.addLink(link);
        
        String result = StringIncrementor.patch(a, r, true);
        
        assertEquals("abcabcde", result);
    }
}