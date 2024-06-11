package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertFalse(entities.isNamedEntity("aaa"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("aaa"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("aaa"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;&amp;<>&quot;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expectedOutput = "&amp;lt;&amp;amp;&amp;lt;&amp;quot;";
        String output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);

        // test with different output settings
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        expectedOutput = "&amp;lt;&amp;gt;&amp;quot;";
        output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;&amp;amp;&amp;lt;&amp;quot;";
        String expectedOutput = "&lt;&amp;&lt;&quot;";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset coreCharset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        for (char c = 0; c <= 0xFF; c++) {
            boolean expectedResult = (c < 0x80);
            boolean result = entities.canEncode(coreCharset, c, encoder);
            assertEquals(expectedResult, result);
        }
    }
}