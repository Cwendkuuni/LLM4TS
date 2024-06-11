
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.relativestring.RelativeString;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test diff method with two equal strings
        String a = "hello";
        String b = "hello";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(result.getLength(), 0);
        assertEquals(result.getAbsolute(), "");

        // Test diff method with strings having no common substring
        a = "abc";
        b = "xyz";
        result = StringIncrementor.diff(a, b);
        assertEquals(result.getLength(), 0);
        assertEquals(result.getAbsolute(), b);

        // Test diff method with strings having a common substring
        a = "abcxyzdef";
        b = "xyz";
        result = StringIncrementor.diff(a, b);
        assertEquals(result.getLength(), 1);
        assertEquals(result.getLink(0).getPosOrig(), 3);
        assertEquals(result.getLink(0).getPosNew(), 0);
        assertEquals(result.getLink(0).getLen(), 3);
        assertEquals(result.getAbsolute(), "abcdef");

        // Test diff method with verbose option
        a = "abcxyzdef";
        b = "xyz";
        result = StringIncrementor.diff(a, b, true);
        assertEquals(result.getLength(), 1);
        assertEquals(result.getLink(0).getPosOrig(), 3);
        assertEquals(result.getLink(0).getPosNew(), 0);
        assertEquals(result.getLink(0).getLen(), 3);
        assertEquals(result.getAbsolute(), "abcdef");
    }

    @Test
    public void testPatch() {
        // Test patch method with empty RelativeString
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals(result, a);

        // Test patch method with non-empty RelativeString
        a = "abcdef";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        r.setAbsolute("xyz");
        String patched = StringIncrementor.patch(a, r);
        assertEquals(patched, "xyzdef");

        // Test patch method with verbose option
        a = "abcdef";
        r = new RelativeString();
        r.addLink(new StringLink(0, 3, 3));
        r.setAbsolute("xyz");
        patched = StringIncrementor.patch(a, r, true);
        assertEquals(patched, "xyzdef");
    }

}

