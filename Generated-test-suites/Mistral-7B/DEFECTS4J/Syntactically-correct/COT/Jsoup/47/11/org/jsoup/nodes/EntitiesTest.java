package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.Entities.EscapeMode;

import org.junit.Test;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertFalse(entities.isNamedEntity("ltx"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertFalse(entities.isBaseNamedEntity("ltx"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Character character = entities.getCharacterByName("lt");
        assertEquals('<', character.charValue());
        assertNull(entities.getCharacterByName("ltx"));
    }

    @Test
    public void testCanEncodeWithASCII() {
        Entities entities = new Entities();
        CoreCharset ascii = CoreCharset.ascii;
        assertTrue(entities.canEncode('A', ascii));
        assertTrue(entities.canEncode(' ', ascii));
        assertTrue(entities.canEncode('&', ascii));
    }

    @Test
    public void testCanEncodeWithUTF() {
        Entities entities = new Entities();
        CoreCharset utf = CoreCharset.utf;
        assertTrue(entities.canEncode('\u0041', utf)); // A
        assertTrue(entities.canEncode('\u0020', utf)); // space
        assertTrue(entities.canEncode('\u0026', utf)); // &
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithInvalidCharset() {
        Entities entities = new Entities();
        CoreCharset invalidCharset = new CoreCharset() {
            @Override
            public String toString() {
                return "invalidCharset";
            }
        };
        entities.canEncode('A', invalidCharset);
    }

    @Test
    public void testEscapeWithXHTMLMode() {
        String input = "&lt;&gt;&amp;&quot;";
        StringBuilder expectedOutput = new StringBuilder("&amp;lt;&amp;gt;&amp;amp;&amp;quot;");

        Entities entities = new Entities();
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        assertEquals(expectedOutput.toString(), entities.escape(input, new Entities.OutputSettings(xhtmlMode)));
    }

    @Test
    public void testEscapeWithDefaultMode() {
        String input = "&lt;&gt;&amp;&quot;";
        StringBuilder expectedOutput = new StringBuilder("&amp;lt;>&amp;gt;&&amp;quot;");

        Entities entities = new Entities();
        Entities.EscapeMode baseMode = Entities.EscapeMode.base;
        assertEquals(expectedOutput.toString(), entities.escape(input, new Entities.OutputSettings(baseMode)));
    }

    @Test
    public void testEscapeWithExtendedMode() {
        String input = "&lt;&gt;&amp;&quot;";
        StringBuilder expectedOutput = new StringBuilder("&lt;&gt;&amp;amp;&quot;");

        Entities entities = new Entities();
        Entities.EscapeMode extendedMode = Entities.EscapeMode.extended;
        assertEquals(expectedOutput.toString(), entities.escape(input, new Entities.OutputSettings(extendedMode)));
    }

    @Test
    public void testUnescapeWithValidString() {
        String input = "&amp;lt;&amp;gt;&amp;amp;&amp;quot;";
        String expectedOutput = "<>amp;";

        Entities entities = new Entities();
        assertEquals(expectedOutput, entities.unescape(input));
    }

    @Test
    public void testUnescapeWithInvalidString() {
        String input = "&xyz;";
        assertNull(entities.unescape(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullString() {
        Entities entities = new Entities();
        entities.unescape(null);
    }

    @Test
    public void testLoadEntitiesFromFile() {
        Entities entities = new Entities();
        Map<String, Character> loadedEntities = entities.loadEntities("entities-full.properties");
        assertEquals(312, loadedEntities.size());
    }

    @Test
    public void testToCharacterKey() {
        Entities entities = new Entities();
        Map<Character, String> characterEntities = entities.toCharacterKey(entities.full);
        assertEquals(312, characterEntities.size());
    }
}