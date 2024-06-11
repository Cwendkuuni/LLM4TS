import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);

        String tagName = "Div";
        String normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);

        tagName = "SPAN";
        normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("span", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);

        String attributeName = "HRef";
        String normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", normalizedAttribute);

        attributeName = "Src";
        normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("src", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("HRef", "https://www.example.com");
        attributes.put("ID", "123456");
        attributes.put("Class", "main");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("123456", normalizedAttributes.get("id"));
        assertEquals("main", normalizedAttributes.get("class"));
    }
}