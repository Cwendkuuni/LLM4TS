import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeStringOutputSettings() {
        String testString = "<div>test</div>";
        EscapeMode escapeMode = EscapeMode.extended;
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        String escapedString = Entities.escape(testString, encoder, escapeMode);
        assertEquals("&lt;div&gt;test&lt;/div&gt;", escapedString);
    }

    @Test
    public void testEscapeStringCharsetEncoderEscapeMode() {
        String testString = "<div>test</div>";
        EscapeMode escapeMode = EscapeMode.xhtml;
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        String escapedString = Entities.escape(testString, encoder, escapeMode);
        assertEquals("&lt;div&gt;test&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescapeString() {
        String testString = "&lt;div&gt;test&lt;/div&gt;";
        String unescapedString = Entities.unescape(testString);
        assertEquals("<div>test</div>", unescapedString);
    }

    @Test
    public void testUnescapeStringStrict() {
        String testString = "&lt;div&gt;test&lt;/div&gt;";
        String unescapedString = Entities.unescape(testString, true);
        assertEquals("<div>test</div>", unescapedString);
    }

}