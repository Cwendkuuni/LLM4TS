import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultSettings() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tag1 = "HTML";
        String tag2 = "HEAD";
        String attr1 = "class";
        String attr2 = "id";
        String tagNameLowerCased = settings.normalizeTag(tag1);
        String tagNameLowerCasedExpected = "html";
        String tagNamePreserved = settings.normalizeTag(tag2);
        String tagNamePreservedExpected = "head";
        String attrNameLowerCased = settings.normalizeAttribute(attr1);
        String attrNameLowerCasedExpected = "class";
        String attrNamePreserved = settings.normalizeAttribute(attr2);
        String attrNamePreservedExpected = "id";

        assertEquals(tagNameLowerCasedExpected, tagNameLowerCased);
        assertEquals(tagNamePreservedExpected, tagNamePreserved);
        assertEquals(attrNameLowerCasedExpected, attrNameLowerCased);
        assertEquals(attrNamePreservedExpected, attrNamePreserved);

        Attributes attributes = new Attributes();
        attributes.put("class", "classValue");
        attributes.put("id", "idValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        String normalizedClassAttribute = normalizedAttributes.get("class");
        String normalizedClassAttributeExpected = "classvalue";
        String normalizedIdAttribute = normalizedAttributes.get("id");
        String normalizedIdAttributeExpected = "idvalue";

        assertEquals(normalizedClassAttributeExpected, normalizedClassAttribute);
        assertEquals(normalizedIdAttributeExpected, normalizedIdAttribute);
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tag1 = "HTML";
        String tag2 = "HEAD";
        String attr1 = "CLASS";
        String attr2 = "ID";
        String tagNameLowerCased = settings.normalizeTag(tag1);
        String tagNameLowerCasedExpected = "HTML";
        String tagNamePreserved = settings.normalizeTag(tag2);
        String tagNamePreservedExpected = "HEAD";
        String attrNameLowerCased = settings.normalizeAttribute(attr1);
        String attrNameLowerCasedExpected = "class";
        String attrNamePreserved = settings.normalizeAttribute(attr2);
        String attrNamePreservedExpected = "ID";

        assertEquals(tagNameLowerCasedExpected, tagNameLowerCased);
        assertEquals(tagNamePreservedExpected, tagNamePreserved);
        assertEquals(attrNameLowerCasedExpected, attrNameLowerCased);
        assertEquals(attrNamePreservedExpected, attrNamePreserved);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "classValue");
        attributes.put("ID", "idValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        String normalizedClassAttribute = normalizedAttributes.get("CLASS");
        String normalizedClassAttributeExpected = "CLASS";
        String normalizedIdAttribute = normalizedAttributes.get("ID");
        String normalizedIdAttributeExpected = "ID";

        assertEquals(normalizedClassAttributeExpected, normalizedClassAttribute);
        assertEquals(normalizedIdAttributeExpected, normalizedIdAttribute);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTagName() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = null;

        String normalizedTagName = settings.normalizeTag(tagName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeName() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = null;

        String normalizedAttrName = settings.normalizeAttribute(attrName);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        ParseSettings settings = ParseSettings.htmlDefault;

        settings.normalizeAttributes(null);
    }
}