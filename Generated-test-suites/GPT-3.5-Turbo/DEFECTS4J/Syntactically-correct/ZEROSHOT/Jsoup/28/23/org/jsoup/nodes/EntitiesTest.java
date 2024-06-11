package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Entities.EscapeMode;
import java.nio.charset.CharsetEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));

        assertFalse(Entities.isNamedEntity("xyz"));
        assertFalse(Entities.isNamedEntity("123"));
        assertFalse(Entities.isNamedEntity("@#$"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 0x0003E), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 0x00027), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));

        assertNull(Entities.getCharacterByName("xyz"));
        assertNull(Entities.getCharacterByName("123"));
        assertNull(Entities.getCharacterByName("@#$"));
    }

    @Test
    public void testEscape() {
        String string = "Hello&nbsp;World";
        Document.OutputSettings out = new Document.OutputSettings();
        EscapeMode escapeMode = EscapeMode.xhtml;

        assertEquals("Hello&nbsp;World", Entities.escape(string, out));
        assertEquals("Hello&nbsp;World", Entities.escape(string, out.encoder(), escapeMode));

        string = "<div>Hello World</div>";
        escapeMode = EscapeMode.base;

        assertEquals("&lt;div&gt;Hello World&lt;/div&gt;", Entities.escape(string, out));
        assertEquals("&lt;div&gt;Hello World&lt;/div&gt;", Entities.escape(string, out.encoder(), escapeMode));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;Hello World&lt;/div&gt;";
        assertFalse(string.contains("<"));

        assertEquals("<div>Hello World</div>", Entities.unescape(string));

        string = "&quot;Hello&nbsp;World&quot;";
        assertFalse(string.contains("\""));

        assertEquals("\"Hello World\"", Entities.unescape(string));

        string = "&amp;Hello&amp;World&amp;";
        assertFalse(string.contains("&"));

        assertEquals("&Hello&World&", Entities.unescape(string));

        string = "&lt;";
        assertFalse(string.contains("<"));

        assertEquals("<", Entities.unescape(string));

        string = "&#60;";
        assertFalse(string.contains("<"));

        assertEquals("<", Entities.unescape(string));

        string = "&ltg;";
        assertEquals("&ltg;", Entities.unescape(string));

        string = "&#xyz;";
        assertEquals("&#xyz;", Entities.unescape(string));

        string = "&123;";
        assertEquals("&123;", Entities.unescape(string));

        string = "&@#$;";
        assertEquals("&@#$;", Entities.unescape(string));
    }
}