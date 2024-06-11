package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape_XHTML() {
        String expected = "&quot;";
        String actual = Entities.escape("\"", Document.OutputSettings.xhtml);
        assertEquals(expected, actual);

        expected = "&amp;";
        actual = Entities.escape("&", Document.OutputSettings.xhtml);
        assertEquals(expected, actual);

        expected = "&apos;";
        actual = Entities.escape("'", Document.OutputSettings.xhtml);
        assertEquals(expected, actual);

        expected = "&lt;";
        actual = Entities.escape("<", Document.OutputSettings.xhtml);
        assertEquals(expected, actual);

        expected = "&gt;";
        actual = Entities.escape(">", Document.OutputSettings.xhtml);
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape_Base() {
        String expected = "&#34;";
        String actual = Entities.escape("\"", Document.OutputSettings.base);
        assertEquals(expected, actual);

        expected = "&#38;";
        actual = Entities.escape("&", Document.OutputSettings.base);
        assertEquals(expected, actual);

        expected = "&#39;";
        actual = Entities.escape("'", Document.OutputSettings.base);
        assertEquals(expected, actual);

        expected = "&#60;";
        actual = Entities.escape("<", Document.OutputSettings.base);
        assertEquals(expected, actual);

        expected = "&#62;";
        actual = Entities.escape(">", Document.OutputSettings.base);
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape_Extended() {
        String expected = "&quot;";
        String actual = Entities.escape("\"", Document.OutputSettings.extended);
        assertEquals(expected, actual);

        expected = "&amp;";
        actual = Entities.escape("&", Document.OutputSettings.extended);
        assertEquals(expected, actual);

        expected = "&apos;";
        actual = Entities.escape("'", Document.OutputSettings.extended);
        assertEquals(expected, actual);

        expected = "&lt;";
        actual = Entities.escape("<", Document.OutputSettings.extended);
        assertEquals(expected, actual);

        expected = "&gt;";
        actual = Entities.escape(">", Document.OutputSettings.extended);
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescape() {
        String expected = "\"";
        String actual = Entities.unescape("&quot;");
        assertEquals(expected, actual);

        expected = "&";
        actual = Entities.unescape("&amp;");
        assertEquals(expected, actual);

        expected = "'";
        actual = Entities.unescape("&apos;");
        assertEquals(expected, actual);

        expected = "<";
        actual = Entities.unescape("&lt;");
        assertEquals(expected, actual);

        expected = ">";
        actual = Entities.unescape("&gt;");
        assertEquals(expected, actual);

        expected = "\"\"";
        actual = Entities.unescape("&quot;&quot;");
        assertEquals(expected, actual);

        expected = "\"&";
        actual = Entities.unescape("&quot;&amp;");
        assertEquals(expected, actual);

        expected = "\"&\"";
        actual = Entities.unescape("&quot;&amp;&quot;");
        assertEquals(expected, actual);

        expected = "\"&&quot;";
        actual = Entities.unescape("&quot;&amp;&amp;quot;");
        assertEquals(expected, actual);

        expected = "";
        actual = Entities.unescape("");
        assertEquals(expected, actual);

        expected = "&foo";
        actual = Entities.unescape("&foo");
        assertEquals(expected, actual);
    }
}