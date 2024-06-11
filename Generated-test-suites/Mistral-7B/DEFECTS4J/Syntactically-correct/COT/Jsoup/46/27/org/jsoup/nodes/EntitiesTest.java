package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;

import java.util.*;
import java.io.IOException;

public class EntitiesTest {

    // Test for isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("gt")); // Different entity
        assertFalse(Entities.isNamedEntity("invalidEntity")); // Non-existent entity
    }

    // Test for isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("gt")); // Different entity
        assertFalse(Entities.isBaseNamedEntity("invalidEntity")); // Non-existent entity
    }

    // Test for getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for unknown entity
        assertNull(Entities.getCharacterByName("unknownEntity"));
    }

    // Test for escape method - Normal string
    @Test
    public void testEscapeNormalString() {
        String input = "Hello & World <tag> Test & ' single quote & \" double quote & &amp; & &gt;";
        String expectedOutput = "Hello &amp; &amp;World&amp; &amp;lt;tag&amp; &amp;amp; &amp; &amp;gt;";
        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    // Test for escape method - InputStream
    @Test
    public void testEscapeInputStream() throws IOException {
        String input = "Hello & World <tag> Test & ' single quote & \" double quote & &amp; & &gt;";
        String expectedOutput = "Hello &amp; &amp;World&amp; &amp;lt;tag&amp; &amp;amp; &amp; &amp;gt;";
        StringBuilder accum = new StringBuilder(expectedOutput.length());
        Entities.escape(accum, input, null, false, false, false);
        assertEquals(expectedOutput, accum.toString());
    }

    // Test for escape method - In attribute
    @Test
    public void testEscapeInAttribute() {
        String input = "<tag attribute='Hello & World <tag> Test & 'single quote & \"double quote & &amp; & &gt;' />";
        String expectedOutput = "<tag attribute='Hello &amp; &amp;World&amp; &amp;lt;tag&amp; &amp;amp; &amp; &amp;gt;' />";
        assertEquals(expectedOutput, Entities.escape(input, null, true, false, false, false));
    }

    // Test for escape method - Normalize White
    @Test
    public void testEscapeNormalizeWhite() {
        String input = "Hello   World <tag> Test & ' single quote & \" double quote & &amp; & &gt;";
        String expectedOutput = "Hello &nbsp; &nbsp;World &nbsp; &nbsp;<tag> &nbsp; Test &nbsp; &nbsp; ' &nbsp; single quote &nbsp; &nbsp; \" &nbsp; double quote &nbsp; &amp; &nbsp; &nbsp; &gt;";
        assertEquals(expectedOutput, Entities.escape(input, null, false, true, false, false));
    }

    // Test for escape method - Strip Leading White
    @Test
    public void testEscapeStripLeadingWhite() {
        String input = "Hello   World <tag> Test & ' single quote & \" double quote & &amp; & &gt;";
        String expectedOutput = "Hello &nbsp; &nbsp;World &nbsp; &nbsp;<tag> &nbsp; Test &nbsp; ' &nbsp; single quote &nbsp; &nbsp; \" &nbsp; double quote &nbsp; &amp; &nbsp; &nbsp; &gt;";
        String output = Entities.escape(input, null, false, true, true, false);
        assertTrue(output.startsWith("Hello ")); // Check that the leading whitespace has not been removed
    }

    // Test for escape method - Combination of options
    @Test
    public void testEscapeCombinationOptions() {
        String input = "  Hello & World <tag> Test & ' single quote & \" double quote & &amp; & &gt;";
        String expectedOutput = "  Hello &nbsp; &nbsp;World &nbsp; &nbsp;<tag> &nbsp; Test &nbsp; ' &nbsp; single quote &nbsp; &nbsp; \" &nbsp; double quote &amp; &nbsp; &nbsp; &gt;";
        assertEquals(expectedOutput, Entities.escape(input, null, true, true, true, false));
    }

    // Test for unescape method
    @Test
    public void testUnescape() {
        String input = "Hello &amp; &amp;World&amp; &amp;lt;tag&amp; &amp;amp; &amp; &amp;gt;";
        String expectedOutput = "Hello & World <tag> Test & & ' single quote & \" double quote & &amp; & &gt;";
        assertEquals(expectedOutput, Entities.unescape(input, false));
    }

    // Test for canEncode method
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\u00A9'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uD800')); // Supplementary character
    }
}