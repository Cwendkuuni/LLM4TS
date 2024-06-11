package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

import static org.jsoup.nodes.Entities.isBaseNamedEntity;
import static org.jsoup.nodes.Entities.isNamedEntity;
import static org.jsoup.nodes.Entities.getCharacterByName;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String[] namedEntities = {"lt", "gt", "amp", "quot"};
        for (String entity : namedEntities) {
            assertTrue(isNamedEntity(entity));
        }

        String[] nonNamedEntities = {"abc", "xyz", "123", "DEF"};
        for (String entity : nonNamedEntities) {
            assertFalse(isNamedEntity(entity));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        String[] baseNamedEntities = {"lt", "gt", "amp", "quot"};
        for (String entity : baseNamedEntities) {
            assertTrue(isBaseNamedEntity(entity));
        }

        String[] nonBaseNamedEntities = {"nbsp", "copy", "trademark", "reg"};
        for (String entity : nonBaseNamedEntities) {
            assertFalse(isBaseNamedEntity(entity));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = getCharacterByName("lt");
        Character gt = getCharacterByName("gt");
        Character amp = getCharacterByName("amp");
        Character quot = getCharacterByName("quot");

        assertEquals('<', lt.charValue());
        assertEquals('>', gt.charValue());
        assertEquals('&', amp.charValue());
        assertEquals('"', quot.charValue());

        String nonEntity = "abc";
        try {
            Character c = getCharacterByName(nonEntity);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidGetCharacterByName() {
        getCharacterByName(null);
    }

    @Test
    public void testEscapeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String source = "&lt;abcd&gt;<e&f>";
        String escaped = Entities.escape(source, outputSettings);

        String expected = "&amp;lt;abcd&amp;gt;&amp;amp;lt;e&amp;amp;amp;f&amp;gt;";
        assertEquals(expected, escaped);
    }

    @Test
    public void testEscapeWithXHTMLMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String source = "&lt;abcd&gt;<e&f>";
        String escaped = Entities.escape(source, outputSettings);

        String expected = "&lt;abcd&gt;&amp;lt;e&amp;gt;";
        assertEquals(expected, escaped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities.escape(null, outputSettings);
    }
}