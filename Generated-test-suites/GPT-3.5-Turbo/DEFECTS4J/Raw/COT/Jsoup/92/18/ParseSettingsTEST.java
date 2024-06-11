
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setup() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings preserveCaseSettings = new ParseSettings(true, false);
        assertEquals("Tag", preserveCaseSettings.normalizeTag("Tag"));
    }
    
    @Test
    public void testNormalizeTagLowercase() {
        assertEquals("tag", parseSettings.normalizeTag("Tag"));
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings preserveCaseSettings = new ParseSettings(false, true);
        assertEquals("Attribute", preserveCaseSettings.normalizeAttribute("Attribute"));
    }

    @Test
    public void testNormalizeAttributeLowercase() {
        assertEquals("attribute", parseSettings.normalizeAttribute("Attribute"));
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings preserveCaseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        Attributes normalizedAttributes = preserveCaseSettings.normalizeAttributes(attributes);
        assertEquals("Value1", normalizedAttributes.get("Attr1"));
        assertEquals("Value2", normalizedAttributes.get("Attr2"));
    }

    @Test
    public void testNormalizeAttributesLowercase() {
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("value1", normalizedAttributes.get("attr1"));
        assertEquals("value2", normalizedAttributes.get("attr2"));
    }
}

