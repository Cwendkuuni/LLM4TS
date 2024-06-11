package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String escaped = Entities.escape("This &amp; That <a>", out);
        assertEquals("This &amp;amp; That &lt;a&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This &amp;amp; That &lt;a&gt;");
        assertEquals("This & That <a>", unescaped);
    }

    @Test
    public void testEscapeWithNormalisation() {
        Document.OutputSettings out = new Document.OutputSettings();
        String escaped = Entities.escape("    Test    JavaScript    ", out);
        assertEquals("Test JavaScript", escaped);
    }

    @Test
    public void testEscapeWithStripLeadingWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        String escaped = Entities.escape("  Leading White  Text", out);
        assertEquals("Leading White  Text", escaped);
    }

}