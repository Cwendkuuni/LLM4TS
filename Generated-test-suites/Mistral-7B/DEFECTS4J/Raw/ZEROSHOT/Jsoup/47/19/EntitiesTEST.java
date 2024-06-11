package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Entities class test file.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test isBaseNamedEntity method
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        // Test escape method
        String input = "&amp; < > &quot;";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "&amp; &amp; &lt; &gt; &amp;";

        String result = Entities.escape(input, settings);
        assertEquals(expectedOutput, result);

        settings.setEscapeMode(Entities.EscapeMode.xhtml);
        expectedOutput = "&amp; &amp; &lt; &gt;";
        result = Entities.escape(input, settings);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testUnescape() {
        // Test unescape method
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "& \n< \n> \"";

        String result = Entities.unescape(input);
        assertEquals(expectedOutput, result);

        input = "&amp; &lt; &gt; &quot; &#x00A0;";
        expectedOutput = "& \n< \n>\n ";
        result = Entities.unescape(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testCanEncode() {
        // Test canEncode method
        CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();

        // Basic ASCII characters
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', encoder));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'B', encoder));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'C', encoder));

        // Non-ASCII characters
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'é', encoder));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'µ', encoder));

        // UTF-8 encoding
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', encoder));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'µ', encoder));
    }
}