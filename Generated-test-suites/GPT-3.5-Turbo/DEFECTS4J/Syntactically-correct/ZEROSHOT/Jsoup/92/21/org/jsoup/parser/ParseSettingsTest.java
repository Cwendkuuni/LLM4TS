import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, true);

        String tagName = "div";
        assertEquals("div", parseSettings.normalizeTag(tagName));

        tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));

        parseSettings = new ParseSettings(false, true);

        tagName = "div";
        assertEquals("div", parseSettings.normalizeTag(tagName));

        tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, true);

        String attributeName = "class";
        assertEquals("class", parseSettings.normalizeAttribute(attributeName));

        attributeName = "CLASS";
        assertEquals("class", parseSettings.normalizeAttribute(attributeName));

        parseSettings = new ParseSettings(true, false);

        attributeName = "class";
        assertEquals("class", parseSettings.normalizeAttribute(attributeName));

        attributeName = "CLASS";
        assertEquals("class", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, true);

        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("STYLE", "font-size: 12px");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("example", normalizedAttributes.get("class"));
        assertEquals("font-size: 12px", normalizedAttributes.get("style"));

        parseSettings = new ParseSettings(true, false);

        attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("STYLE", "font-size: 12px");

        normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("example", normalizedAttributes.get("class"));
        assertEquals("font-size: 12px", normalizedAttributes.get("style"));
    }
}