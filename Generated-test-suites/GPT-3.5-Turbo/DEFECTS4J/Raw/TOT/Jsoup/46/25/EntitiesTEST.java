package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertTrue(Entities.isNamedEntity("amp")); // typical use case
        assertFalse(Entities.isNamedEntity("xyz")); // edge case - unknown entity name
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
        assertTrue(Entities.isBaseNamedEntity("amp")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("xyz")); // edge case - unknown entity name
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // typical use case
        assertNull(Entities.getCharacterByName("xyz")); // edge case - unknown entity name
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<h1>Hello & Welcome</h1>", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("&lt;h1&gt;Hello &amp; Welcome&lt;/h1&gt;", escapedString); // typical use case
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;h1&gt;Hello &amp; Welcome&lt;/h1&gt;");
        assertEquals("<h1>Hello & Welcome</h1>", unescapedString); // typical use case
    }

    @Test
    public void testUnescapeWithStrict() {
        String unescapedString = Entities.unescape("&lt;h1&gt;Hello &amp; Welcome&lt;/h1&gt;", true);
        assertEquals("&lt;h1&gt;Hello &amp; Welcome&lt;/h1&gt;", unescapedString); // edge case - strict mode
    }

    @Test
    public void testUnescapeWithoutStrict() {
        String unescapedString = Entities.unescape("&lt;h1&gt;Hello &amp; Welcome&lt;/h1&gt;", false);
        assertEquals("<h1>Hello & Welcome</h1>", unescapedString); // typical use case
    }

    @Test
    public void testEscapeWithMultipleWhiteSpaces() {
        String escapedString = Entities.escape("    ", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("    ", escapedString); // edge case - multiple white spaces
    }

    @Test
    public void testEscapeWithNonAsciiCharacter() {
        String escapedString = Entities.escape("©", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("&#xA9;", escapedString); // edge case - non-ascii character
    }

    @Test
    public void testEscapeWithUnknownEntity() {
        String escapedString = Entities.escape("<h1>Hello &amp;xyz</h1>", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("&lt;h1&gt;Hello &amp;xyz&lt;/h1&gt;", escapedString); // edge case - unknown entity
    }

    @Test
    public void testEscapeWithAttribute() {
        StringBuilder accum = new StringBuilder();
        String string = "<a href=\"https://www.example.com\">Link</a>";
        Document.OutputSettings out = new Document.OutputSettings();
        boolean inAttribute = true;
        boolean normaliseWhite = false;
        boolean stripLeadingWhite = false;

        Entities.escape(accum, string, out, inAttribute, normaliseWhite, stripLeadingWhite);

        assertEquals("&lt;a href=&quot;https://www.example.com&quot;&gt;Link&lt;/a&gt;", accum.toString()); // typical use case
    }

}