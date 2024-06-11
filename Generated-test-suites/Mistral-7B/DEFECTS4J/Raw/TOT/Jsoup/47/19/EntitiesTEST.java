package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in the base entity set
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test for unknown named entities
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for unknown named entities
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>Hello <b>World</b> &gt;!";
        String output = Entities.escape(input, new Document.OutputSettings());

        // Test for basic entities
        assertEquals("&amp;amp;&lt;lt&gt;Hello &amp;lt;b&gt;World&amp;gt;!", output);

        // Test for special characters
        assertEquals("&amp;lt;Hello &amp;lt;b&gt;World&amp;gt;!", escape("Hello <b>World</b>", new Document.OutputSettings()));
        assertEquals("&amp;lt;Hello &amp;quot;World&amp;quot;&amp;gt;!", escape("Hello \"World\"", new Document.OutputSettings()));
        assertEquals("&amp;lt;&amp;amp;&amp;gt;", escape("<<&", new Document.OutputSettings()));

        // Test for unsupported characters
        assertEquals("&#x26;#x60;#x3C;#x61;#x6C;&#x26;#x62;#x61;#x63;#x3E;&#x21;", escape("&×\"<abc>!", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;&lt;lt&gt;Hello &amp;lt;b&gt;World&amp;gt;!";
        String output = Entities.unescape(input, false);

        assertEquals("&amp;<lt>Hello <b>World</b>&gt;!", output);
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;amp;&lt;lt&gt;Hello &amp;lt;b&gt;World&amp;gt;!";
        String output = Entities.unescape(input, true);

        assertEquals("&amp;<lt>Hello &lt;b&gt;World&gt;!", output);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingResourceException() {
        Entities.loadEntities("non-existent.properties");
    }
}