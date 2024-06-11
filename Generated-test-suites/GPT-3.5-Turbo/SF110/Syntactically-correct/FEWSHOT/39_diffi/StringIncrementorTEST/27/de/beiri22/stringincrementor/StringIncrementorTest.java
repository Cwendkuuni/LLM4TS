package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abc";
        String b = "def";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());

        a = "abc";
        b = "abc";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());

        a = "abcdef";
        b = "defabc";
        result = StringIncrementor.diff(a, b);
        assertEquals("def", result.getAbsolute());
    }

    @Test
    public void testDiffVerbose() {
        String a = "abcdef";
        String b = "defabc";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals("def", result.getAbsolute());
    }

    @Test
    public void testPatch() {
        String a = "abc";
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        String result = StringIncrementor.patch(a, relativeString);
        assertEquals("abc", result);

        a = "def";
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        result = StringIncrementor.patch(a, relativeString);
        assertEquals("abc", result);

        a = "abcdef";
        relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("def");
        result = StringIncrementor.patch(a, relativeString);
        assertEquals("defabc", result);
    }

    @Test
    public void testPatchVerbose() {
        String a = "abcdef";
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 3));
        relativeString.setAbsolute("def");
        String result = StringIncrementor.patch(a, relativeString, true);
        assertEquals("defabc", result);
    }
}