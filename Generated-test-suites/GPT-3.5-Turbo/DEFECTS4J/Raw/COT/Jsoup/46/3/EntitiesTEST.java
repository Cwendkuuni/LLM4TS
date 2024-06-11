import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    // Test isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    // Test getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }

    // Test escape method
    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<h1>Hello</h1>", Document.OutputSettings.html());
        assertEquals("&lt;h1&gt;Hello&lt;/h1&gt;", escapedString);
    }

    // Test unescape method
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;h1&gt;Hello&lt;/h1&gt;");
        assertEquals("<h1>Hello</h1>", unescapedString);
    }

    // Test unescape strict method
    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("&lt;h1&gt;Hello&lt;/h1&gt;", true);
        assertEquals("&lt;h1&gt;Hello&lt;/h1&gt;", unescapedString);
    }

    // Test escape method for extended EscapeMode
    @Test
    public void testEscapeExtended() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        String escapedString = Entities.escape("<p>Test</p>", Document.OutputSettings.escapeMode(escapeMode));
        assertEquals("&lt;p&gt;Test&lt;/p&gt;", escapedString);
    }

    // Test escape method for base EscapeMode with normaliseWhite and stripLeadingWhite
    @Test
    public void testEscapeBase() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        Document.OutputSettings outputSettings = Document.OutputSettings.escapeMode(escapeMode).prettyPrint(true);
        String escapedString = Entities.escape("  <p>  Test  </p>  ", outputSettings);
        assertEquals("<p>Test</p>", escapedString);
    }

    // Test escape method for xhtml EscapeMode
    @Test
    public void testEscapeXhtml() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        String escapedString = Entities.escape("<p>Test</p>", Document.OutputSettings.escapeMode(escapeMode));
        assertEquals("&lt;p&gt;Test&lt;/p&gt;", escapedString);
    }

}