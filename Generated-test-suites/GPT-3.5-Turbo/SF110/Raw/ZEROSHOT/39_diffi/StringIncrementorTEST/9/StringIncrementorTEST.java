package de.beiri22.stringincrementor;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    private String a;
    private String b;

    @Before
    public void setup() {
        a = "abcdefg";
        b = "abxyzcdefg";
    }

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(3, result.linksCount());
    }

    @Test
    public void testDiffWithVerbose() {
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(3, result.linksCount());
    }

    @Test
    public void testPatch() {
        RelativeString diffResult = StringIncrementor.diff(a, b);
        String patchedString = StringIncrementor.patch(a, diffResult);
        assertEquals(b, patchedString);
    }

    @Test
    public void testPatchWithVerbose() {
        RelativeString diffResult = StringIncrementor.diff(a, b);
        String patchedString = StringIncrementor.patch(a, diffResult, true);
        assertEquals(b, patchedString);
    }
}