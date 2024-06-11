package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.jsoup.helper.StringUtil.isWhitespace;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("gt"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo&gt;<bar>baz&amp;quux&quot;";
        String expectedOutput = "&amp;lt;foo&amp;&gt;<bar>&amp;amp;quux&amp;quot;";

        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test
    public void testEscapeWithOutputSettings() {
        String input = "&lt;foo&gt;<bar>baz&amp;quux&quot;";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.xhtml);

        String expectedOutput = "&amp;lt;foo&gt;&amp;lt;bar&gt;baz&amp;amp;quux&amp;quot;";

        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(CoreCharset.utf, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.utf, '\u00E4', null)); // ä
        assertFalse(Entities.canEncode(CoreCharset.ascii, '\u00E4', null)); // ä
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&gt;&amp;quot;bar&amp;&amp;amp;quux&amp;quot;";
        String expectedOutput = "&lt;foo&quot;bar&amp;quux&quot;";

        assertEquals(expectedOutput, Entities.unescape(input, false));
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;lt;foo&gt;&quot;bar&amp;amp;quux&quot;";
        String expectedOutput = "&lt;foo&quot;bar&amp;quux&quot;";

        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
}