
package de.beiri22.stringincrementor;

import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello";
        String b = "world";
        RelativeString result = StringIncrementor.diff(a, b);
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 0));
        assertEquals(expected, result);

        a = "abcdefghi";
        b = "abcdjklghi";
        result = StringIncrementor.diff(a, b);
        expected = new RelativeString();
        expected.addLink(new StringLink(4, 4, 1));
        expected.addLink(new StringLink(9, 9, 0));
        assertEquals(expected, result);

        a = "abcd";
        b = "abcd";
        result = StringIncrementor.diff(a, b);
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 4));
        assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        String a = "hello";
        String b = "world";
        RelativeString result = StringIncrementor.diff(a, b, true);
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 0));
        assertEquals(expected, result);

        a = "abcdefghi";
        b = "abcdjklghi";
        result = StringIncrementor.diff(a, b, true);
        expected = new RelativeString();
        expected.addLink(new StringLink(4, 4, 1));
        expected.addLink(new StringLink(9, 9, 0));
        assertEquals(expected, result);

        a = "abcd";
        b = "abcd";
        result = StringIncrementor.diff(a, b, true);
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 4));
        assertEquals(expected, result);
    }

    @Test
    public void testPatch() {
        String a = "hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 0));
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);

        a = "abcdefghi";
        r = new RelativeString();
        r.addLink(new StringLink(4, 4, 1));
        r.addLink(new StringLink(9, 9, 0));
        result = StringIncrementor.patch(a, r);
        assertEquals("abcdjklghi", result);

        a = "abcd";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }

    @Test
    public void testPatchVerbose() {
        String a = "hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 0));
        String result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);

        a = "abcdefghi";
        r = new RelativeString();
        r.addLink(new StringLink(4, 4, 1));
        r.addLink(new StringLink(9, 9, 0));
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abcdjklghi", result);

        a = "abcd";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);
    }
}
