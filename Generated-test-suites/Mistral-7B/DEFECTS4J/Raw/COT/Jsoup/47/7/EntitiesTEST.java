package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "nbsp", "gt"};
        for (String name : names) {
            assertTrue(entities.isNamedEntity(name));
        }

        String[] invalidNames = {"invalidName", "&", "\u0000"};
        for (String name : invalidNames) {
            assertFalse(entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "nbsp", "gt"};
        for (String name : names) {
            assertTrue(entities.isBaseNamedEntity(name));
        }

        String[] invalidNames = {"invalidName", "&", "\u0000"};
        for (String name : invalidNames) {
            assertFalse(entities.isBaseNamedEntity(name));
        }

        String[] baseNamesNotInFull = {"&apos;", "&eacute;", "&egrave;", "&iacute;", "&oacute;", "&uacute;", "&aelig;", "&eth;", "&thorn;", "&yuml;", "&OElig;", "&Scaron;", "&acute;", "&Aring;", "&Ccedil;", "&Eta;", "&ensp;", "&emsp;", "&Thorn;", "&fnof;", "&ndash;"};
        for (String name : baseNamesNotInFull) {
            assertFalse(entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "nbsp", "gt"};
        Character[] expected = {'<', '&', '"', '\u00A0', '>'};
        for (int i = 0; i < names.length; i++) {
            assertEquals(expected[i], entities.getCharacterByName(names[i]));
        }

        String invalidName = "invalidName";
        try {
            entities.getCharacterByName(invalidName);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithInvalidName() {
        Entities entities = new Entities();
        String invalidName = "invalidName";
        entities.getCharacterByName(invalidName);
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        Entities entities = new Entities();
        String input = "<test>&amp;<test>";
        String expectedOutput = "&lt;test&amp;&amp;gt;";
        String output = entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        Entities entities = new Entities();
        String input = "<test>&amp;<test>";
        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String expectedOutput = "&lt;test&gt;";
        String output = entities.escape(input, xhtmlSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithCustomSettings() {
        Entities entities = new Entities();
        String input = "<test>&amp;<test>";
        Document.OutputSettings customSettings = new Document.OutputSettings();
        customSettings.setEscapeMode(Entities.EscapeMode.extended);
        String expectedOutput = "&lt;test&amp;amp;&amp;gt;";
        String output = entities.escape(input, customSettings);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithInvalidOutputSettings() {
        Entities entities = new Entities();
        Document.OutputSettings invalidSettings = new Document.OutputSettings();
        invalidSettings.setEscapeMode(null);
        String input = "<test>&amp;<test>";
        entities.escape(input, invalidSettings);
    }

    @Test
    public void testUnescapeMethodWithDefaultSettings() {
        Entities entities = new Entities();
        String input = "&lt;test&amp;&amp;gt;";
        String expectedOutput = "<test>&amp;<test>";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodWithStrictSettings() {
        Entities entities = new Entities();
        String input = "&lt;test&amp;&amp;gt;";
        Document.OutputSettings strictSettings = new Document.OutputSettings();
        strictSettings.setEscapeMode(Document.OutputSettings.EscapeMode.htmlParanoid);
        String expectedOutput = "<test>&amp;<test>";
        String output = entities.unescape(input, strictSettings);
        assertEquals(expectedOutput, output);
    }
}