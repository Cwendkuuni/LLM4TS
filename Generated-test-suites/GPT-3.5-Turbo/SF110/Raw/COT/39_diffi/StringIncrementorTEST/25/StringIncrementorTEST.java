import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: a and b are equal
        String a = "abc";
        String b = "abc";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(0, result.linksCount());
        
        // Test case 2: a is equal to a substring of b
        a = "abc";
        b = "xyzabc123";
        result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        assertEquals("abc", result.getLink(0).toString());
        
        // Test case 3: a is not present in b
        a = "abc";
        b = "xyz123";
        result = StringIncrementor.diff(a, b);
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffVerbose() {
        // Test case 1: a and b are equal
        String a = "abc";
        String b = "abc";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(0, result.linksCount());
        
        // Test case 2: a is equal to a substring of b
        a = "abc";
        b = "xyzabc123";
        result = StringIncrementor.diff(a, b, true);
        assertEquals(1, result.linksCount());
        assertEquals("abc", result.getLink(0).toString());
        
        // Test case 3: a is not present in b
        a = "abc";
        b = "xyz123";
        result = StringIncrementor.diff(a, b, true);
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch() {
        // Test case 1: RelativeString with no links
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
        
        // Test case 2: RelativeString with one link
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
        
        // Test case 3: RelativeString with multiple links
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 1, 1));
        r.addLink(new StringLink(1, 2, 1));
        r.addLink(new StringLink(2, 3, 1));
        result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }

    @Test
    public void testPatchVerbose() {
        // Test case 1: RelativeString with no links
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);
        
        // Test case 2: RelativeString with one link
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);
        
        // Test case 3: RelativeString with multiple links
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 1, 1));
        r.addLink(new StringLink(1, 2, 1));
        r.addLink(new StringLink(2, 3, 1));
        result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);
    }
}