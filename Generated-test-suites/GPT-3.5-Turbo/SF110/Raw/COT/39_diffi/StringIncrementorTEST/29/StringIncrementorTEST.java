import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcdef";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        assertEquals(expected, StringIncrementor.diff(a, b));
        
        a = "abcde";
        b = "abcde";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }
    
    @Test
    public void testDiffWithVerbose() {
        String a = "abcde";
        String b = "abcdef";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        assertEquals(expected, StringIncrementor.diff(a, b, true));
        
        a = "abcde";
        b = "abcde";
        expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b, true));
    }
    
    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        String expected = "abcdef";
        assertEquals(expected, StringIncrementor.patch(a, r));
        
        a = "abcde";
        r = new RelativeString();
        expected = "abcde";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }
    
    @Test
    public void testPatchWithVerbose() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        String expected = "abcdef";
        assertEquals(expected, StringIncrementor.patch(a, r, true));
        
        a = "abcde";
        r = new RelativeString();
        expected = "abcde";
        assertEquals(expected, StringIncrementor.patch(a, r, true));
    }
    
}