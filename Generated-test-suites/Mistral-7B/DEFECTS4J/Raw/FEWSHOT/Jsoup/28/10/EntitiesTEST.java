package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        String string = "&lt;foo&bar;baz&gt;";
        assertEquals("&lt;foo&amp;bar;baz&gt;", Entities.escape(string, null, Entities.EscapeMode.base));
        assertEquals("&lt;foo&amp;bar;&gt;baz", Entities.escape(string, null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testEscapeWithEncoder() {
        String string = "abcdefghijklmnopqrstuvwxyz";
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertEquals(string, Entities.escape(string, encoder, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;foo&amp;bar;&gt;baz&quot; &apos; &nbsp; &iexcl; &cent; &pound; &copy; &euro; &reg; &trade;";
        assertEquals("<foo&bar;>baz\" ' \u00A1 \u00A2 \u00A3 \u00A9 \u00AE \u00B0 \u00B1", Entities.unescape(string));
    }

    @Test
    public void testUnescapeWithStrict() {
        String string = "&lt;foo&amp;bar;&gt;baz&quot; &apos; &nbsp; &iexcl; &cent; &pound; &copy; &euro; &reg; &trade;";
        assertEquals("<foo&bar;>baz\" ' \u00A1 \u00A2 \u00A3 \u00A9 \u00AE \u00B0 \u00B1", Entities.unescape(string, true));
    }

    @Test
    public void testUnescapeInvalid() {
        String string = "&foo;";
        assertNull(Entities.unescape(string));
    }

    @Test
    public void testIsNamedEntityInvalid() {
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("fooBar"));
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            fail();
        }
    }
}