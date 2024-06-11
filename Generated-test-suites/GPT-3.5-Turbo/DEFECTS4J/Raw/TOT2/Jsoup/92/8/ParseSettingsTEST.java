import static org.junit.Assert.*;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagCasePreservation() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeTagNoCasePreservation() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttributeCasePreservation() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributeNoCasePreservation() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributesCasePreservation() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.asList().get(0).getKey());
    }

    @Test
    public void testNormalizeAttributesNoCasePreservation() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.asList().get(0).getKey());
    }
}