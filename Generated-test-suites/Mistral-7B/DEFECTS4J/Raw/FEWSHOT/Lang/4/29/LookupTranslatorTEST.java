package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(1, translator.shortest);
        assertEquals(4, translator.longest);
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
    }

    @Test
    public void testTranslateWithNoMatch() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("xyz"), 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMatch() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(2, translator.translate(new String("cd"), 0, out));
        assertEquals("2", out.toString());
    }

    @Test
    public void testTranslateWithMatchFromMiddle() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("efgdh"), 3, out));
        assertEquals("gh", out.toString());
    }

    @Test
    public void testTranslateWithMatchFromEnd() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(4, translator.translate(new String("ghij"), 4, out));
        assertEquals("", out.toString());
        assertEquals(4, translator.translate(new String("ghij"), 3, out));
        assertEquals("", out.toString());
        assertEquals(3, translator.translate(new String("ghij"), 2, out));
        assertEquals("h", out.toString());
        assertEquals(2, translator.translate(new String("ghij"), 1, out));
        assertEquals("g", out.toString());
        assertEquals(1, translator.translate(new String("ghij"), 0, out));
        assertEquals("gh", out.toString());
    }

    @Test
    public void testTranslateWithMatchFromStart() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(1, translator.translate(new String("ab"), 0, out));
        assertEquals("1", out.toString());
    }

    @Test
    public void testTranslateWithMultipleMatches() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" }, { "ij", "5" }, { "kl", "6" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(2, translator.translate(new String("cdij"), 1, out));
        assertEquals("j", out.toString());
        assertEquals(4, translator.translate(new String("cdij"), 0, out));
        assertEquals("cdij", out.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        Charsequence[][] lookup = new Charsequence[][] {};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String(""), 0, out));
        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() throws IOException {
        Charsequence[][] lookup = new Charsequence[][] {};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        translator.translate(null, 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithIndexOutOfBounds() throws IOException {
        Charsequence[][] lookup = new Charsequence[][] { { "ab", "1" }, { "cd", "2" }, { "ef", "3" }, { "gh", "4" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        translator.translate(new String("xx"), 5, out);
    }

    @Test
    public void testTranslateWithNullLookUpTable() {
        LookupTranslator translator = new LookupTranslator((Charsequence[][]) null);
        assertNull(translator.lookupMap);
    }
}