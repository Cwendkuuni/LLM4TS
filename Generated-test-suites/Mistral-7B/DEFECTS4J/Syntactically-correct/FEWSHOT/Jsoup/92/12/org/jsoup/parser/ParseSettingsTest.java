package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jsoup.internal.Normalizer.lowerCase;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        assertEquals("tag", tagNameLower);

        String attrNameLower = settings.normalizeAttribute("ATTR");
        assertEquals("attr", attrNameLower);

        Attributes attributes = new Attributes();
        attributes.put("Tag", "value");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("tag", attributes.get("tag").getValue());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagNamePreserved = settings.normalizeTag("TAG");
        assertEquals("TAG", tagNamePreserved);

        String attrNamePreserved = settings.normalizeAttribute("ATTR");
        assertEquals("ATTR", attrNamePreserved);

        Attributes attributes = new Attributes();
        attributes.put("Tag", "value");
        attributes = settings.normalizeAttributes(attributes);
        assertNotNull(attributes.get("tag"));
        assertEquals("TAG", attributes.get("tag").getValue());
    }
}