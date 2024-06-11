import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "div";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTagWithPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        assertEquals("DIV", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "class";
        assertEquals("class", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributeWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "CLASS";
        assertEquals("CLASS", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("class", "link");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertSame(attributes, normalizedAttributes);
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("class", "link");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertNotSame(attributes, normalizedAttributes);
        assertTrue(attributes.equals(normalizedAttributes));
    }

}