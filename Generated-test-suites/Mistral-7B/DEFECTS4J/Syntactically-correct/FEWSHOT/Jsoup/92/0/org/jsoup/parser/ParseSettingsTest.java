package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import static org.jsoup.internal.Normalizer.lowerCase;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tagName = "Div";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizedTagName);

        String attributeName = "class";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("class", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalizedAttributes.keySet().iterator().next());
        Assert.assertEquals("foo", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttributeName);

        attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(attributeName, normalizedAttributes.keySet().iterator().next());
        Assert.assertEquals("foo", normalizedAttributes.get(attributeName));
    }

    @Test
    public void testPreserveCaseTagAndAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        tagName = "DIV";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);

        attributeName = "CLASS";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttributeName);

        attributes = new Attributes();
        attributes.put("CLASS", "foo");
        normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(attributeName, normalizedAttributes.keySet().iterator().next());
        Assert.assertEquals("foo", normalizedAttributes.get(attributeName));
    }

    @Test
    public void testPreserveCaseTagOnly() {
        ParseSettings settings = new ParseSettings(true, false);
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        tagName = "DIV";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);

        attributeName = "class";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("class", normalizedAttributeName);

        attributes = new Attributes();
        attributes.put("class", "foo");
        normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalizedAttributes.keySet().iterator().next());
        Assert.assertEquals("foo", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseAttributeOnly() {
        ParseSettings settings = new ParseSettings(false, true);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        tagName = "div";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizedTagName);

        attributeName = "CLASS";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttributeName);

        attributes = new Attributes();
        attributes.put("CLASS", "foo");
        normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("CLASS", normalizedAttributes.keySet().iterator().next());
        Assert.assertEquals("foo", normalizedAttributes.get(attributeName));
    }
}