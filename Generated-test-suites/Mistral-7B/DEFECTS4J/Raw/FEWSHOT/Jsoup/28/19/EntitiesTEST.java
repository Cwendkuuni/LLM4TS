package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test unknown named entities
        assertFalse(Entities.isNamedEntity("random"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('\'', Entities.getCharacterByName("apos"));
        assertEquals('\"', Entities.getCharacterByName("quot"));

        // Test unknown named entities
        assertNull(Entities.getCharacterByName("random"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEncoder(Character.isDefinedChar(0x1F000) ? System.out::writeUTF : System.out::print);
        settings.setEscapeMode(Entities.EscapeMode.base);

        String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp; &copy; &iexcl; &cent; &pound;";
        String expectedOutput = "&amp; &lt; &gt; &apos; &quot; &A treat; &nbsp; & (C)";

        String output = Entities.escape(input, settings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot; &eacute; &nbsp; &copy; &iexcl; &cent; &pound;";
        String expectedOutput = "<&gt; &amp;&amp; &apos;&apos; &quot;&quot; é &nbsp; © !¬";

        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot; &eacute; &nbsp; &copy; &iexcl; &cent; &pound;";
        String expectedOutput = "<&gt; &amp;&amp; &apos;&apos; &quot;&quot; é &nbsp; © !¬";

        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);

        String inputWithMissingSemicolon = "&amp&lt;";
        String outputWithMissingSemicolon = Entities.unescape(inputWithMissingSemicolon, true);
        assertNull(outputWithMissingSemicolon);
    }

    @Test
    public void testGetEscapeMapByMode() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();

        // Test XHTML entities
        assertEquals("quot", xhtmlMap.get('"'));
        assertEquals("amp", xhtmlMap.get('&'));
        assertEquals("apos", xhtmlMap.get('\''));
        assertEquals("lt", xhtmlMap.get('<'));
        assertEquals("gt", xhtmlMap.get('>'));

        // Test base entities
        assertEquals("quot", baseMap.get('"'));
        assertEquals("amp", baseMap.get('&'));
        assertEquals("apos", baseMap.get('\''));
        assertEquals("lt", baseMap.get('<'));
        assertEquals("gt", baseMap.get('>'));
        assertEquals("nbsp", baseMap.get((char) 160));

        // Test extended entities
        assertEquals("quot", extendedMap.get('"'));
        assertEquals("amp", extendedMap.get('&'));
        assertEquals("apos", extendedMap.get('\''));
        assertEquals("lt", extendedMap.get('<'));
        assertEquals("gt", extendedMap.get('>'));
        assertEquals("eacute", extendedMap.get((char) 225));
        assertEquals("iexcl", extendedMap.get((char) 161));
        assertEquals("cent", extendedMap.get((char) 163));
        assertEquals("pound", extendedMap.get((char) 164));
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap<String, Character>();

        InputStream resourceAsStream = Entities.class.getResourceAsStream("entities-base.properties");
        properties.load(resourceAsStream);
        resourceAsStream.close();

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Character character = Character.valueOf(entry.getValue().charAt(0));
            String name = entry.getKey();
            entities.put(name, character);
        }

        Map<Character, String> loadedEntities = Entities.toCharacterKey(entities);

        // Test loaded entities
        assertEquals('&', loadedEntities.get('amp'));
        assertEquals('<', loadedEntities.get('lt'));
        assertEquals('>', loadedEntities.get('gt'));
        assertEquals('\'', loadedEntities.get('apos'));
        assertEquals('"', loadedEntities.get('quot'));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<String, Character>();
        entities.put("quot", '"');
        entities.put("amp", '&');
        entities.put("apos", '\'');
        entities.put("lt", '<');
        entities.put("gt", '>');

        Map<Character, String> characterEntities = Entities.toCharacterKey(entities);

        // Test character entities
        assertEquals("quot", characterEntities.get('"'));
        assertEquals("amp", characterEntities.get('&'));
        assertEquals("apos", characterEntities.get('\''));
        assertEquals("lt", characterEntities.get('<'));
        assertEquals("gt", characterEntities.get('>'));
    }
}