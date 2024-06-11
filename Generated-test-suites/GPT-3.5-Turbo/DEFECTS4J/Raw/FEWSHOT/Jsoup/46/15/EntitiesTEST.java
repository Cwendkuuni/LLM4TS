package org.jsoup.nodes;

import static org.junit.Assert.*;
import java.util.*;
import org.jsoup.parser.Parser;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("html"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("quo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("html"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("html"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("quo"));
    }

    @Test
    public void testEscape() {
        String string = "<b>Example String</b>";
        String expected = "&lt;b&gt;Example String&lt;/b&gt;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String result = Entities.escape(string, outputSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;b&gt;Example String&lt;/b&gt;";
        String expected = "<b>Example String</b>";
        String result = Entities.unescape(string);
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeWithTypeXHTML() {
        String string = "<b>Example String</b>";
        String expected = "&lt;b&gt;Example String&lt;/b&gt;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.EscapeMode.xhtml);
        String result = Entities.escape(string, outputSettings);
        assertEquals(expected, result);
    }

}