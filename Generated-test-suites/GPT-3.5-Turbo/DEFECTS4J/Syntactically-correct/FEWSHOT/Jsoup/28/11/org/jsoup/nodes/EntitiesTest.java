package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test with known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test with unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test with known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));

        // Test with unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeXhtml() {
        // Test with xhtml escapeMode
        String escapedString = Entities.escape("<div>hello</div>", Entities.EscapeMode.xhtml);
        assertEquals("&lt;div&gt;hello&lt;/div&gt;", escapedString);
    }

    @Test
    public void testEscapeBase() {
        // Test with base escapeMode
        String escapedString = Entities.escape("<div>hello</div>", Entities.EscapeMode.base);
        assertEquals("&lt;div&gt;hello&lt;/div&gt;", escapedString);
    }

    @Test
    public void testEscapeExtended() {
        // Test with extended escapeMode
        String escapedString = Entities.escape("<div>hello</div>", Entities.EscapeMode.extended);
        assertEquals("&lt;div&gt;hello&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        // Test with unescape
        String unescapedString = Entities.unescape("&lt;div&gt;hello&lt;/div&gt;");
        assertEquals("<div>hello</div>", unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        // Test with strict unescape
        String unescapedString = Entities.unescape("&lt;div&gt;hello&lt;/div&gt;", true);
        assertEquals("<div>hello</div>", unescapedString);
    }
}