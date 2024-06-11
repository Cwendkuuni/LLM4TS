import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Typical use case
        RelativeString diffResult = StringIncrementor.diff("abcde", "abfgh");
        assertNotNull(diffResult);
        
        // Edge case: empty strings
        diffResult = StringIncrementor.diff("", "");
        assertNotNull(diffResult);
        
        // Error scenario: null input
        diffResult = StringIncrementor.diff(null, "abc");
        assertNull(diffResult);
    }
    
    @Test
    public void testPatch() {
        // Typical use case
        String patchedString = StringIncrementor.patch("abcde", new RelativeString());
        assertNotNull(patchedString);
        
        // Edge case: empty input string
        patchedString = StringIncrementor.patch("", new RelativeString());
        assertNotNull(patchedString);
        
        // Error scenario: null RelativeString
        patchedString = StringIncrementor.patch("abc", null);
        assertNull(patchedString);
    }
}