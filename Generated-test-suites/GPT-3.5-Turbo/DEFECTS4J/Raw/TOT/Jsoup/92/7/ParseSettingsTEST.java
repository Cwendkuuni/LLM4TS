import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testNotPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNotPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "div";
        assertEquals(tagName, settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTagNotPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        assertEquals("div", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "class";
        assertEquals(attributeName, settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributeNotPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "CLASS";
        assertEquals("class", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("myId", normalizedAttributes.get("id"));
        assertEquals("myClass", normalizedAttributes.get("class"));
    }

    @Test
    public void testNormalizeAttributesNotPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "myId");
        attributes.put("CLASS", "myClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("myid", normalizedAttributes.get("id"));
        assertEquals("myclass", normalizedAttributes.get("class"));
    }

}