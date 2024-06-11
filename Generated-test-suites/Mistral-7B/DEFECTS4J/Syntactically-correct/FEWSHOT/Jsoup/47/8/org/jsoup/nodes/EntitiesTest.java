package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.getEntities();
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertTrue(Entities.isNamedEntity(Character.toString(key)));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.getBaseEntities();
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertTrue(Entities.isBaseNamedEntity(Character.toString(key)));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.getEntities();
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            Character result = Entities.getCharacterByName(name);
            Character expected = key;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String input = "&lt;script&gt;alert(&quot;Hello World!&quot;);&lt;/script&gt;";
        String expected = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt;";
        String result = Entities.escape(input, out);
        assertEquals(expected, result);

        input = "&lt;script&gt;alert(&quot;Hello World!&quot;);&lt;/script&gt;<Br>";
        expected = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt;&#xA;<Br>";
        result = Entities.escape(input, out);
        assertEquals(expected, result);

        input = "&lt;script&gt;alert(&quot;Hello World!&quot;);&lt;/script&gt; <Br>";
        expected = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt; &amp;#xA;<Br>";
        result = Entities.escape(input, out);
        assertEquals(expected, result);

        out.escapeMode(Entities.EscapeMode.XHTML);
        input = "&lt;script&gt;alert(&quot;Hello World!&quot;);&lt;/script&gt; <Br>";
        expected = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt; &amp;#xA;<Br>";
        result = Entities.escape(input, out);
        assertEquals(expected, result);

        out.escapeMode(Entities.EscapeMode.EXTENDED);
        input = "&lt;script&gt;alert(&quot;Hello World!&quot;);&lt;/script&gt; <Br>";
        expected = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt; &amp;#xA;<Br>";
        result = Entities.escape(input, out);
        assertEquals(expected, result);

        out.encoder(StandardCharsets.UTF_8);
        input = "ñño";
        expected = "&#xC3;&#xBF;&#xC3;&#xB1;";
        result = Entities.escape(input, out);
        assertEquals(expected, result);

        out.encoder(StandardCharsets.UTF_8);
        input = "😃";
        expected = "&#x1F600;";
        result = Entities.escape(input, out);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt;";
        String expected = "<script>alert(\"Hello World!\");</script>";
        String result = Entities.unescape(input);
        assertEquals(expected, result);

        input = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt;<Br>";
        expected = "<script>alert(\"Hello World!\");</script>\n";
        result = Entities.unescape(input);
        assertEquals(expected, result);

        input = "&amp;lt;script&amp;gt;alert(&amp;quot;Hello World!&amp;quot;);&amp;lt;/script&amp;gt; <Br>";
        expected = "<script>alert(\"Hello World!\");</script> \n";
        result = Entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        CoreCharset charset = CoreCharset.utf;
        for (char c = 0; c <= 0x10FFFF; c++) {
            boolean canEncode = Entities.canEncode(charset, c, encoder);
            if (Character.isSupplementaryCodePoint(c)) {
                assertTrue(!canEncode);
                continue;
            }
            if (c < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
                assertTrue(Character.isDefined(c));
                assertTrue(canEncode);
                continue;
            }
            if (Character.isHighSurrogate(c)) {
                char highSurrogate = c;
                char lowSurrogate = (char) (0xDC00 + Character.codePointAt(new String(Character.toChars(highSurrogate)), 0).charValue());
                assertTrue(Character.isDefined(highSurrogate));
                assertTrue(Character.isDefined(lowSurrogate));
                assertTrue(canEncode);
                continue;
            }
            String str = Character.toString(c);
            assertTrue(encoder.canEncode(str));
            assertTrue(canEncode);
        }
    }
}