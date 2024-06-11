package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);

        assertTrue(htmlDefault.preserveTagCase() == false);
        assertTrue(preserveCase.preserveTagCase() == true);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings preserveCase = new ParseSettings(true, false);

        String tagName1 = "DIV";
        String tagName2 = "P";
        String tagName3 = "SPAN";

        assertEquals(tagName1, preserveCase.normalizeTag(tagName1));
        assertEquals(tagName2, preserveCase.normalizeTag(tagName2));
        assertEquals(tagName3, preserveCase.normalizeTag(tagName3));

        ParseSettings htmlDefault = new ParseSettings(false, false);
        String tagName4 = "DIV";
        String tagName5 = "P";
        String tagName6 = "SPAN";

        assertEquals("div", htmlDefault.normalizeTag(tagName1));
        assertEquals("p", htmlDefault.normalizeTag(tagName2));
        assertEquals("span", htmlDefault.normalizeTag(tagName3));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);

        assertTrue(htmlDefault.preserveAttributeCase() == false);
        assertTrue(preserveCase.preserveAttributeCase() == true);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings preserveCase = new ParseSettings(true, false);

        String attributeName1 = "class";
        String attributeName2 = "id";
        String attributeName3 = "style";

        assertEquals(attributeName1, preserveCase.normalizeAttribute(attributeName1));
        assertEquals(attributeName2, preserveCase.normalizeAttribute(attributeName2));
        assertEquals(attributeName3, preserveCase.normalizeAttribute(attributeName3));

        ParseSettings htmlDefault = new ParseSettings(false, false);
        String attributeName4 = "class";
        String attributeName5 = "id";
        String attributeName6 = "style";

        assertEquals("class", htmlDefault.normalizeAttribute(attributeName1));
        assertEquals("id", htmlDefault.normalizeAttribute(attributeName2));
        assertEquals("style", htmlDefault.normalizeAttribute(attributeName3));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings preserveCase = new ParseSettings(true, false);

        Attributes attributes = new Attributes();
        attributes.put("class", "someClass");
        attributes.put("id", "someId");
        attributes.put("style", "someStyle");

        ParseSettings htmlDefault = new ParseSettings(false, false);

        assertEquals(3, preserveCase.normalizeAttributes(attributes).size());
        assertEquals("someClass", preserveCase.normalizeAttributes(attributes).get("class"));
        assertEquals("someId", preserveCase.normalizeAttributes(attributes).get("id"));
        assertEquals("someStyle", preserveCase.normalizeAttributes(attributes).get("style"));

        assertEquals(3, htmlDefault.normalizeAttributes(attributes).size());
        assertEquals("someclass", htmlDefault.normalizeAttributes(attributes).get("class"));
        assertEquals("someid", htmlDefault.normalizeAttributes(attributes).get("id"));
        assertEquals("someStyle", htmlDefault.normalizeAttributes(attributes).get("style"));
    }
}