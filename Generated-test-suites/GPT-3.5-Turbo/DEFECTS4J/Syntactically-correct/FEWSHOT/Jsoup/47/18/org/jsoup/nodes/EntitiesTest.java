package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("abcd"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abcd"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("abcd"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&amp;", Entities.escape("&", outputSettings));
        assertEquals("&amp;", Entities.escape("&amp;", outputSettings));
        assertEquals("&nbsp;", Entities.escape("\u00A0", outputSettings));
        assertEquals("&lt;", Entities.escape("<", outputSettings));
        assertEquals("<", Entities.escape("<", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&#x0026;"));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&;", Entities.unescape("&;", true));
    }

    @Test
    public void testCanEncode() {
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, (char) 0x00A9, null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uD834', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', null));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertTrue(entities.containsKey("lt"));
        assertEquals(Character.valueOf((char) 0x0003C), entities.get("lt"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Map<Character, String> map = Entities.toCharacterKey(entities);
        assertTrue(map.containsKey(Character.valueOf((char) 0x0003C)));
        assertEquals("lt", map.get(Character.valueOf((char) 0x0003C)));
    }
}