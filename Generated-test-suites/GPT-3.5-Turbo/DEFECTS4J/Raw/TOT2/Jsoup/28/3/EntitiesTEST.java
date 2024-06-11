package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<>&", Document.OutputSettings.html());
        assertEquals("&lt;&gt;&amp;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;&gt;&amp;");
        assertEquals("<>&", unescapedString);
    }

    @Test
    public void testUnescapeWithStrict() {
        String unescapedString = Entities.unescape("&lt;&gt;&amp;", true);
        assertEquals("<>&", unescapedString);
    }

    // Add more test cases for other methods
    
}