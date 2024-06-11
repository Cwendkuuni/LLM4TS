import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // typical use case
        String a = "abcde";
        String b = "xyzabcde";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        
        // edge case
        a = "";
        b = "xyzabcde";
        result = StringIncrementor.diff(a, b);
        assertEquals(0, result.linksCount());
        
        // error scenario
        a = "abcde";
        b = "";
        result = StringIncrementor.diff(a, b);
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        // typical use case
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 3, 2));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abbcde", result);
        
        // edge case
        a = "";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 1));
        result = StringIncrementor.patch(a, r);
        assertEquals("a", result);
        
        // error scenario
        a = "abcde";
        r = new RelativeString();
        result = StringIncrementor.patch(a, r);
        assertEquals("abcde", result);
    }
}