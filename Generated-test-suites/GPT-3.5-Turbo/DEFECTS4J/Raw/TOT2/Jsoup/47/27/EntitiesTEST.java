import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "<div>Test</div>";
        String output = Entities.escape(input, new Document.OutputSettings());
        assertEquals("&lt;div&gt;Test&lt;/div&gt;", output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String output = Entities.unescape(input);
        assertEquals("<div>Test</div>", output);
    }

    @Test
    public void testEscapeWithAttributes() {
        String input = "attribute=&quot;value&quot;";
        String output = Entities.escape(input, new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("attribute=&quot;value&quot;", output);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String output = Entities.unescape(input, true);
        assertEquals("<div>Test</div>", output);
    }

    @Test
    public void testEscapeNullString() {
        String output = Entities.escape(null, new Document.OutputSettings());
        assertNotNull(output);
        assertTrue(output.isEmpty());
    }

    @Test
    public void testUnescapeEmptyString() {
        String output = Entities.unescape("");
        assertNotNull(output);
        assertTrue(output.isEmpty());
    }

    @Test
    public void testEscapeMalformedInput() {
        String input = "<div>Test</div" + (char) 0xD800; // high surrogate without low surrogate
        String output = Entities.escape(input, new Document.OutputSettings());
        assertEquals("&lt;div&gt;Test&lt;/div&#x");
    }

}