import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // Typical use case
        assertFalse(Entities.isNamedEntity("invalid")); // Edge case
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp")); // Typical use case
        assertFalse(Entities.isBaseNamedEntity("invalid")); // Edge case
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Typical use case
        assertNull(Entities.getCharacterByName("invalid")); // Edge case
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>&amp;</div>", Document.OutputSettings.html());
        assertEquals("&lt;div&gt;&amp;amp;&lt;/div&gt;", escapedString); // Typical use case
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;");
        assertEquals("<div>&amp;</div>", unescapedString); // Typical use case
    }

    @Test
    public void testEscape_CaseSensitiveEdge() {
        String escapedString = Entities.escape("<div>&AMP;</div>", Document.OutputSettings.html());
        assertEquals("&lt;div&gt;&amp;amp;&lt;/div&gt;", escapedString); // Edge case for case sensitivity
    }

    @Test
    public void testUnescape_Strict() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;", true);
        assertEquals("<div>&amp;</div>", unescapedString); // Error scenario for strict unescaping
    }
}