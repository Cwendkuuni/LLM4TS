import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        // Typical use case
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());

        // Edge case
        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());

        // Error scenario
        try {
            settings = new ParseSettings(null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Tag preservation cannot be null", e.getMessage());
        }
    }

    @Test
    public void testPreserveAttributeCase() {
        // Typical use case
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());

        // Edge case
        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());

        // Error scenario
        try {
            settings = new ParseSettings(false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Attribute preservation cannot be null", e.getMessage());
        }
    }

    @Test
    public void testNormalizeTag() {
        // Typical use case
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));

        // Edge case
        settings = new ParseSettings(false, false);
        assertEquals("div", settings.normalizeTag("div"));

        // Error scenario
        try {
            settings.normalizeTag(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Tag name cannot be null", e.getMessage());
        }
    }

    @Test
    public void testNormalizeAttribute() {
        // Typical use case
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("CLASS", settings.normalizeAttribute("class"));

        // Edge case
        settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute("class"));

        // Error scenario
        try {
            settings.normalizeAttribute(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Attribute name cannot be null", e.getMessage());
        }
    }

    @Test
    public void testNormalizeAttributes() {
        // Typical use case
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("Class", "test");
        attributes.put("id", "testId");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("test", normalizedAttributes.getIgnoreCase("class"));
        assertEquals("testId", normalizedAttributes.getIgnoreCase("id"));

        // Edge case
        settings = new ParseSettings(false, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("test", normalizedAttributes.getIgnoreCase("class"));
        assertEquals("testid", normalizedAttributes.getIgnoreCase("id"));

        // Error scenario
        try {
            settings.normalizeAttributes(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Attributes cannot be null", e.getMessage());
        }
    }
}