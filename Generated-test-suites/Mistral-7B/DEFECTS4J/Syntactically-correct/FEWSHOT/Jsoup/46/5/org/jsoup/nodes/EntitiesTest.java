package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("x"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo&amp;bar&quot;";
        String expectedOutput = "&amp;lt;foo&amp;amp;&amp;quot;";
        String output = Entities.escape(input, Document.OutputSettings.base());
        assertEquals(expectedOutput, output);

        input = "&xlt;foo&amp;xbar&quot;";
        expectedOutput = "&#x26;lt;foo&#x26;amp;&#x22;";
        output = Entities.escape(input, Document.OutputSettings.base());
        assertEquals(expectedOutput, output);

        input = "&lt;foo&amp;bar&quot;";
        expectedOutput = "<foo>&amp;bar";
        output = Entities.escape(input, Document.OutputSettings.xhtml());
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;amp;&amp;quot;";
        String expectedOutput = "&lt;foo&amp;quot;";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        input = "&amp;#x26;lt;foo&amp;quot;";
        expectedOutput = "&lt;foo";
        output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        input = "&lt;foo&amp;quot;";
        expectedOutput = "<foo>";
        output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'a', null));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '€', null));

        assertTrue(Entities.canEncode(CoreCharset.utf, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.utf, 'a', null));
        assertTrue(Entities.canEncode(CoreCharset.utf, '€', null));

        assertTrue(Entities.canEncode(CoreCharset.fallback, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.fallback, 'a', null));
        assertTrue(Entities.canEncode(CoreCharset.fallback, '€', null));
    }
}