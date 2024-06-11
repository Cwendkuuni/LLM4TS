import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String escapedString = Entities.escape("<div>&amp;</div>", outputSettings);
        assertEquals("&lt;div&gt;&amp;amp;&lt;/div&gt;", escapedString);
    }

    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String escapedString = Entities.escape("\"attribute\"", outputSettings);
        assertEquals("&#34;attribute&#34;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;");
        assertEquals("<div>&amp;</div>", unescapedString);
    }

    @Test
    public void testUnescape_strict() {
        String unescapedString = Entities.unescape("&lt;div&amp;amp;&lt;/div&gt;", true);
        assertEquals("&lt;div&amp;amp;&lt;/div&gt;", unescapedString);
    }
}