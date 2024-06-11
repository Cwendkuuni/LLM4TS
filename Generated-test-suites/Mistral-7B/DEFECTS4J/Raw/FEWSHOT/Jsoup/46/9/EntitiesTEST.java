package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity: " + name, Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity: " + name, Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertFalse("Unexpected base named entity: &#x0003;", Entities.isBaseNamedEntity("&#x0003;"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<foo>&quot;bar&gt;&#xA0; identifier &amp; &#x1F601;";
        OutputSettings outputSettings = new OutputSettings();
        String expected = "&amp;amp;&lt;foo&gt;&amp;quot;bar&gt;&#xA0; identifier &amp; &amp;#x1F601;";

        assertEquals(expected, Entities.escape(input, outputSettings));

        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        expected = "&amp;lt;&amp;gt;&amp;quot; &amp;#xA0; identifier &amp;#x1F601;";
        assertEquals(expected, Entities.escape(input, outputSettings));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;&lt;foo&gt;&amp;quot;bar&gt;&#xA0; identifier &amp; &amp;#x1F601;";
        assertEquals("&amp;<foo>&quot;bar&gt;\u00A0 identifier &amp; &amp;✈;", Entities.unescape(input));
    }

    @Test
    public void testCanEncode() {
        Map<Character, String> utf = Entities.fullByVal;
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        for (Map.Entry<Character, String> entry : utf.entrySet()) {
            Character character = entry.getKey();
            String entity = entry.getValue();
            assertTrue("Encoding: " + character, Entities.canEncode(CoreCharset.utf, character, encoder));
            assertEquals(character, Entities.getCharacterByName(entity));
        }

        CharsetEncoder fallbackEncoder = StandardCharsets.UTF_8.newEncoder();
        Character invalidCharacter = '\uD840\uDC00';
        assertFalse("Encoding: " + invalidCharacter, Entities.canEncode(CoreCharset.ascii, invalidCharacter, fallbackEncoder));
    }
}