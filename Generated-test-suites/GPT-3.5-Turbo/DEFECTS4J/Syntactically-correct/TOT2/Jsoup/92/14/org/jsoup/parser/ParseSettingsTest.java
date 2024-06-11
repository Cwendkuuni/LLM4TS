import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = ParseSettings.htmlDefault;
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("div", parseSettings.normalizeTag("   DIV   "));
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        assertEquals("DIV", preserveCaseSettings.normalizeTag("   DIV   "));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("id", parseSettings.normalizeAttribute("   ID   "));
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        assertEquals("ID", preserveCaseSettings.normalizeAttribute("   ID   "));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "value");
        attributes.put("Id", "value");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertFalse(normalizedAttributes.hasKey("class"));
        assertFalse(normalizedAttributes.hasKey("Id"));
    }

    @Test
    public void testExceptionHandling() {
        try {
            parseSettings.normalizeTag(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Success
        }
    }
}