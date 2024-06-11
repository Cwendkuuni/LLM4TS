
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "aBc";
        assertEquals("abc", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HrEf";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HrEf", "https://example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("href", normalizedAttributes.getKey());
        assertEquals("https://example.com", normalizedAttributes.getValue());
    }

    @Test
    public void testHtmlDefault() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertFalse(htmlDefault.preserveTagCase());
        assertFalse(htmlDefault.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertTrue(preserveCase.preserveTagCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }
}
