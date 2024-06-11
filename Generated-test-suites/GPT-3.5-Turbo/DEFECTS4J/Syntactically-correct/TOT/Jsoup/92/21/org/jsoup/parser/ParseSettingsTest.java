
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_preserveTagCase() {
        ParseSettings preserveTagCaseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        
        assertEquals(tagName, preserveTagCaseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTag_notPreserveTagCase() {
        ParseSettings notPreserveTagCaseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        
        assertEquals("div", notPreserveTagCaseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute_preserveAttributeCase() {
        ParseSettings preserveAttributeCaseSettings = new ParseSettings(false, true);
        String attributeName = "HtMl";
        
        assertEquals(attributeName, preserveAttributeCaseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttribute_notPreserveAttributeCase() {
        ParseSettings notPreserveAttributeCaseSettings = new ParseSettings(false, false);
        String attributeName = "HtMl";
        
        assertEquals("html", notPreserveAttributeCaseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttribute_emptyString() {
        String attributeName = "";
        
        assertEquals("", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes_preserveAttributeCase() {
        ParseSettings preserveAttributeCaseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes().put("HtML", "value");
        
        Attributes normalizedAttributes = preserveAttributeCaseSettings.normalizeAttributes(attributes);
        
        assertEquals(attributes.toString(), normalizedAttributes.toString());
    }

    @Test
    public void testNormalizeAttributes_notPreserveAttributeCase() {
        ParseSettings notPreserveAttributeCaseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes().put("HtML", "value");
        
        Attributes normalizedAttributes = notPreserveAttributeCaseSettings.normalizeAttributes(attributes);
        
        assertEquals("html=\"value\"", normalizedAttributes.toString());
    }
}
