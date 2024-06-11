package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("Hello < World & Nice", new Document.OutputSettings());
        assertEquals("Hello &lt; World &amp; Nice", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &lt; World &amp; Nice");
        assertEquals("Hello < World & Nice", unescapedString);
    }

    @Test
    public void testEscape_edgeCases() {
        // test with empty string
        assertEquals("", Entities.escape("", new Document.OutputSettings()));

        // test with string containing only escapes
        String escapedString = Entities.escape("&lt;&gt;&amp;", new Document.OutputSettings());
        assertEquals("&amp;lt;&amp;gt;&amp;amp;", escapedString);
    }

    @Test
    public void testUnescape_edgeCases() {
        // test with empty string
        assertEquals("", Entities.unescape(""));

        // test with typical unescaped string
        assertEquals("<Hello World>", Entities.unescape("&lt;Hello World&gt;"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescape_error() {
        Entities.unescape("&lt&SomethingWentWrong;");
    }
}