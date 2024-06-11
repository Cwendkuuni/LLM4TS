import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
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
        String input = "<p>Escape &lt; and &amp;</p>";
        String expected = "&lt;p&gt;Escape &amp;lt; and &amp;amp;&lt;/p&gt;";
        assertEquals(expected, Entities.escape(input, Document.OutputSettings.xhtml()));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;p&gt;Escape &amp;lt; and &amp;amp;&lt;/p&gt;";
        String expected = "<p>Escape &lt; and &amp;</p>";
        assertEquals(expected, Entities.unescape(input));
    }

}