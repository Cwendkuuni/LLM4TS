package de.beiri22.stringincrementor;

import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.relativestring.RelativeString;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1
        String a1 = "abcde";
        String b1 = "abcde";
        RelativeString diff1 = StringIncrementor.diff(a1, b1);
        assertEquals(0, diff1.linksCount());
        assertEquals(a1, StringIncrementor.patch(b1, diff1));
        
        // Test case 2
        String a2 = "abcde";
        String b2 = "fghij";
        RelativeString diff2 = StringIncrementor.diff(a2, b2);
        assertEquals(0, diff2.linksCount());
        assertEquals(a2, StringIncrementor.patch(b2, diff2));
        
        // Test case 3
        String a3 = "abcde";
        String b3 = "abcdexyz";
        RelativeString diff3 = StringIncrementor.diff(a3, b3);
        assertEquals(1, diff3.linksCount());
        assertEquals(a3, StringIncrementor.patch(b3, diff3));
        
        // Test case 4
        String a4 = "abcde";
        String b4 = "axyz";
        RelativeString diff4 = StringIncrementor.diff(a4, b4);
        assertEquals(1, diff4.linksCount());
        assertEquals(a4, StringIncrementor.patch(b4, diff4));
    }

    @Test
    public void testPatch() {
        // Test case 1
        String a1 = "abcde";
        RelativeString r1 = new RelativeString();
        assertEquals(a1, StringIncrementor.patch(a1, r1));
        
        // Test case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(0, 0, 5));
        assertEquals(a2, StringIncrementor.patch(a2, r2));
        
        // Test case 3
        String a3 = "abcde";
        RelativeString r3 = new RelativeString();
        r3.setAbsolute("xyz");
        assertEquals("xyz", StringIncrementor.patch(a3, r3));
        
        // Test case 4
        String a4 = "abcde";
        RelativeString r4 = new RelativeString();
        r4.addLink(new StringLink(0, 0, 2));
        r4.setAbsolute("xyz");
        assertEquals("xyabcze", StringIncrementor.patch(a4, r4));
    }
}