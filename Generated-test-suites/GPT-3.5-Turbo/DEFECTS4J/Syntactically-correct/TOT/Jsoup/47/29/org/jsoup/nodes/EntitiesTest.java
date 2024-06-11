
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Entities.EscapeMode;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("random"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("random"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("random"));
    }

    @Test
    public void testEscape() {
        String string = "<p>Test</p>";
        String expected = "&lt;p&gt;Test&lt;/p&gt;";
        assertEquals(expected, Entities.escape(string, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;p&gt;Test&lt;/p&gt;";
        String expected = "<p>Test</p>";
        assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&lt;p&gt;Test&lt;/p&gt;";
        String expected = "<p>Test</p>";
        assertEquals(expected, Entities.unescape(string, true));
    }
}
