
package de.beiri22.stringincrementor;

import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcde";
        String b = "abcde";
        boolean verbose = false;
        RelativeString expected = new RelativeString();
        StringIncrementor.diff(a, b, verbose);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPatch() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        boolean verbose = false;
        String expected = "abcde";
        String actual = StringIncrementor.patch(a, r, verbose);
        Assert.assertEquals(expected, actual);
    }
}
