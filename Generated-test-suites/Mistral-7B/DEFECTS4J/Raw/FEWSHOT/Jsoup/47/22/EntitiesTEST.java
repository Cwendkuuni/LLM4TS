package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.getEntities();
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.getBaseEntities().getMap();
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.getEntities();
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
    }

    @Test
    public void testEscape() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, "&amp;<gt>\"", Entities.EscapeMode.base);
        assertEquals("&amp;&gt;\"", accumulator.toString());

        accumulator = new StringBuilder();
        Entities.escape(accumulator, "&amp;<gt>\"", Entities.EscapeMode.xhtml);
        assertEquals("&amp;#62;&#34;", accumulator.toString());

        accumulator = new StringBuilder();
        Entities.escape(accumulator, "&lt;foo&gt;<bar>&amp;", Entities.EscapeMode.extended);
        assertEquals("&lt;foo&gt;&#60;bar&gt;&amp;", accumulator.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("&amp;<gt>\"", Entities.escape("&amp;&gt;\"", Entities.EscapeMode.base));
        assertEquals("&lt;foo&gt;<bar>&amp;", Entities.escape("&lt;foo&gt;&#60;bar&gt;&amp;", Entities.EscapeMode.extended));

        assertEquals("&amp;", Entities.unescape("&amp;"));
        assertEquals("&lt;foo&gt;<bar>&amp;", Entities.unescape("&lt;foo&gt;&#60;bar&gt;&amp;", false));
        assertEquals("&lt;foo&gt;<bar>&amp;", Entities.unescape("&lt;foo&gt;&#60;bar&gt;&amp;", true));
    }

    @Test
    public void testCanEncode() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'ö'));
        assertEquals(false, Entities.canEncode(Entities.CoreCharset.ascii, '\u00E4'));
    }
}