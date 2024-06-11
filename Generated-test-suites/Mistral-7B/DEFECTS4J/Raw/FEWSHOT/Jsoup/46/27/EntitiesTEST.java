package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, Character> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity not recognized: " + key, Entities.isNamedEntity(value.toString()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, Character> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Base named entity not recognized: " + key, Entities.isBaseNamedEntity(value.toString()));
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, Character> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named entity value mismatch: " + key, value, Entities.getCharacterByName(value.toString()));
        }
    }

    @Test
    public void testEscapeModeXhtml() {
        Character[] entities = new Character[]{'<', '>', '&', '"'};
        for (Character entity : entities) {
            assertEquals("XHTML escape mode incorrect: " + entity, Entities.getCharacterByName("lt"), Entities.escape(String.valueOf(entity), Entities.EscapeMode.xhtml));
        }
    }

    @Test
    public void testEscapeModeBase() {
        Character[] entities = new Character[]{'<', '>', '&', '"'};
        for (Character entity : entities) {
            if (entity == '&') {
                assertEquals("Base escape mode incorrect: " + entity, "&amp;", Entities.escape(String.valueOf(entity), Entities.EscapeMode.base));
            } else {
                assertEquals("Base escape mode incorrect: " + entity, String.valueOf(entity), Entities.escape(String.valueOf(entity), Entities.EscapeMode.base));
            }
        }
    }

    @Test
    public void testEscapeModeExtended() {
        Character[] entities = new Character[]{'<', '>', '&', '"'};
        for (Character entity : entities) {
            if (entity == '&') {
                assertEquals("Extended escape mode incorrect: " + entity, "&amp;", Entities.escape(String.valueOf(entity), Entities.EscapeMode.extended));
            } else {
                assertEquals("Extended escape mode incorrect: " + entity, "&#x" + Integer.toHexString(entity.codePointAt(0)) + ";", Entities.escape(String.valueOf(entity), Entities.EscapeMode.extended));
            }
        }
    }

    @Test
    public void testUnescape() {
        String input = "&lt;Hello&amp; World&gt; &quot; &apos; &amp; &amp;amp; &amp;#x26; &amp;#x22;";
        String expectedOutput = "<Hello World> \" ' & & & & & &";
        assertEquals("Unescape test failed: expected \"" + expectedOutput + "\", but got \"" + Entities.unescape(input) + "\"", expectedOutput, Entities.unescape(input));
    }
}