package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        assertEquals(null, StringIncrementor.diff(null, null));
        assertEquals(null, StringIncrementor.diff("", null));
        assertEquals(null, StringIncrementor.diff(null, ""));
        assertEquals(new RelativeString(), StringIncrementor.diff("", ""));
        String a = "abc";
        assertEquals(new RelativeString(), StringIncrementor.diff(a, a));
        String b = "def";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 3));
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
        b = "abc";
        expectedResult = new RelativeString();
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffVerbose() {
        // Test with verbose set to true
        // Need to figure out how to test Console Output
    }

    @Test
    public void testPatch() {
        // Test patch method
        assertNull(StringIncrementor.patch(null, null));
        assertEquals("", StringIncrementor.patch("", null));
        assertEquals(null, StringIncrementor.patch(null, new RelativeString()));
        assertEquals("", StringIncrementor.patch("", new RelativeString()));
        String a = "abc";
        assertEquals("abc", StringIncrementor.patch(a, new RelativeString()));
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        assertEquals("def", StringIncrementor.patch(a, relativeString));
        relativeString = new RelativeString();
        relativeString.setAbsolute("def");
        assertEquals("def", StringIncrementor.patch(a, relativeString));
    }

    @Test
    public void testPatchVerbose() {
        // Test with verbose set to true
        // Need to figure out how to test Console Output
    }
}