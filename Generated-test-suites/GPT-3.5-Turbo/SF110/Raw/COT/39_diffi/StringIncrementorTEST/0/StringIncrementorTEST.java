
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: Test diff method with non-verbose input
        String a1 = "abcd";
        String b1 = "abcde";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        assertEquals(1, result1.linksCount());
        
        // Test case 2: Test diff method with verbose input
        String a2 = "abcd";
        String b2 = "abcde";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        assertEquals(1, result2.linksCount());
        
        // Test case 3: Test diff method with no common characters
        String a3 = "abcd";
        String b3 = "efgh";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        assertEquals(0, result3.linksCount());
    }

    @Test
    public void testPatch() {
        // Test case 1: Test patch method with non-verbose input
        String a1 = "abcd";
        RelativeString r1 = new RelativeString();
        r1.addLink(new StringLink(0, 0, 1));
        String result1 = StringIncrementor.patch(a1, r1);
        assertEquals("aabcd", result1);
        
        // Test case 2: Test patch method with verbose input
        String a2 = "abcd";
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(0, 0, 1));
        String result2 = StringIncrementor.patch(a2, r2, true);
        assertEquals("aabcd", result2);
    }
}

