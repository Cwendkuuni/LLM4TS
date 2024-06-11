import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("invalid"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("invalid"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((Character) '<', Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        assertEquals("Test &amp; escape &lt; string &gt;", Entities.escape("Test & escape < string >", documentOutputSettings));
    }

    @Test
    public void testUnescape() {
        assertEquals("Test & escape < string >", Entities.unescape("Test &amp; escape &lt; string &gt;"));
    }

    @Test
    public void testEscapeEdgeCases() {
        assertEquals("&#x20;Escape test&#x20;for edge cases", Entities.escape(" Escape test for edge cases", documentOutputSettings));
    }

    @Test
    public void testUnescapeEdgeCases() {
        assertEquals("Edge case &#x3b;for unescape", Entities.unescape("Edge case ;for unescape"));
    }

    @Test
    public void testEscapeExceptionHandling() {
        try {
            Entities.escape(null, documentOutputSettings);
            fail("Null string should throw an exception");
        } catch (NullPointerException e) {
            // expected NullPointerException
        }
    }

    @Test
    public void testUnescapeExceptionHandling() {
        try {
            Entities.unescape(null);
            fail("Null string should throw an exception");
        } catch (NullPointerException e) {
            // expected NullPointerException
        }
    }
}