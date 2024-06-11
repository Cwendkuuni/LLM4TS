package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.getFullMap();

        for (Map.Entry<String, Character> entity : fullEntities.entrySet()) {
            String name = entity.getKey();
            Character character = entity.getValue();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown entity should not be known", Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.getBaseMap();

        for (Map.Entry<String, Character> entity : baseEntities.entrySet()) {
            String name = entity.getKey();
            Character character = entity.getValue();
            assertTrue("Base named entity '" + name + "' should be known", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown entity should not be known in base set", Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.getFullMap();

        for (Map.Entry<String, Character> entity : fullEntities.entrySet()) {
            String name = entity.getKey();
            Character character = entity.getValue();
            assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        assertNull("Unknown named entity should return null", Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        CharsetEncoder encoder = Character.encodingForName("UTF-8");
        settings.encoder(encoder);

        String input = "&lt;foo&gt;bar&amp;baz&quot;quux";
        String expectedOutput = "&#x3C;foo&#x3E;bar&amp;amp;baz&quot;quux";

        String output = Entities.escape(input, settings);
        assertEquals("Escaped output should match expected", expectedOutput, output);

        String input2 = "foo<bar baz=\"quux\" quux=\"quux\" quux>";
        String expectedOutput2 = "foo&lt;bar baz=&quot;quux&quot; quux=&quot;quux&quot; quux";

        String output2 = Entities.escape(input2, settings);
        assertEquals("Escaped output should match expected", expectedOutput2, output2);
    }

    @Test
    public void testUnescape() {
        String input = "&#x3C;foo&#x3E;bar&amp;amp;baz&quot;quux";
        String expectedOutput = "&lt;foo&gt;bar&amp;baz&quot;quux";

        String output = Entities.unescape(input);
        assertEquals("Unescaped output should match expected", expectedOutput, output);

        String input2 = "foo&lt;bar baz=&quot;quux&quot; quux";
        String expectedOutput2 = "foo<bar baz=\"quux\" quux>";

        String output2 = Entities.unescape(input2);
        assertEquals("Unescaped output should match expected", expectedOutput2, output2);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.ascii;

        for (char c = 0; c < 128; c++) {
            if (Entities.canEncode(charset, c, null)) {
                assertTrue("ASCII character " + (int) c + " should be encodable", true);
            } else {
                fail("ASCII character " + (int) c + " should be encodable");
            }
        }

        for (char c = 128; c < 0x10000; c++) {
            if (Character.isSupplementaryCodePoint(c)) {
                continue;
            }
            CharsetEncoder encoder = Character.encodingForName("UTF-8");
            boolean canEncode = Entities.canEncode(CoreCharset.utf, c, encoder);
            assertTrue("Unicode character " + (int) c + " should be encodable", canEncode);
        }

        char supplementary = Character.toCodePoint(0x1D302);
        CharsetEncoder encoder = Character.encodingForName("UTF-8");
        assertFalse("Unicode supplementary character " + supplementary + " should not be encodable", Entities.canEncode(CoreCharset.utf, supplementary, encoder));
    }
}