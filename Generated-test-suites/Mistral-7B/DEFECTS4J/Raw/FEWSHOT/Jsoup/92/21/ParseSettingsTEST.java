package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultSettings() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertTrue(htmlDefault.preserveTagCase() == false);
        assertTrue(htmlDefault.preserveAttributeCase() == false);
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertTrue(preserveCase.preserveTagCase() == true);
        assertTrue(preserveCase.preserveAttributeCase() == true);
    }

    @Test
    public void testNormalizeTag_preserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TAG", settings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeTag_dontPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeAttribute_preserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("ATTR_NAME", settings.normalizeAttribute("ATTR_NAME"));
    }

    @Test
    public void testNormalizeAttribute_dontPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("attr_name", settings.normalizeAttribute("ATTR_NAME"));
    }

    @Test
    public void testNormalizeAttributes_preserveAttributeCase() {
        String input = "class=\"CLASS_NAME\" id=\"ID_NAME\"";
        Attributes attributes = new Attributes();
        attributes.add("class", "CLASS_NAME");
        attributes.add("id", "ID_NAME");

        ParseSettings settings = new ParseSettings(false, true);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        String expected = input;
        String output = normalizedAttributes.toString();

        assertTrue(expected.equals(output));
    }

    @Test
    public void testNormalizeAttributes_dontPreserveAttributeCase() {
        String input = "CLASS=\"CLASS_NAME\" ID=\"ID_NAME\"";
        Attributes attributes = new Attributes();
        attributes.add("class", "CLASS_NAME");
        attributes.add("id", "ID_NAME");

        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        String expected = "class=\"className\" id=\"id_name\"";
        String output = normalizedAttributes.toString();

        assertTrue(expected.equals(output));
    }
}