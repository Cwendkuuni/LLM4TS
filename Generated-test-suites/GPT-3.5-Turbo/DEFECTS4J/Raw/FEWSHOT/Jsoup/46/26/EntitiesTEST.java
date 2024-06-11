import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("nbsp"));

    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("nbsp"));
    }

    @Test
    public void testEscape() {
        assertEquals("This is &lt;html&gt;", Entities.escape("This is <html>", Document.OutputSettings.base()));
        assertEquals("This is &gt;", Entities.escape("This is >", Document.OutputSettings.base()));
        assertEquals("This is &amp;", Entities.escape("This is &", Document.OutputSettings.base()));
        assertEquals("This is &quot;", Entities.escape("This is \"", Document.OutputSettings.base()));
        assertEquals("This is &lt;html&gt;", Entities.escape("This is <html>", Document.OutputSettings.xhtml()));
        assertEquals("This is >", Entities.escape("This is >", Document.OutputSettings.xhtml()));
        assertEquals("This is &", Entities.escape("This is &", Document.OutputSettings.xhtml()));
        assertEquals("This is \"", Entities.escape("This is \"", Document.OutputSettings.xhtml()));
    }

    @Test
    public void testUnescape() {
        assertEquals("This is <html>", Entities.unescape("This is &lt;html&gt;"));
        assertEquals("This is >", Entities.unescape("This is &gt;"));
        assertEquals("This is &", Entities.unescape("This is &amp;"));
        assertEquals("This is \"", Entities.unescape("This is &quot;"));
    }
}