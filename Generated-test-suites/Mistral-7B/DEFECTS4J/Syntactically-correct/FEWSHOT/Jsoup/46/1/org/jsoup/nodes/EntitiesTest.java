package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, Character> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(String.valueOf(key));
            assertEquals(expected, actual);
        }

        for (Map.Entry<Character, Character> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(String.valueOf(key));
            assertEquals(expected, actual);
        }

        for (Map.Entry<Character, Character> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isBaseNamedEntity(String.valueOf(key));
            assertEquals(expected, actual);
        }

        for (Character key : new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '=', '[', ']', '{', '}', '|', ';', ':', '"', '<', '>', '>', '?', '/', '\\'}) {
            boolean actual = Entities.isNamedEntity(String.valueOf(key));
            assertFalse(actual);
        }

        for (Character key : new char[]{'a', 'b', 'c'}) {
            boolean actual = Entities.isNamedEntity(String.valueOf(key));
            assertFalse(actual);
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, Character> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            Character actual = Entities.getCharacterByName(String.valueOf(key));
            assertEquals(value, actual);
        }
    }

    @Test
    public void testEscape() {
        StringBuilder expected = new StringBuilder();
        String input = "this is a &lt;test&gt; with &amp; special characters: \'single quote\' &apos;, \"double quote\" &quot;, < less than, > greater than, and & ampersand &entityRef;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.enablePrettyPrinting(true);
        outputSettings.setEscapeMode(Entities.EscapeMode.extended);

        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, input, outputSettings, false, false, false);

        expected.append("this is a &lt;test&gt; with &amp;amp; special characters: &apos;single quote&apos;, &quot;double quote&quot;, &lt;less than&gt;, &gt;greater than&gt;, and &amp;amp;amp; ampersand &amp;entityRef;");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUnescape() {
        String input = "this is a &lt;test&gt; with &amp;amp; special characters: &apos;single quote&apos;, &quot;double quote&quot;, < less than, > greater than, and & ampersand &entityRef;";
        String expected = "this is a <test> with amp special characters: 'single quote', \"double quote\", < less than, > greater than, and & ampersand entityRef;";

        String actual = Entities.unescape(input, false);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        for (char c = 0; c <= 0x10FFFF; c++) {
            boolean actual = Entities.canEncode(charset, c, null);
            if (Character.isSupplementaryCodePoint(c)) {
                assertFalse(actual);
            } else {
                assertTrue(actual);
            }
        }
    }
}