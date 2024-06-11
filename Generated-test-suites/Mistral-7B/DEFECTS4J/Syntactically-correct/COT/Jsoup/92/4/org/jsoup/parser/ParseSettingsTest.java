package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("HTML");
        assertEquals("html", tagName);

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("testclass", normalizedAttributes.get("class").toLowerCase());
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = settings.normalizeTag("HTML");
        assertEquals("HTML", tagName);

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("testClass", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = settings.normalizeAttribute("CLASS");
        assertEquals("CLASS", attributeName);

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("testClass", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testPreserveCaseAttributes() {
        ParseSettings settings = ParseSettings.preserveCase;
        Attributes attributes = new Attributes();
        attributes.put("Class", "testClass");
        attributes.put("id", "testId");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("Class", normalizedAttributes.get("Class").getValue());
        assertEquals("testClass", normalizedAttributes.get("Class").getValue());
        assertEquals("id", normalizedAttributes.get("id").getValue());
        assertEquals("testId", normalizedAttributes.get("id").getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNameParameter() {
        ParseSettings settings = ParseSettings.htmlDefault;
        settings.normalizeTag(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributesParameter() {
        ParseSettings settings = ParseSettings.htmlDefault;
        settings.normalizeAttributes(null);
    }
}