package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (char c : Entities.full.keySet()) {
            String name = Character.toString(c);
            assertTrue("Named entity " + name + " should be known", Entities.isNamedEntity(name));
        }

        assertFalse("Unknown named entity should not be known", Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (char c : Entities.base.keySet()) {
            String name = Character.toString(c);
            assertTrue("Base named entity " + name + " should be known", Entities.isBaseNamedEntity(name));
        }

        for (char c : Entities.full.keySet()) {
            if (!Entities.base.containsKey(Character.toString(c))) {
                String name = Character.toString(c);
                assertFalse("Named entity " + name + " should not be known as base named entity", Entities.isBaseNamedEntity(name));
            }
        }

        assertFalse("Unknown named entity should not be known as base named entity", Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        for (char c : Entities.full.keySet()) {
            String name = Character.toString(c);
            Character character = Entities.getCharacterByName(name);
            assertEquals("Named entity " + name + " should return character " + character, c, character);
        }

        assertNull("Unknown named entity should return null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeModeXhtml() {
        assertEquals("&quot;", Entities.escape(String.valueOf('"'), Entities.EscapeMode.xhtml));
        assertEquals("&amp;", Entities.escape('&', Entities.EscapeMode.xhtml));
        assertEquals("<", Entities.escape('<', Entities.EscapeMode.xhtml));
        assertEquals(">", Entities.escape('>', Entities.EscapeMode.xhtml));

        assertEquals("&quot; &amp; < &gt;", Entities.escape("\"&<>", Entities.EscapeMode.xhtml));
    }

    @Test
    public void testEscapeModeBase() {
        assertEquals("&quot;", Entities.escape(String.valueOf('"'), Entities.EscapeMode.base));
        assertEquals("&amp;", Entities.escape('&', Entities.EscapeMode.base));
        assertEquals("<", Entities.escape('<', Entities.EscapeMode.base));
        assertEquals(">", Entities.escape('>', Entities.EscapeMode.base));

        assertEquals("&quot; &quot; &amp; &amp; < &lt; > &gt;", Entities.escape("\"\"&amp;&lt;&gt;", Entities.EscapeMode.base));
    }

    @Test
    public void testEscapeModeExtended() {
        assertEquals("&quot;", Entities.escape(String.valueOf('"'), Entities.EscapeMode.extended));
        assertEquals("&amp;", Entities.escape('&', Entities.EscapeMode.extended));
        assertEquals("<", Entities.escape('<', Entities.EscapeMode.extended));
        assertEquals(">", Entities.escape('>', Entities.EscapeMode.extended));

        assertEquals("&quot; &quot; &amp; &amp; #xA0; &lt; &gt; &apos; &quot; &amp; &quot; &amp; #x26; &amp; #x22;", Entities.escape("\"\"&amp;&lt;&gt;&apos;&quot;&amp;&amp;&amp;", Entities.EscapeMode.extended));
    }

    @Test
    public void testEscape() {
        assertEquals("&quot; &amp; &lt; &gt; &apos;", Entities.escape("\"&<>&apos;", null));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"&<>&apos;", Entities.unescape(Entities.escape("\"&<>&apos;", null)));
    }
}