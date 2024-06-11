package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity: " + name + " should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity: " + name + " should have the correct value", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity: " + name + " should be known", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity: " + name + " should have the correct value", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;foo&amp;bar&gt;quux&quot;";
        String expected = "<foo&amp;bar>quux\"";
        assertEquals("Escape", Entities.escape(input, null), expected);
        assertEquals("Unescape", Entities.unescape(Entities.escape(input, null)), input);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        String input = "&lt;foo&amp;bar&gt;quux&quot;";
        String output = "&lt;foo&amp;amp;bar&amp;gt;quux&amp;quot;";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.base);
        assertEquals("Escape with settings", output, Entities.escape(input, settings));
    }

    @Test
    public void testCanEncode() {
        Map<Character, String> full = Entities.full;
        for (Character character : full.values()) {
            assertEquals("Character " + character + " should be encodable", true, Entities.canEncode(Entities.CoreCharset.utf, character, null));
        }
    }

    @Test
    public void testCanEncodeWithFallback() {
        Map<Character, String> full = Entities.full;
        for (Character character : full.values()) {
            if (character > 0xFFFF) { // surrogate pairs
                String chars = new String(Character.toChars(character));
                assertEquals("Character " + character + " should not be encodable", false, Entities.canEncode(Entities.CoreCharset.ascii, character, null));
                assertEquals("Character " + character + " should be encodable with fallback", true, Entities.canEncode(Entities.CoreCharset.utf, character, Entities.CoreCharset.ascii.fallback));
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap<String, Character>();
        try {
            InputStream in = Entities.class.getResourceAsStream("/entities-full.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load entities file", e);
        }

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String name = entry.getKey();
            int hexCode = Integer.parseInt(entry.getValue(), 16);
            Character character = Character.valueOf((char) hexCode);
            entities.put(name, character);
        }

        Map<Character, String> expected = Entities.fullByVal;
        assertEquals("Loaded entities should match expected ones", expected, entities);
    }
}