package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\""));
    }

    @Test
    public void testEscapeWithEncoderAndEscapeMode() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\"", null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;"));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("<>&'", Entities.unescape("&lt;&gt;&amp;&apos;", true));
    }
}