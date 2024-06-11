import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.html()));
        assertEquals("&nbsp;", Entities.escape(Character.toString((char) 160), Document.OutputSettings.html()));
        assertEquals("&#xa0;", Entities.escape(Character.toString((char) 160), Document.OutputSettings.xhtml()));
        assertEquals("&amp;&lt;&gt;&quot;", Entities.escape("&<>\"", Document.OutputSettings.html()));
    }

    @Test
    public void testEscapeWithAttributes() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html().prettyPrint(false)));
        assertEquals(">", Entities.escape(">", Document.OutputSettings.html().prettyPrint(false)));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html().prettyPrint(false)));
        assertEquals("\"", Entities.escape("\"", Document.OutputSettings.html().prettyPrint(false)));
        assertEquals(Character.toString((char) 160), Entities.escape(Character.toString((char) 160), Document.OutputSettings.html().prettyPrint(false)));
        assertEquals(Character.toString((char) 160), Entities.escape(Character.toString((char) 160), Document.OutputSettings.xhtml().prettyPrint(false)));
        assertEquals("&<>\"", Entities.escape("&<>\"", Document.OutputSettings.html().prettyPrint(false)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals(Character.toString((char) 160), Entities.unescape("&nbsp;"));
        assertEquals(Character.toString((char) 160), Entities.unescape("&#xa0;"));
        assertEquals("&<>\"", Entities.unescape("&amp;&lt;&gt;&quot;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals(Character.toString((char) 160), Entities.unescape("&nbsp;", true));
        assertEquals(Character.toString((char) 160), Entities.unescape("&#xa0;", true));
        assertEquals("&<>\"", Entities.unescape("&amp;&lt;&gt;&quot;", true));
    }

    @Test
    public void testUnescapeNonStrict() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals(Character.toString((char) 160), Entities.unescape("&nbsp;", false));
        assertEquals(Character.toString((char) 160), Entities.unescape("&#xa0;", false));
        assertEquals("&<>\"", Entities.unescape("&amp;&lt;&gt;&quot;", false));
    }

}