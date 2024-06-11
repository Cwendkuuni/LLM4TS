import static org.junit.Assert.*;
import org.junit.*;

public class StringIncrementorTest {
    
    // Test cases for diff(String a, String b) method
    
    @Test
    public void testDiff_StringsDifferent_ReturnsRelativeString() {
        String a = "abcd";
        String b = "efgh";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_StringALargerThanB_ReturnsRelativeStringWithRest() {
        String a = "abcdefgh";
        String b = "abcd";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("efgh");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_StringBSmallerThan13Characters_ReturnsRelativeStringWithRest() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcd";
        RelativeString expected = new RelativeString();
        expected.setAbsolute("efghijklmnopqrstuvwxyz");
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    // Test cases for diff(String a, String b, boolean verbose) method
    
    @Test
    public void testDiff_StringsDifferentVerbose_ReturnsRelativeString() {
        String a = "abcd";
        String b = "efgh";
        boolean verbose = true;
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_StringALargerThanBVerbose_ReturnsRelativeStringWithRest() {
        String a = "abcdefgh";
        String b = "abcd";
        boolean verbose = true;
        RelativeString expected = new RelativeString();
        expected.setAbsolute("efgh");
        
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_StringBSmallerThan13CharactersVerbose_ReturnsRelativeStringWithRest() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcd";
        boolean verbose = true;
        RelativeString expected = new RelativeString();
        expected.setAbsolute("efghijklmnopqrstuvwxyz");
        
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        
        assertEquals(expected, result);
    }
    
    // Test cases for patch(String a, RelativeString r) method
    
    @Test
    public void testPatch_RelativeStringEmpty_ReturnsPatchedString() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        String expected = "abcd";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_RelativeStringNotEmpty_ReturnsPatchedString() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.setAbsolute("efgh");
        r.addLink(new StringLink(0, 0, 2));
        String expected = "abefcdgh";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    // Test cases for patch(String a, RelativeString r, boolean verbose) method
    
    @Test
    public void testPatch_RelativeStringEmptyVerbose_ReturnsPatchedString() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        boolean verbose = true;
        String expected = "abcd";
        
        String result = StringIncrementor.patch(a, r, verbose);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_RelativeStringNotEmptyVerbose_ReturnsPatchedString() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.setAbsolute("efgh");
        r.addLink(new StringLink(0, 0, 2));
        boolean verbose = true;
        String expected = "abefcdgh";
        
        String result = StringIncrementor.patch(a, r, verbose);
        
        assertEquals(expected, result);
    }
}