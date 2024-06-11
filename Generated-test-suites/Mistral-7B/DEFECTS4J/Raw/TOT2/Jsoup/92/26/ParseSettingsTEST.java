import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default
        assertFalse(settings.preserveTagCase());

        ParseSettings settingsPreserveTag = new ParseSettings(true, false); // Preserve tag case
        assertTrue(settingsPreserveTag.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default
        assertFalse(settings.preserveAttributeCase());

        ParseSettings settingsPreserveAttribute = new ParseSettings(false, true); // Preserve attribute case
        assertTrue(settingsPreserveAttribute.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        String tagNameUpperCase = "DIV";
        String normalizedTagNameUpperCase = settings.normalizeTag(tagNameUpperCase);
        assertEquals("div", normalizedTagNameUpperCase);

        String tagNameEmpty = "";
        String normalizedTagNameEmpty = settings.normalizeTag(tagNameEmpty);
        assertEquals("", normalizedTagNameEmpty);
    }

    @Test
    public void testNormalizeTag_PreserveCase() {
        ParseSettings settings = new ParseSettings(true, false); // Preserve tag case

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default

        String attrName = "myAttribute";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("myattribute", normalizedAttrName);

        String attrNameUpperCase = "MyAttribute";
        String normalizedAttrNameUpperCase = settings.normalizeAttribute(attrNameUpperCase);
        assertEquals("myattribute", normalizedAttrNameUpperCase);

        String attrNameEmpty = "";
        String normalizedAttrNameEmpty = settings.normalizeAttribute(attrNameEmpty);
        assertEquals("", normalizedAttrNameEmpty);
    }

    @Test
    public void testNormalizeAttribute_PreserveCase() {
        ParseSettings settings = new ParseSettings(true, true); // Preserve tag and attribute case

        String attrName = "myAttribute";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("myattribute", normalizedAttrName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidArgument() {
        new ParseSettings(true, "invalid"); // Invalid boolean for attribute case
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default
        Attributes attributes = new Attributes();
        attributes.put("myattr", "value");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("myattribute", normalizedAttributes.get("myattr").getValue());

        Attributes emptyAttributes = new Attributes();
        Attributes normalizedEmptyAttributes = settings.normalizeAttributes(emptyAttributes);
        assertNotNull(normalizedEmptyAttributes);
        assertTrue(normalizedEmptyAttributes.isEmpty());

        Attributes attributesWithInvalidName = new Attributes();
        attributesWithInvalidName.put("myattr:", "value");

        try {
            ParseSettings.normalizeAttributes(attributesWithInvalidName);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributesNullAttributes() {
        ParseSettings settings = new ParseSettings(false, false); // HTML default
        Attributes attributes = null;

        ParseSettings.normalizeAttributes(attributes);
        fail();
    }
}