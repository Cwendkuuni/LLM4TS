package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("notAnEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("notBaseEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("notAnEntity"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<div>Hello &amp; World</div>", new Document.OutputSettings());
        assertEquals("&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;");
        assertEquals("<div>Hello &amp; World</div>", unescaped);
    }

    @Test
    public void testEscapeWithNullInput() {
        String escaped = Entities.escape(null, new Document.OutputSettings());
        assertNotNull(escaped);
        assertTrue(escaped.isEmpty());
    }

    @Test
    public void testUnescapeWithNullInput() {
        String unescaped = Entities.unescape(null);
        assertNotNull(unescaped);
        assertTrue(unescaped.isEmpty());
    }

    @Test
    public void testEscapeWithEmptyString() {
        String escaped = Entities.escape("", new Document.OutputSettings());
        assertNotNull(escaped);
        assertTrue(escaped.isEmpty());
    }

    @Test
    public void testUnescapeWithEmptyString() {
        String unescaped = Entities.unescape("");
        assertNotNull(unescaped);
        assertTrue(unescaped.isEmpty());
    }

}