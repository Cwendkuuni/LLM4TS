package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        boolean result1 = Entities.isNamedEntity("lt");
        assertTrue(result1);

        // Edge case - unknown entity
        boolean result2 = Entities.isNamedEntity("xyz");
        assertFalse(result2);

        // Error scenario - null input
        boolean result3 = Entities.isNamedEntity(null);
        assertFalse(result3);
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        boolean result1 = Entities.isBaseNamedEntity("lt");
        assertTrue(result1);

        // Edge case - unknown entity
        boolean result2 = Entities.isBaseNamedEntity("xyz");
        assertFalse(result2);

        // Error scenario - null input
        boolean result3 = Entities.isBaseNamedEntity(null);
        assertFalse(result3);
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Character result1 = Entities.getCharacterByName("lt");
        assertEquals('<', (char) result1);

        // Edge case - unknown entity
        Character result2 = Entities.getCharacterByName("xyz");
        assertNull(result2);

        // Error scenario - null input
        Character result3 = Entities.getCharacterByName(null);
        assertNull(result3);
    }

    @Test
    public void testEscape() {
        // Typical use case
        String input1 = "<p>Hello World</p>";
        String result1 = Entities.escape(input1, new Document.OutputSettings());
        assertEquals("&lt;p&gt;Hello World&lt;/p&gt;", result1);

        // Edge case - empty string
        String input2 = "";
        String result2 = Entities.escape(input2, new Document.OutputSettings());
        assertEquals("", result2);

        // Error scenario - null input
        String input3 = null;
        String result3 = Entities.escape(input3, new Document.OutputSettings());
        assertNull(result3);
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String input1 = "&lt;p&gt;Hello World&lt;/p&gt;";
        String result1 = Entities.unescape(input1);
        assertEquals("<p>Hello World</p>", result1);

        // Edge case - empty string
        String input2 = "";
        String result2 = Entities.unescape(input2);
        assertEquals("", result2);

        // Error scenario - null input
        String input3 = null;
        String result3 = Entities.unescape(input3);
        assertNull(result3);
    }

    @Test
    public void testUnescapeStrict() {
        // Typical use case
        String input1 = "&lt;p&gt;Hello World&lt;/p&gt;";
        String result1 = Entities.unescape(input1, true);
        assertEquals("<p>Hello World</p>", result1);

        // Edge case - empty string
        String input2 = "";
        String result2 = Entities.unescape(input2, true);
        assertEquals("", result2);

        // Error scenario - null input
        String input3 = null;
        String result3 = Entities.unescape(input3, true);
        assertNull(result3);
    }

    @Test
    public void testEscapeWithAttribute() {
        // Typical use case
        String input1 = "<p class=\"message\">Hello World</p>";
        Document.OutputSettings out1 = new Document.OutputSettings();
        out1.escapeMode(Entities.EscapeMode.xhtml);
        String result1 = Entities.escape(input1, out1);
        assertEquals("<p class=&quot;message&quot;>Hello World</p>", result1);

        // Edge case - empty string
        String input2 = "";
        Document.OutputSettings out2 = new Document.OutputSettings();
        out2.escapeMode(Entities.EscapeMode.xhtml);
        String result2 = Entities.escape(input2, out2);
        assertEquals("", result2);

        // Error scenario - null input
        String input3 = null;
        Document.OutputSettings out3 = new Document.OutputSettings();
        out3.escapeMode(Entities.EscapeMode.xhtml);
        String result3 = Entities.escape(input3, out3);
        assertNull(result3);
    }
}