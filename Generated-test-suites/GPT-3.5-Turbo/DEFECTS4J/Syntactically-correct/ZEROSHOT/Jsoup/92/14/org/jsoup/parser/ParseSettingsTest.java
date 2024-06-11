import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        boolean result = parseSettings.preserveTagCase();
        assertEquals(true, result);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        boolean result = parseSettings.preserveAttributeCase();
        assertEquals(true, result);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String result = parseSettings.normalizeTag("TestTag");
        assertEquals("TestTag", result);

        parseSettings = new ParseSettings(false, true);
        result = parseSettings.normalizeTag("TestTag");
        assertEquals("testtag", result);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String result = parseSettings.normalizeAttribute("TestAttribute");
        assertEquals("TestAttribute", result);

        parseSettings = new ParseSettings(false, true);
        result = parseSettings.normalizeAttribute("TestAttribute");
        assertEquals("testattribute", result);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("Attribute1", "Value1");
        attributes.put("Attribute2", "Value2");

        Attributes result = parseSettings.normalizeAttributes(attributes);

        assertEquals(attributes, result);

        parseSettings = new ParseSettings(false, true);
        attributes = new Attributes();
        attributes.put("Attribute1", "Value1");
        attributes.put("Attribute2", "Value2");

        result = parseSettings.normalizeAttributes(attributes);

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("attribute1", "Value1");
        expectedAttributes.put("attribute2", "Value2");

        assertEquals(expectedAttributes, result);
    }
}