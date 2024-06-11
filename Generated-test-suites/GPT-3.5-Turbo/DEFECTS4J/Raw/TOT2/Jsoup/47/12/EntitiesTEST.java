import org.jsoup.nodes.Entities;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&amp;&gt;&quot;", Entities.escape("<&>\""));
    }

    @Test
    public void testUnescape() {
        assertEquals("<&>\"'", Entities.unescape("&lt;&amp;&gt;&quot;&apos;", false));
    }

    @Test
    public void testEscapeEdgeCases() {
        assertEquals("&nbsp;&#x2603;", Entities.escape("\u00A0☃"));
    }

    @Test
    public void testUnescapeEdgeCases() {
        assertEquals("\u00A0☃", Entities.unescape("&nbsp;&#x2603;", true));
    }

    @Test
    public void testEscapeExceptions() {
        try {
            Entities.escape(null, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testUnescapeExceptions() {
        try {
            Entities.unescape(null, true);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}