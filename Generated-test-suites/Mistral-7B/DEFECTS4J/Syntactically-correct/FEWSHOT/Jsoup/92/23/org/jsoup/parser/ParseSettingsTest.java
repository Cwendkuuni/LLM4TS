package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;

import java.util.HashMap;
import java.util.Map;

public class ParseSettingsTest {

    @Test
    public void testPreserveCaseDefaults() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertEquals(false, htmlDefault.preserveTagCase());
        assertEquals(false, htmlDefault.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertEquals(true, preserveCase.preserveTagCase());
        assertEquals(true, preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagName() {
        ParseSettings settings = new ParseSettings(true, false);

        assertEquals("DIV", settings.normalizeTag("DIV"));
        assertEquals("div", settings.normalizeTag("DIV"));
        assertEquals("DIV", settings.normalizeTag("DIv"));
        assertEquals("DIV", settings.normalizeTag("DIvS"));
        assertEquals("DIV", settings.normalizeTag("DIv   "));

        // Edge cases
        assertEquals("", settings.normalizeTag(""));
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("TAG  "));
        assertEquals("TAG", settings.normalizeTag("TAG\t"));

        ParseSettings htmlSettings = new ParseSettings(false, false);
        assertEquals("div", htmlSettings.normalizeTag("DIV"));
        assertEquals("div", htmlSettings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("class", settings.normalizeAttribute("CLASS   "));
        assertEquals("class", settings.normalizeAttribute("CLASS\t"));
        assertEquals("class", settings.normalizeAttribute("CLASSes"));
        assertEquals("class", settings.normalizeAttribute("class attribute"));
        assertEquals("class", settings.normalizeAttribute("class attribute "));
        assertEquals("class", settings.normalizeAttribute("class attribute \t"));

        assertEquals("", settings.normalizeAttribute(""));
        assertEquals("attr", settings.normalizeAttribute("attr"));
        assertEquals("Attr", settings.normalizeAttribute("Attr"));
        assertEquals("ATTR", settings.normalizeAttribute("ATTR"));

        ParseSettings htmlSettings = new ParseSettings(false, false);
        assertEquals("class", htmlSettings.normalizeAttribute("class"));
        assertEquals("class", htmlSettings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        attributes.put("id", "id1");
        attributes.put("title", "title1 Title2");

        Attributes normalized = settings.normalizeAttributes(attributes);
        assertEquals("class1 class2", normalized.get("class"));
        assertEquals("class1 class2", normalized.get("CLASS"));
        assertEquals("id1", normalized.get("id"));
        assertEquals("title1 Title2", normalized.get("title"));
        assertEquals("title1 Title2", normalized.get("TITLE"));

        assertEquals(attributes.size(), normalized.size());
        assertEquals(attributes.toString(), normalized.toString());

        // Test empty attributes
        Attributes emptyAttributes = new Attributes();
        assertEquals(0, emptyAttributes.size());
        assertEquals("", emptyAttributes.toString());

        ParseSettings htmlSettings = new ParseSettings(false, false);
        assertEquals(2, htmlSettings.normalizeAttributes(attributes).size());
        assertEquals("class1 class2", htmlSettings.normalizeAttributes(attributes).get("class"));
        assertEquals("class1 class2", htmlSettings.normalizeAttributes(attributes).get("class"));
    }

    @Test
    public void testParseTagWithPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);

        Element tag = new Element("DIV");
        assertEquals("DIV", tag.tagName());

        Element parsedTag = settings.parseTag(new Element("DIV").html(), tag.tags());
        assertEquals("DIV", parsedTag.tagName());
    }

    @Test
    public void testParseTagWithLowercase() {
        ParseSettings settings = new ParseSettings(false, false);

        Element tag = new Element("DIV");
        assertEquals("DIV", tag.tagName());

        Element parsedTag = settings.parseTag(new Element("DIV").html(), tag.tags());
        assertEquals("div", parsedTag.tagName());
    }

    @Test
    public void testParseAttributesWithPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);

        Element tag = new Element("div");
        tag.attr("class", "class1 class2");
        tag.attr("id", "id1");
        tag.attr("title", "title1 Title2");

        Element parsedTag = settings.parseTag(tag.html(), tag.tags());

        assertEquals(3, parsedTag.attributes().size());
        assertEquals("class1 class2", parsedTag.attr("class"));
        assertEquals("class1 class2", parsedTag.attr("class"));
        assertEquals("id1", parsedTag.attr("id"));
        assertEquals("title1 Title2", parsedTag.attr("title"));
        assertEquals("title1 Title2", parsedTag.attr("TITLE"));
    }

    @Test
    public void testParseAttributesWithLowercase() {
        ParseSettings settings = new ParseSettings(false, false);

        Element tag = new Element("div");
        tag.attr("class", "class1 class2");
        tag.attr("id", "id1");
        tag.attr("title", "title1 Title2");

        Element parsedTag = settings.parseTag(tag.html(), tag.tags());

        assertEquals(3, parsedTag.attributes().size());
        assertEquals("class1 class2", parsedTag.attr("class"));
        assertEquals("class1 class2", parsedTag.attr("class"));
        assertEquals("id1", parsedTag.attr("id"));
        assertEquals("title1 Title2", parsedTag.attr("title"));
        assertEquals("title1 Title2", parsedTag.attr("title"));
    }

    @Test
    public void testParseTagWithMixedCase() {
        ParseSettings settings = new ParseSettings(true, true);

        Element tag = new Element("DIV");
        tag.attr("class", "class1 class2");
        tag.attr("id", "id1");
        tag.attr("title", "Title1 Title2");

        Element parsedTag = settings.parseTag(tag.html(), tag.tags());

        assertEquals("DIV", parsedTag.tagName());
        assertEquals(3, parsedTag.attributes().size());
        assertEquals("class1 class2", parsedTag.attr("class"));
        assertEquals("class1 class2", parsedTag.attr("CLASS"));
        assertEquals("id1", parsedTag.attr("id"));
        assertEquals("Title1 Title2", parsedTag.attr("title"));
        assertEquals("Title1 Title2", parsedTag.attr("TITLE"));
    }

    @Test
    public void testParseTagWithEmptyAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Element tag = new Element("div");
        Element parsedTag = settings.parseTag(tag.html(), tag.tags());

        assertEquals(0, parsedTag.attributes().size());
        assertEquals("", parsedTag.toString());
    }

    @Test
    public void testParseTagWithEmptyTag() {
        ParseSettings settings = new ParseSettings(false, false);

        Element parsedTag = settings.parseTag("", new TextNode(""));

        assertEquals(0, parsedTag.attributes().size());
        assertEquals("", parsedTag.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseTagWithNegativeIndex() {
        ParseSettings settings = new ParseSettings(false, false);

        Element tag = new Element("div");
        tag.attr("class", "class1 class2");
        tag.attr("id", "id1");

        Element parsedTag = settings.parseTag(tag.html(), new int[]{-1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTagWithNullTag() {
        ParseSettings settings = new ParseSettings(false, false);

        Element parsedTag = settings.parseTag(null, new TextNode(""));
    }

    @Test
    public void testParseTagWithNullHtml() {
        ParseSettings settings = new ParseSettings(false, false);

        Element parsedTag = settings.parseTag("", null);

        assertEquals(0, parsedTag.attributes().size());
        assertEquals("", parsedTag.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTagWithNullTags() {
        ParseSettings settings = new ParseSettings(false, false);

        Element parsedTag = settings.parseTag("", (TextNode[])null);
    }
}