package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>&</div>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base));
        assertEquals("&lt;div&gt;&amp;&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;&lt;/div&gt;");
        assertEquals("<div>&</div>", unescapedString);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        String input = "special chars: < > & \"";
        String escapedString = Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("special chars: &lt; &gt; &amp; &quot;", escapedString);
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&lt;div&gt;&amp;&lt;/div";
        String unescapedString = Entities.unescape(input, true);
        assertEquals("<div>&<&gt;/div", unescapedString);
    }

}