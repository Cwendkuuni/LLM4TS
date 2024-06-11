import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Basic test case
        String a = "abcde";
        String b = "bcdef";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(1, result.linksCount());
        
        // Edge case where a and b are identical
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(0, result.linksCount());
        
        // Exception handling scenario where b is empty
        a = "abcde";
        b = "";
        result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        // Basic test case
        String a = "abcde";
        RelativeString r = new RelativeString();
        String patchedString = StringIncrementor.patch(a, r);
        assertNotNull(patchedString);
        assertEquals("abcde", patchedString);
        
        // Edge case where r is empty
        a = "abcde";
        r = new RelativeString();
        patchedString = StringIncrementor.patch(a, r);
        assertNotNull(patchedString);
        assertEquals("abcde", patchedString);
        
        // Exception handling scenario where a is empty
        a = "";
        r = new RelativeString();
        patchedString = StringIncrementor.patch(a, r);
        assertNotNull(patchedString);
        assertEquals("", patchedString);
    }
}