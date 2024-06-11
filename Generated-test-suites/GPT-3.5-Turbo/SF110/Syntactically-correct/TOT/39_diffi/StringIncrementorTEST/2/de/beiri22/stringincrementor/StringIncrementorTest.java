import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    // Test cases for method diff(String, String)
    @Test
    public void testDiffWithSameStrings() {
        String a = "test";
        String b = "test";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testDiffWithDifferentStrings() {
        String a = "abc";
        String b = "def";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals(b, result.getAbsolute());
    }
    
    @Test
    public void testDiffWithSubstringOfStringBNotPresentInStringA() {
        String a = "abc";
        String b = "abcd";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals(b, result.getAbsolute());
    }
    
    @Test
    public void testDiffWithSubstringOfStringBPresentInStringA() {
        String a = "abcde";
        String b = "acde";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(1, result.linksCount());
        assertEquals("abcde", result.getLink(0).toString());
        assertEquals("acde", result.getLink(0).getSubString());
        assertEquals("", result.getAbsolute());
    }
    
    // Test cases for method diff(String, String, boolean)
    @Test
    public void testDiffVerboseModeWithSameStrings() {
        String a = "test";
        String b = "test";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void testDiffVerboseModeWithDifferentStrings() {
        String a = "abc";
        String b = "def";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertEquals(0, result.linksCount());
        assertEquals(b, result.getAbsolute());
    }
    
    @Test
    public void testDiffVerboseModeWithSubstringOfStringBNotPresentInStringA() {
        String a = "abc";
        String b = "abcd";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertEquals(0, result.linksCount());
        assertEquals(b, result.getAbsolute());
    }
    
    @Test
    public void testDiffVerboseModeWithSubstringOfStringBPresentInStringA() {
        String a = "abcde";
        String b = "acde";
        
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        assertEquals(1, result.linksCount());
        assertEquals("abcde", result.getLink(0).toString());
        assertEquals("acde", result.getLink(0).getSubString());
        assertEquals("", result.getAbsolute());
    }
    
    // Test cases for method patch(String, RelativeString)
    @Test
    public void testPatchWithNoRelativeLinks() {
        String a = "abc";
        RelativeString r = new RelativeString();
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testPatchWithRelativeLinks() {
        String a = "abc";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 1, 2);
        r.addLink(link);
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("ab", result);
    }
    
    // Test cases for method patch(String, RelativeString, boolean)
    @Test
    public void testPatchVerboseModeWithNoRelativeLinks() {
        String a = "abc";
        RelativeString r = new RelativeString();
        
        String result = StringIncrementor.patch(a, r, true);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testPatchVerboseModeWithRelativeLinks() {
        String a = "abc";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 1, 2);
        r.addLink(link);
        
        String result = StringIncrementor.patch(a, r, true);
        
        assertEquals("ab", result);
    }
}