import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {
    
    // Test diff method with basic inputs
    @Test
    public void testDiffBasic() {
        RelativeString result = StringIncrementor.diff("abcdefg", "abcdefg");
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    // Test diff method with verbose set to true
    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abcdefg", "abcyzfg", true);
        assertEquals("yz", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(2, 4, 5), result.getLink(0));
    }
    
    // Test patch method with basic inputs
    @Test
    public void testPatchBasic() {
        String result = StringIncrementor.patch("abcdefg", new RelativeString());
        assertEquals("abcdefg", result);
    }
    
    // Test patch method with verbose set to true
    @Test
    public void testPatchVerbose() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 4, 5));
        String result = StringIncrementor.patch("abcdefg", r, true);
        assertEquals("abcfghijefg", result);
    }
    
    // Test diff method with edge case of empty string
    @Test
    public void testDiffEmptyString() {
        RelativeString result = StringIncrementor.diff("", "");
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }
    
    // Test patch method with edge case of empty string
    @Test
    public void testPatchEmptyString() {
        String result = StringIncrementor.patch("", new RelativeString());
        assertEquals("", result);
    }
    
    // Test diff method with one string longer than the other
    @Test
    public void testDiffDifferentLengths() {
        RelativeString result = StringIncrementor.diff("abcd", "abcdefg");
        assertEquals("efg", result.getAbsolute());
        assertEquals(1, result.linksCount());
        assertEquals(new StringLink(0, 4, 3), result.getLink(0));
    }
    
    // Test patch method with one string longer than the other
    @Test
    public void testPatchDifferentLengths() {
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 4, 3));
        String result = StringIncrementor.patch("abcd", r);
        assertEquals("abcdefg", result);
    }
    
    // Test diff method with exception handling for invalid input
    @Test(expected = NullPointerException.class)
    public void testDiffInvalidInput() {
        RelativeString result = StringIncrementor.diff(null, "abcdefg");
    }
    
    // Test patch method with exception handling for invalid input
    @Test(expected = NullPointerException.class)
    public void testPatchInvalidInput() {
        String result = StringIncrementor.patch(null, new RelativeString());
    }
}

