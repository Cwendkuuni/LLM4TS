import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Test Case 1
        String a1 = "abcde";
        String b1 = "abcde";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        RelativeString expected1 = new RelativeString();
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcde";
        String b2 = "xyz";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        RelativeString expected2 = new RelativeString();
        expected2.setAbsolute("xyz");
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "abcdef";
        String b3 = "abx";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        RelativeString expected3 = new RelativeString();
        StringLink link1 = new StringLink(2, 0, 2);
        expected3.addLink(link1);
        expected3.setAbsolute("xf");
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcdefg";
        String b4 = "abxef";
        RelativeString result4 = StringIncrementor.diff(a4, b4);
        RelativeString expected4 = new RelativeString();
        StringLink link2 = new StringLink(2, 0, 2);
        StringLink link3 = new StringLink(4, 3, 2);
        expected4.addLink(link2);
        expected4.addLink(link3);
        expected4.setAbsolute("xf");
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testDiff_withVerbose() {
        // Test Case 1
        String a1 = "abcde";
        String b1 = "abcde";
        RelativeString result1 = StringIncrementor.diff(a1, b1, true);
        RelativeString expected1 = new RelativeString();
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcde";
        String b2 = "xyz";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        RelativeString expected2 = new RelativeString();
        expected2.setAbsolute("xyz");
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "abcdef";
        String b3 = "abx";
        RelativeString result3 = StringIncrementor.diff(a3, b3, true);
        RelativeString expected3 = new RelativeString();
        StringLink link1 = new StringLink(2, 0, 2);
        expected3.addLink(link1);
        expected3.setAbsolute("xf");
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcdefg";
        String b4 = "abxef";
        RelativeString result4 = StringIncrementor.diff(a4, b4, true);
        RelativeString expected4 = new RelativeString();
        StringLink link2 = new StringLink(2, 0, 2);
        StringLink link3 = new StringLink(4, 3, 2);
        expected4.addLink(link2);
        expected4.addLink(link3);
        expected4.setAbsolute("xf");
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testPatch() {
        // Test Case 1
        String a1 = "abcde";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1);
        String expected1 = "abcde";
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("xyz");
        String result2 = StringIncrementor.patch(a2, r2);
        String expected2 = "xyz";
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "abcdef";
        RelativeString r3 = new RelativeString();
        StringLink link1 = new StringLink(2, 0, 2);
        r3.addLink(link1);
        r3.setAbsolute("xf");
        String result3 = StringIncrementor.patch(a3, r3);
        String expected3 = "abxfdef";
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcdefg";
        RelativeString r4 = new RelativeString();
        StringLink link2 = new StringLink(2, 0, 2);
        StringLink link3 = new StringLink(4, 3, 2);
        r4.addLink(link2);
        r4.addLink(link3);
        r4.setAbsolute("xf");
        String result4 = StringIncrementor.patch(a4, r4);
        String expected4 = "abxfexfg";
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testPatch_withVerbose() {
        // Test Case 1
        String a1 = "abcde";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1, true);
        String expected1 = "abcde";
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        r2.setAbsolute("xyz");
        String result2 = StringIncrementor.patch(a2, r2, true);
        String expected2 = "xyz";
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "abcdef";
        RelativeString r3 = new RelativeString();
        StringLink link1 = new StringLink(2, 0, 2);
        r3.addLink(link1);
        r3.setAbsolute("xf");
        String result3 = StringIncrementor.patch(a3, r3, true);
        String expected3 = "abxfdef";
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcdefg";
        RelativeString r4 = new RelativeString();
        StringLink link2 = new StringLink(2, 0, 2);
        StringLink link3 = new StringLink(4, 3, 2);
        r4.addLink(link2);
        r4.addLink(link3);
        r4.setAbsolute("xf");
        String result4 = StringIncrementor.patch(a4, r4, true);
        String expected4 = "abxfexfg";
        assertEquals(expected4, result4);
    }
}