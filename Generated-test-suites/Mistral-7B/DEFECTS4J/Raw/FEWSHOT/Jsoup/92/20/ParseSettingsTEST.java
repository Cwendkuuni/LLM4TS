package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;

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
        Assert.assertEquals("class", normalizedAttributes.get("class"));
        Assert.assertNotNull(normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseForTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("DIV", normalizedTagName);
    }

    @Test
    public void testPreserveCaseForAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("DIV", normalizedTagName);

        String attributeName = "Class";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("Class", normalizedAttributeName);
    }

    @Test
    public void testPreserveCaseForBothTagAndAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("DIV", normalizedTagName);

        String attributeName = "Class";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("Class", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("Class", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("Class", normalizedAttributes.get("Class"));
        Assert.assertNotNull(normalizedAttributes.get("Class"));
    }

    @Test
    public void testNormalizeAttributesWithLowercase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveAttributeCase());

        Attributes attributes = new Attributes();
        attributes.put("Class", "foo");
        attributes.put("ID", "bar");
        attributes.put("data-testid", "baz");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalizedAttributes.get("class"));
        Assert.assertEquals("id", normalizedAttributes.get("id"));
        Assert.assertEquals("data-testid", normalizedAttributes.get("data-testid"));
    }

    @Test
    public void testNormalizeAttributesWithPreservedCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertFalse(settings.preserveAttributeCase());

        Attributes attributes = new Attributes();
        attributes.put("Class", "foo");
        attributes.put("ID", "bar");
        attributes.put("Data-testid", "baz");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("Class", normalizedAttributes.get("Class"));
        Assert.assertEquals("ID", normalizedAttributes.get("ID"));
        Assert.assertEquals("Data-testid", normalizedAttributes.get("Data-testid"));
    }

    @Test
    public void testNormalizeAttributesWithPreservedTagAndAttributeCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        Attributes attributes = new Attributes();
        attributes.put("Class", "foo");
        attributes.put("ID", "bar");
        attributes.put("Data-testid", "baz");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("Class", normalizedAttributes.get("Class"));
        Assert.assertEquals("ID", normalizedAttributes.get("ID"));
        Assert.assertEquals("Data-testid", normalizedAttributes.get("Data-testid"));
    }
}