import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>");
        assertEquals("&lt;div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;");
        assertEquals("<div>", unescapedString);
    }
    
    // Test edge cases and exceptions

    @Test
    public void testEscape_emptyString() {
        String escapedString = Entities.escape("", new Document.OutputSettings());
        assertEquals("", escapedString);
    }

    @Test
    public void testEscape_nullString() {
        String escapedString = Entities.escape(null, new Document.OutputSettings());
        assertEquals("", escapedString);
    }

    @Test
    public void testEscape_withSpecialChars() {
        String escapedString = Entities.escape("\"Hello < World & Goodbye>", new Document.OutputSettings());
        assertEquals("&quot;Hello &lt; World &amp; Goodbye&gt;", escapedString);
    }

    @Test
    public void testEscape_inAttribute() {
        String escapedString = Entities.escape("<div>", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("<div>", escapedString);
    }

    @Test
    public void testUnescape_invalidEntity() {
        String unescapedString = Entities.unescape("&fake;");
        assertEquals("&fake;", unescapedString);
    }
    
}