package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultPreserveTagCaseFalse() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "Div";
        String normalizeTag = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizeTag);
    }

    @Test
    public void testHtmlDefaultPreserveTagCaseTrue() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "Div";
        String normalizeTag = settings.normalizeTag(tagName);
        Assert.assertEquals("Div", normalizeTag);
    }

    @Test
    public void testHtmlDefaultPreserveAttributeCaseFalse() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = "class";
        String normalizeAttribute = settings.normalizeAttribute(attrName);
        Assert.assertEquals("class", normalizeAttribute);
    }

    @Test
    public void testHtmlDefaultPreserveAttributeCaseTrue() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = "class";
        String normalizeAttribute = settings.normalizeAttribute(attrName);
        Assert.assertEquals("class", normalizeAttribute);
    }

    @Test
    public void testPreserveCasePreserveTagCaseTrue() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "Div";
        String normalizeTag = settings.normalizeTag(tagName);
        Assert.assertEquals("Div", normalizeTag);
    }

    @Test
    public void testPreserveCasePreserveTagCaseFalse() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "Div";
        String normalizeTag = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizeTag);
    }

    @Test
    public void testPreserveCasePreserveAttributeCaseTrue() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attrName = "class";
        String normalizeAttribute = settings.normalizeAttribute(attrName);
        Assert.assertEquals("class", normalizeAttribute);
    }

    @Test
    public void testPreserveCasePreserveAttributeCaseFalse() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attrName = "class";
        String normalizeAttribute = settings.normalizeAttribute(attrName);
        Assert.assertEquals("class", normalizeAttribute);
    }

    @Test
    public void testNormalizeAttributesFalse() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("class", "foo bar baz");
        Attributes normalizeAttributes = settings.normalizeAttributes(attributes);
        String[] classNames = normalizeAttributes.get("class").split(" ");
        Assert.assertEquals(3, classNames.length);
        Assert.assertEquals("foo", classNames[0]);
        Assert.assertEquals("bar", classNames[1]);
        Assert.assertEquals("baz", classNames[2]);
    }

    @Test
    public void testNormalizeAttributesTrue() {
        ParseSettings settings = ParseSettings.preserveCase;
        Attributes attributes = new Attributes();
        attributes.put("class", "foo bar baz");
        Attributes normalizeAttributes = settings.normalizeAttributes(attributes);
        String classNames = normalizeAttributes.get("class");
        Assert.assertEquals("class='foo bar baz'", classNames);
    }
}