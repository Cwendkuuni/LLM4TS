
package org.junit.test;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test with invalid named entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity("123"));
        assertFalse(Entities.isNamedEntity("ABC"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(34, Entities.getCharacterByName("quot").charValue());
        assertEquals(60, Entities.getCharacterByName("lt").charValue());
        assertEquals(62, Entities.getCharacterByName("gt").charValue());
        assertEquals(38, Entities.getCharacterByName("amp").charValue());
        assertEquals(39, Entities.getCharacterByName("apos").charValue());
    }

    @Test
    public void testEscape() {
        String input = "&lt;test&gt; &amp; &apos; &quot;";
        String expectedOutput = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot;";

        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscapeEmptyString() {
        assertEquals("", Entities.escape("", null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscapeNullInput() {
        String expectedOutput = "null";
        assertEquals(expectedOutput, Entities.escape(null, null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscapeCustomEncoding() {
        String input = "éàç";
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String expectedOutput = "&eacute; &agrave; &acirc;";

        assertEquals(expectedOutput, Entities.escape(input, encoder, Entities.EscapeMode.base));
    }

    @Test
    public void testEscapeXHTML() {
        String input = "<script>alert('XSS')</script>";
        String expectedOutput = "&lt;script&gt;alert(&#x27;XSS&#x27;)&lt;/script&gt;";

        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.xhtml));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeInvalidEscapeMode() {
        String input = "&amp;";
        String expectedOutput = "&amp;amp;";

        try {
            assertEquals(expectedOutput, Entities.escape(input, null, null));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testUnescapeEmptyString() {
        assertEquals("", Entities.unescape(""));
    }

    @Test
    public void testUnescapeNullInput() {
        String expectedOutput = "null";
        assertEquals(expectedOutput, Entities.unescape(null));
    }

    @Test
    public void testUnescapeSimpleEntities() {
        String input = "&amp; &lt; &gt; &quot; &apos;";
        String expectedOutput = "& amp; < > \" \"";

        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescapeComplexEntities() {
        String input = "&eacute;&agrave;&acirc;&#x00A0;";
        String expectedOutput = "é  à  à ␠";

        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeInvalidInput() {
        String input = "&invalidEntity;";

        try {
            String expectedOutput = "invalidEntity";
            assertEquals(expectedOutput, Entities.unescape(input));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testUnescapeStrictMode() {
        String input = "&amp;&#x26; &lt;&#x3C; &gt;&#x3E; &quot;&#x22; &apos;&#x27; &eacute;&#xA9; &agrave;&#xA0; &acirc;&#xA4;";
        String expectedOutput = "&amp;amp;&amp; &lt;&lt; &gt;&gt; &quot;&quot; &apos;&apos; é  à  à ␠";

        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
}
