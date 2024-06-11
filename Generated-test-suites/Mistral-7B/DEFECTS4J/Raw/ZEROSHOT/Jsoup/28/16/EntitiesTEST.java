package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @org.junit.BeforeClass
    public static void setup() {
        // Initialize static variables and maps in Entities class
    }

    @org.junit.Test
    public void testIsNamedEntity() {
        for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
            for (Map<String, Character> map : new Map[] { Entities.full, Entities.base, Entities.xhtmlByVal }) {
                for (Map.Entry<String, Character> entry : map.entrySet()) {
                    String name = entry.getKey();
                    Character character = entry.getValue();
                    assertTrue(Entities.isNamedEntity(name));
                    assertEquals(character, Entities.getCharacterByName(name));
                }
            }
        }

        for (String name : new String[] {"!", "\"", "#", "%", "&", "'", "(", ")", "*", "+", "<", ">", "=", ">="}) {
            assertFalse(Entities.isNamedEntity(name));
        }
    }

    @org.junit.Test
    public void testEscape() {
        String input = "&lt;<&gt;<&apos;>&amp;<&quot;>";
        String expected = "&amp;lt;&amp;lt;>&amp;&gt;>&amp;apos;&amp;amp;&amp;quot;";

        for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
            String result = Entities.escape(input, null, mode);
            assertEquals(expected, result);
        }
    }

    @org.junit.Test
    public void testUnescape() {
        String input = "&amp;lt;&amp;lt;>&amp;&gt;>&amp;apos;&amp;amp;&amp;quot;";
        String expected = "&lt;&lt;>&gt;&gt;&apos;&amp;&quot;";

        String result = Entities.unescape(input);
        assertEquals(expected, result);

        String strictInput = "&amp;lt;&amp;lt;>&amp;&gt;>&amp;apos;&amp;amp;&amp;quot;&#x26;";
        String strictExpected = "&lt;&lt;>&gt;&gt;&apos;&amp;&quot;<";

        String strictResult = Entities.unescape(strictInput, true);
        assertEquals(strictExpected, strictResult);
    }
}