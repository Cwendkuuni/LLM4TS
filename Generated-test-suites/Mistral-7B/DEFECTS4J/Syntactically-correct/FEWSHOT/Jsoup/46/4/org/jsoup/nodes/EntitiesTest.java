package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, Character> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity '" + key + "' not recognized", Entities.isNamedEntity(key.toString()));
            assertEquals("Named entity '" + key + "' has wrong value", value, Entities.getCharacterByName(key.toString()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, Character> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Base named entity '" + key + "' not recognized", Entities.isBaseNamedEntity(key.toString()));
            assertEquals("Base named entity '" + key + "' has wrong value", value, Entities.getCharacterByName(key.toString()));
        }

        assertFalse("lt is not a base named entity", Entities.isBaseNamedEntity("lt".toString()));
        assertFalse("gt is not a base named entity", Entities.isBaseNamedEntity("gt".toString()));
        assertFalse("quot is not a base named entity", Entities.isBaseNamedEntity("quot".toString()));
        assertFalse("amp is not a base named entity", Entities.isBaseNamedEntity("amp".toString()));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals("'<'' is the character for entity 'lt'", '<', Entities.getCharacterByName("lt".toString()));
        assertEquals("'&' is the character for entity 'amp'", '&', Entities.getCharacterByName("amp".toString()));
        assertEquals("'\u00A0' is the character for entity 'nbsp'", '\u00A0', Entities.getCharacterByName("nbsp".toString()));
        assertEquals("'" + Character.toChars(0x2602)[0] + "' is the character for entity 'heart'", '\u2602', Entities.getCharacterByName("heart".toString()));
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;foo&gt;<bar>&quot;baz&quot;<quux&gt;";
        assertEquals("&lt;foo&gt;&amp;lt;bar&gt;&amp;quot;baz&amp;quot;&amp;gt;<quux&gt;", Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
        assertEquals("&lt;foo&gt;<bar>&quot;baz&quot;&gt;<quux&gt;", Entities.unescape(Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base))));
    }

    @Test
    public void testCanEncode() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'A'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'ö'));
        assertEquals(false, Entities.canEncode(Entities.CoreCharset.ascii, 'ö'));
    }
}