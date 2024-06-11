
package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }
    
    @Test
    public void testEscape() {
        String escapeResult = Entities.escape("Hello < World", null);
        assertEquals("Hello &lt; World", escapeResult);
    }

    @Test
    public void testEscape_withOut() {
        String escapeResult = Entities.escape("Hello < World", new Document.OutputSettings());
        assertEquals("Hello &lt; World", escapeResult);
    }

    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String escapeResult = Entities.escape("Hello \"World\"", outputSettings);
        assertEquals("Hello &quot;World&quot;", escapeResult);
    }

    @Test
    public void testUnescape() {
        String unescapeResult = Entities.unescape("Hello &lt; World");
        assertEquals("Hello < World", unescapeResult);
    }

    @Test
    public void testUnescape_withStrict() {
        String unescapeResult = Entities.unescape("Hello &lt; World", true);
        assertEquals("Hello < World", unescapeResult);
    }
    
    // Additional test cases can be added to increase the coverage of the class
}
