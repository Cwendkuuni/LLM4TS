package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        assertEquals("tag", tagNameLower);

        String attrNameLower = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("attribute", attrNameLower);

        Attributes attributesLower = settings.normalizeAttributes(new Attributes());
        attributesLower.put("TAG", "value");
        assertEquals("tag", attributesLower.get("tag"));
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagNameUpper = settings.normalizeTag("TAG");
        assertEquals("TAG", tagNameUpper);

        String attrNameUpper = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("ATTRIBUTE", attrNameUpper);

        Attributes attributesUpper = settings.normalizeAttributes(new Attributes());
        attributesUpper.put("TAG", "value");
        assertEquals("TAG", attributesUpper.get("TAG"));
    }

    @Test
    public void testParseSettingsPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagNameUpper = settings.normalizeTag("TAG");
        assertEquals("TAG", tagNameUpper);

        String attrNameLower = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("attribute", attrNameLower);

        Attributes attributesMixed = settings.normalizeAttributes(new Attributes());
        attributesMixed.put("TAG", "value");
        assertEquals("TAG", attributesMixed.get("TAG"));
        assertEquals("attribute", attributesMixed.get("attribute"));
    }

    @Test
    public void testParseSettingsPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        assertEquals("tag", tagNameLower);

        String attrNameUpper = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("ATTRIBUTE", attrNameUpper);

        Attributes attributesMixed = settings.normalizeAttributes(new Attributes());
        attributesMixed.put("TAG", "value");
        assertEquals("tag", attributesMixed.get("TAG"));
        assertEquals("ATTRIBUTE", attributesMixed.get("ATTRIBUTE"));
    }

    @Test
    public void testParseSettingsNull() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeTag(null));
        assertNull(settings.normalizeAttribute(null));
        assertSame(attributes -> attributes, settings.normalizeAttributes(null));
    }
}