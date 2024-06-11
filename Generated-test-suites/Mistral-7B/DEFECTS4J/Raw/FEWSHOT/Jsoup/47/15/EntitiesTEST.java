package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(value, Entities.getCharacterByName(name));
        }

        // test unknown entity
        assertFalse(Entities.isNamedEntity("UnknownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character name = entry.getKey();
            String value = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(value));
            assertEquals(name, Entities.getCharacterByName(value));
        }

        // test unknown entity
        assertFalse(Entities.isBaseNamedEntity("UnknownEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertEquals(value, Entities.getCharacterByName(name));
        }

        // test unknown entity
        assertNull(Entities.getCharacterByName("UnknownEntity"));
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
            Character name = entry.getKey();
            String value = entry.getValue();
            assertEquals("&" + value + ";", Entities.escape(value, Entities.EscapeMode.xhtml));
        }
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character name = entry.getKey();
            String value = entry.getValue();
            if (name == '\u00022') { // quot
                assertEquals("&quot;", Entities.escape(value, Entities.EscapeMode.base));
            } else if (name == '\u00026') { // amp
                assertEquals("&amp;", Entities.escape(value, Entities.EscapeMode.base));
            } else if (name == '\u0003C') { // lt
                assertEquals("&lt;", Entities.escape(value, Entities.EscapeMode.base));
            } else if (name == '\u0003E') { // gt
                assertEquals("&gt;", Entities.escape(value, Entities.EscapeMode.base));
            } else {
                String str = Character.toString(name);
                assertEquals("&" + str + ";", Entities.escape(str, Entities.EscapeMode.base));
            }
        }
    }

    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> fullByVal = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullByVal.entrySet()) {
            Character name = entry.getKey();
            String value = entry.getValue();
            assertEquals("&#x" + Integer.toHexString(name.codePointAt(0)) + ";", Entities.escape(Character.toString(name), Entities.EscapeMode.extended));
        }
    }
}