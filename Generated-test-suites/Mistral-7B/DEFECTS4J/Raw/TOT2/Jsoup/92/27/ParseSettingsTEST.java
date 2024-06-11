package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagNameUpperCase = "DIV";
        String tagNameLowerCase = tagNameUpperCase.toLowerCase();
        String normalizedTagUpperCase = settings.normalizeTag(tagNameUpperCase);
        String normalizedTagLowerCase = settings.normalizeTag(tagNameLowerCase);

        assert (normalizedTagUpperCase.equals(tagNameUpperCase));
        assert (normalizedTagLowerCase.equals(tagNameLowerCase));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeNameUpperCase = "data-test-id";
        String attributeNameLowerCase = attributeNameUpperCase.toLowerCase();
        String normalizedAttributeUpperCase = settings.normalizeAttribute(attributeNameUpperCase);
        String normalizedAttributeLowerCase = settings.normalizeAttribute(attributeNameLowerCase);

        assert (normalizedAttributeUpperCase.equals(attributeNameUpperCase));
        assert (normalizedAttributeLowerCase.equals(attributeNameLowerCase));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("data-test-id", "test123");
        attributes.put("class", "myClass");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String attributeDataTestId = normalizedAttributes.get("data-test-id");
        String attributeClass = normalizedAttributes.get("class");

        assert (attributeDataTestId.equalsIgnoreCase("data-test-id"));
        assert (attributeClass.equalsIgnoreCase("myClass"));
    }

    @Test
    public void testEmptyTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "";
        String normalizedTag = settings.normalizeTag(tagName);

        assert (normalizedTag.equals(""));
    }

    @Test
    public void testNullTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = null;

        try {
            String normalizedTag = settings.normalizeTag(tagName);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testLongTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "aReallyLongTagNameThatCouldCauseProblems";
        String normalizedTag = settings.normalizeTag(tagName);

        assert (normalizedTag.equals("aReallyLongTagNameThatCouldCauseProblems"));
    }

    @Test
    public void testSpecialCharacterTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "tag-with-special-characters!";
        String normalizedTag = settings.normalizeTag(tagName);

        assert (normalizedTag.equals("tag-with-special-characters!"));
    }

    @Test
    public void testEmptyAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assert (normalizedAttribute.equals(""));
    }

    @Test
    public void testNullAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = null;

        try {
            String normalizedAttribute = settings.normalizeAttribute(attributeName);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testLongAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "attribute-name-with-a-really-long-name-that-could-cause-problems";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assert (normalizedAttribute.equals("attribute-name-with-a-really-long-name-that-could-cause-problems"));
    }

    @Test
    public void testSpecialCharacterAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "attribute-with-special-characters!";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assert (normalizedAttribute.equals("attribute-with-special-characters!"));
    }

    @Test
    public void testNullAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = null;

        try {
            Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}