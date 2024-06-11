package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import static org.jsoup.internal.Normalizer.lowerCase;

public class ParseSettingsTest {
    @Test
    public void testHtmlDefaultParseSettings() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        Assert.assertFalse(htmlDefault.preserveTagCase());
        Assert.assertFalse(htmlDefault.preserveAttributeCase());

        String tagName = "DIV";
        String result = htmlDefault.normalizeTag(tagName);
        Assert.assertEquals("div", result);

        String attributeName = "CLASS";
        String attributeValue = "class1 class2";
        Attributes attributes = new Attributes();
        attributes.put(attributeName, attributeValue);
        Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
        String normalizedAttributeName = normalizedAttributes.get(attributeName);
        Assert.assertEquals("class", normalizedAttributeName);
        Assert.assertEquals(attributeValue, normalizedAttributes.get(attributeName));
    }

    @Test
    public void testPreserveCaseParseSettings() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        Assert.assertTrue(preserveCase.preserveTagCase());
        Assert.assertTrue(preserveCase.preserveAttributeCase());

        String tagName = "DIV";
        String result = preserveCase.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        String attributeName = "CLASS";
        String attributeValue = "class1 class2";
        Attributes attributes = new Attributes();
        attributes.put(attributeName, attributeValue);
        Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
        String normalizedAttributeName = normalizedAttributes.get(attributeName);
        Assert.assertEquals("CLASS", normalizedAttributeName);
        Assert.assertEquals(attributeValue, normalizedAttributes.get(attributeName));
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        String result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIv";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIv";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "dIV";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIV ";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIV\t";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIV\r\n";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "DIV\r\n \t";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("DIV", result);

        tagName = "";
        result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("", result);

        tagName = null;
        Assert.assertNull(parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "CLASS";
        String attributeValue = "class1 class2";
        String result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS";
        result = parseSettings.normalizeAttribute(attributeName.toUpperCase());
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS";
        result = parseSettings.normalizeAttribute(attributeName.toLowerCase());
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS ";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = " CLASS ";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS\t";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS\r\n";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = "CLASS\r\n\t";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("CLASS", result);

        attributeName = "";
        result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("", result);

        attributeName = null;
        Assert.assertNull(parseSettings.normalizeAttribute(attributeName));

        attributeValue = "";
        attributes = new Attributes();
        attributes.put(attributeName, attributeValue);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertTrue(normalizedAttributes.isEmpty());
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName1 = "CLASS";
        String attributeName2 = "ID";
        String attributeValue1 = "class1 class2";
        String attributeValue2 = "id1 id2";
        Attributes attributes = new Attributes();
        attributes.put(attributeName1, attributeValue1);
        attributes.put(attributeName2, attributeValue2);
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        String normalizedAttributeName1 = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("CLASS", normalizedAttributeName1);
        Assert.assertEquals(attributeValue1, normalizedAttributes.get(attributeName1));

        String normalizedAttributeName2 = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("ID", normalizedAttributeName2);
        Assert.assertEquals(attributeValue2, normalizedAttributes.get(attributeName2));

        attributeName1 = "CLASS";
        attributeValue1 = "class1 class2 class3";
        attributes = new Attributes();
        attributes.put(attributeName1, attributeValue1);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        normalizedAttributeName1 = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("CLASS", normalizedAttributeName1);
        Assert.assertEquals(attributeValue1, normalizedAttributes.get(attributeName1));

        attributeName2 = "ID";
        attributeValue2 = "id1 id2 id3";
        attributes = new Attributes();
        attributes.put(attributeName2, attributeValue2);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        normalizedAttributeName2 = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("ID", normalizedAttributeName2);
        Assert.assertEquals(attributeValue2, normalizedAttributes.get(attributeName2));

        attributeName1 = "CLASS";
        attributeValue1 = "class1 class2";
        attributes = new Attributes();
        attributes.put(attributeName1, attributeValue1);
        ParseSettings preserveAttributeCaseParseSettings = new ParseSettings(false, true);
        normalizedAttributes = preserveAttributeCaseParseSettings.normalizeAttributes(attributes);

        normalizedAttributeName1 = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("CLASS", normalizedAttributeName1);
        Assert.assertEquals(attributeValue1, normalizedAttributes.get(attributeName1));

        attributeName2 = "ID";
        attributeValue2 = "id1 id2";
        attributes = new Attributes();
        attributes.put(attributeName2, attributeValue2);
        parseSettings = new ParseSettings(true, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        normalizedAttributeName2 = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("ID", normalizedAttributeName2);
        Assert.assertEquals(attributeValue2, normalizedAttributes.get(attributeName2));
    }
}