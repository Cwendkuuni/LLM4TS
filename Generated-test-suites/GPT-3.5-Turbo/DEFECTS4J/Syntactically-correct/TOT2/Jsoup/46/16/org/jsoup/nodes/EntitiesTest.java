import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<div>&amp;</div>", Document.OutputSettings.html());
        assertEquals("&lt;div&amp;amp;&lt;/div&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;div&amp;amp;&lt;/div&gt;");
        assertEquals("<div>&amp;</div>", unescaped);
    }

    @Test
    public void testEscapeEdgeCases() {
        String escaped = Entities.escape("<<><&", Document.OutputSettings.html());
        assertEquals("&lt;&lt;&gt;&lt;&amp;", escaped);
    }

    @Test
    public void testUnescapeEdgeCases() {
        String unescaped = Entities.unescape("&lt;&lt;&gt;&lt;&amp;");
        assertEquals("<<><&", unescaped);
    }
}