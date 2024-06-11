
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1
        String a1 = "abcd";
        String b1 = "abce";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        assertEquals(1, result1.linksCount());
        assertEquals(0, result1.getLink(0).getPosOrig());
        assertEquals(0, result1.getLink(0).getPosNew());
        assertEquals(4, result1.getLink(0).getLen());

        // Test case 2
        String a2 = "abc";
        String b2 = "def";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        assertEquals(0, result2.linksCount());

        // Test case 3
        String a3 = "abcdef";
        String b3 = "abdef";
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        assertEquals(1, result3.linksCount());
        assertEquals(2, result3.getLink(0).getPosOrig());
        assertEquals(2, result3.getLink(0).getPosNew());
        assertEquals(1, result3.getLink(0).getLen());
    }

    @Test
    public void testPatch() {
        // Test case 1
        String a1 = "abcd";
        RelativeString r1 = new RelativeString();
        r1.addLink(0, 0, 4);
        String result1 = StringIncrementor.patch(a1, r1);
        assertEquals("abcd", result1);

        // Test case 2
        String a2 = "abcdef";
        RelativeString r2 = new RelativeString();
        r2.addLink(2, 2, 1);
        String result2 = StringIncrementor.patch(a2, r2);
        assertEquals("abc", result2);
    }
}
