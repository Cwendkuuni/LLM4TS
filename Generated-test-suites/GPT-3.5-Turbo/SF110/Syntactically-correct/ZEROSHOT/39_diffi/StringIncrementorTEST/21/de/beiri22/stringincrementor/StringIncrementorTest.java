import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Test case 1
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(-1, 0, 3));
        assertEquals(expected1, result1);
        
        // Test case 2
        String a2 = "aaaaa";
        String b2 = "aaaadef";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        RelativeString expected2 = new RelativeString();
        expected2.addLink(new StringLink(-1, 0, 5));
        expected2.addLink(new StringLink(-1, 5, 3));
        assertEquals(expected2, result2);
        
        // Test case 3
        String a3 = "abcd";
        String b3 = "abefcd";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        RelativeString expected3 = new RelativeString();
        expected3.addLink(new StringLink(0, 0, 2));
        expected3.addLink(new StringLink(-1, 2, 2));
        expected3.addLink(new StringLink(2, 4, 2));
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testDiffVerbose() {
        // Test case 1
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1, true);
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(-1, 0, 3));
        assertEquals(expected1, result1);
        
        // Test case 2
        String a2 = "aaaaa";
        String b2 = "aaaadef";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        RelativeString expected2 = new RelativeString();
        expected2.addLink(new StringLink(-1, 0, 5));
        expected2.addLink(new StringLink(-1, 5, 3));
        assertEquals(expected2, result2);
        
        // Test case 3
        String a3 = "abcd";
        String b3 = "abefcd";
        RelativeString result3 = StringIncrementor.diff(a3, b3, true);
        RelativeString expected3 = new RelativeString();
        expected3.addLink(new StringLink(0, 0, 2));
        expected3.addLink(new StringLink(-1, 2, 2));
        expected3.addLink(new StringLink(2, 4, 2));
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testPatch() {
        // Test case 1
        String a1 = "abc";
        RelativeString r1 = new RelativeString();
        r1.addLink(new StringLink(-1, 0, 3));
        String result1 = StringIncrementor.patch(a1, r1);
        String expected1 = "abc";
        assertEquals(expected1, result1);
        
        // Test case 2
        String a2 = "aaaaa";
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(-1, 0, 5));
        r2.addLink(new StringLink(-1, 5, 3));
        String result2 = StringIncrementor.patch(a2, r2);
        String expected2 = "aaaadaaa";
        assertEquals(expected2, result2);
        
        // Test case 3
        String a3 = "abcd";
        RelativeString r3 = new RelativeString();
        r3.addLink(new StringLink(0, 0, 2));
        r3.addLink(new StringLink(-1, 2, 2));
        r3.addLink(new StringLink(2, 4, 2));
        String result3 = StringIncrementor.patch(a3, r3);
        String expected3 = "abababcdcd";
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testPatchVerbose() {
        // Test case 1
        String a1 = "abc";
        RelativeString r1 = new RelativeString();
        r1.addLink(new StringLink(-1, 0, 3));
        String result1 = StringIncrementor.patch(a1, r1, true);
        String expected1 = "abc";
        assertEquals(expected1, result1);
        
        // Test case 2
        String a2 = "aaaaa";
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(-1, 0, 5));
        r2.addLink(new StringLink(-1, 5, 3));
        String result2 = StringIncrementor.patch(a2, r2, true);
        String expected2 = "aaaadaaa";
        assertEquals(expected2, result2);
        
        // Test case 3
        String a3 = "abcd";
        RelativeString r3 = new RelativeString();
        r3.addLink(new StringLink(0, 0, 2));
        r3.addLink(new StringLink(-1, 2, 2));
        r3.addLink(new StringLink(2, 4, 2));
        String result3 = StringIncrementor.patch(a3, r3, true);
        String expected3 = "abababcdcd";
        assertEquals(expected3, result3);
    }
}