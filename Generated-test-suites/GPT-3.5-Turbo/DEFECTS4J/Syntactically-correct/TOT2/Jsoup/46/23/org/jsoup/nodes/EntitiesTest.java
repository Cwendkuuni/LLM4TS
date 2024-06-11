import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Test for escaping characters in different modes
        String input = "<div>Foo & Bar</div>";
        String escapedXhtml = Entities.escape(input, new Document.OutputSettings(EscapeMode.xhtml));
        assertEquals("&lt;div&gt;Foo &amp; Bar&lt;/div&gt;", escapedXhtml);

        String escapedBase = Entities.escape(input, new Document.OutputSettings(EscapeMode.base));
        assertEquals("<div>Foo & Bar</div>", escapedBase);

        String escapedExtended = Entities.escape(input, new Document.OutputSettings(EscapeMode.extended));
        assertEquals("&lt;div&gt;Foo &amp; Bar&lt;/div&gt;", escapedExtended);
    }

    @Test
    public void testUnescape() {
        String escapedInput = "&lt;div&gt;Foo &amp; Bar&lt;/div&gt;";
        String unescaped = Entities.unescape(escapedInput);
        assertEquals("<div>Foo & Bar</div>", unescaped);
    }

    // Add more test cases for additional methods if needed

}