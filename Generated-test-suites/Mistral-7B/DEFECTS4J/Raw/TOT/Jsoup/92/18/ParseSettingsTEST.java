import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "Div";
        String attributeName1 = "class";
        String attributeName2 = "id";

        // Test normalizeTag method with HTML default settings
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);

        // Test normalizeAttribute method with HTML default settings
        String normalizedAttribute1 = settings.normalizeAttribute(attributeName1);
        assertEquals("class", normalizedAttribute1);
        String normalizedAttribute2 = settings.normalizeAttribute(attributeName2);
        assertEquals("id", normalizedAttribute2);

        // Test normalizeAttributes method with HTML default settings
        Attributes attributes = new Attributes();
        attributes.put("class", "classValue");
        attributes.put("id", "idValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("classValue", normalizedAttributes.get("class"));
        assertEquals("idValue", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "CLASS";
        String attributeName2 = "ID";

        // Test normalizeTag method with preserve case settings
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTag);

        // Test normalizeAttribute method with preserve case settings
        String normalizedAttribute1 = settings.normalizeAttribute(attributeName1);
        assertEquals("CLASS", normalizedAttribute1);
        String normalizedAttribute2 = settings.normalizeAttribute(attributeName2);
        assertEquals("ID", normalizedAttribute2);

        // Test normalizeAttributes method with preserve case settings
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "classValue");
        attributes.put("ID", "idValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.get("CLASS"));
        assertEquals("ID", normalizedAttributes.get("ID"));
    }

    @Test
    public void testNormalizeTagWithEmptyString() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "";

        // Test normalizeTag method with empty string
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("", normalizedTag);
    }

    @Test
    public void testNormalizeAttributeWithEmptyString() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = "";

        // Test normalizeAttribute method with empty string
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        assertEquals("", normalizedAttribute);
    }
}