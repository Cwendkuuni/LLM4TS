import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Basic test case
        RelativeString result = StringIncrementor.diff("abc", "abc");
        assertEquals(0, result.linksCount());
        
        // Edge case: empty strings
        result = StringIncrementor.diff("", "");
        assertEquals(0, result.linksCount());
        
        // Exception handling: null input
        try {
            StringIncrementor.diff(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testPatch() {
        // Basic test case
        RelativeString rs = new RelativeString();
        String patched = StringIncrementor.patch("abc", rs);
        assertEquals("abc", patched);
        
        // Edge case: empty RelativeString
        rs.setAbsolute("");
        patched = StringIncrementor.patch("abc", rs);
        assertEquals("abc", patched);
        
        // Exception handling: null input
        try {
            StringIncrementor.patch(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}