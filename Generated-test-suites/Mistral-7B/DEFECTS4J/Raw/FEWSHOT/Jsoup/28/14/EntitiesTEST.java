package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("not_exist"));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("not_exist"));
    }

    @Test
    public void testEscape() {
        String string = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        String expected = "&amp;lt;foo&amp;gt;bar&amp;amp;baz&amp;quot;quux&amp;apos;";
        assertEquals(expected, Entities.escape(string, null, Entities.EscapeMode.extended));

        string = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        expected = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        assertEquals(expected, Entities.escape(string, null, Entities.EscapeMode.base));

        string = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        expected = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        assertEquals(expected, Entities.escape(string, StandardCharsets.UTF_8, Entities.EscapeMode.xhtml));

        string = "&lt;foo&gt;bar&amp;baz&quot;quux&apos;";
        expected = "&amp;lt;foo&amp;gt;bar&amp;amp;baz&amp;quot;quux&amp;apos;";
        assertEquals(expected, Entities.escape(string, StandardCharsets.UTF_8, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String string = "&amp;lt;foo&amp;gt;bar&amp;amp;baz&amp;quot;quux&amp;apos;";
        String expected = "&lt;foo&gt;bar&amp;amp;baz&quot;quux&apos;";
        assertEquals(expected, Entities.unescape(string));

        string = "&amp;lt;foo&amp;gt;bar&amp;amp;baz&amp;quot;quux&amp;apos;";
        expected = "&lt;foo&gt;bar&amp;amp;baz&quot;quux&apos;";
        assertEquals(expected, Entities.unescape(string, false));

        string = "&amp;#34; &amp;#34; &quot; &quot; &amp;#34;";
        expected = "\" \" \" \"";
        assertEquals(expected, Entities.unescape(string, false));

        string = "&amp;#x3C; &amp;#x3E; &quot; &quot; &amp;#x3C;";
        expected = "&lt; &gt; &quot; &quot; &lt;";
        assertEquals(expected, Entities.unescape(string, false));

        string = "&amp;#x3C; &amp;#x3E; &quot; &quot; &amp;#x3C;";
        expected = "&lt; &gt; &quot; &quot; &lt;";
        assertEquals(expected, Entities.unescape(string, true));
    }
}