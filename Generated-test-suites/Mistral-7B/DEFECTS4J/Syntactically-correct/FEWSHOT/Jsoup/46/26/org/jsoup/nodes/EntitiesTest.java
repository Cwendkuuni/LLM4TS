package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character c = entry.getValue();
            assertTrue("Named entity '" + name + "' not found in Entities.full", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' has wrong character", c, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String name = entry.getKey();
            Character c = entry.getValue();
            assertTrue("Named entity '" + name + "' not found in Entities.base", Entities.isBaseNamedEntity(name));
            assertEquals("Named entity '" + name + "' has wrong character", c, Entities.getCharacterByName(name));
        }

        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character c = entry.getValue();
            assertFalse("Named entity '" + name + "' found in Entities.base", Entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character c = entry.getValue();
            assertEquals("Named entity '" + name + "' has wrong character", c, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsNamedEntityWithInvalidEntity() {
        assertFalse("Invalid named entity should not be found", Entities.isNamedEntity("invalid"));
        assertNull("Invalid named entity should return null", Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;lt;foo&quot; bar &nbsp; &nbsp; &nbsp; <script>alert('XSS')</script>";
        EscapeMode escapeMode = EscapeMode.base;
        String expectedOutput = "&amp;amp;lt; &amp;lt;foo&amp;quot; &amp;quot; bar &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;lt;script&amp;gt;&amp;lt;script&amp;gt;alert('XSS')&amp;lt;script&amp;gt;";
        assertEquals("Escaped string does not match expected output", expectedOutput, Entities.escape(input, new Document.OutputSettings().escapeMode(escapeMode)));
    }

    @Test
    public void testEscapeWithXHTML() {
        String input = "&amp;lt;foo&quot; bar &nbsp; &nbsp; &nbsp; <script>alert('XSS')</script>";
        EscapeMode escapeMode = EscapeMode.xhtml;
        String expectedOutput = "&amp;lt; &amp;lt;foo&amp;quot; &amp;quot; bar &amp;nbsp; &amp;nbsp; &amp;nbsp;";
        assertEquals("Escaped string does not match expected output with XHTML", expectedOutput, Entities.escape(input, new Document.OutputSettings().escapeMode(escapeMode)));
    }

    @Test
    public void testEscapeWithUTF8() {
        String input = "𝄞 foo 𝄞 bar 𝄞";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.encoder(Charset.forName("UTF-8"));
        String expectedOutput = "&#x1F001; &amp;foo; &#x1F001; bar &#x1F001;";
        assertEquals("Escaped string does not match expected output with UTF-8", expectedOutput, Entities.escape(input, outputSettings));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&quot; bar &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;lt;script&gt;alert('XSS')&lt;script&gt;";
        String expectedOutput = "&lt;foo&quot; bar \t \t \t <script>alert('XSS')</script>";
        assertEquals("Unescaped string does not match expected output", expectedOutput, Entities.unescape(input));
    }
}