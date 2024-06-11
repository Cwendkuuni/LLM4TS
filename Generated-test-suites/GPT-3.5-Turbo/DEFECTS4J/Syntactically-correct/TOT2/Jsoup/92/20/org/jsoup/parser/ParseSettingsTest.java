import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, true);
        String normalizedTag = settings.normalizeTag("DIV");
        assertEquals("DIV", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String normalizedAttribute = settings.normalizeAttribute("id");
        assertEquals("id", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("123", normalizedAttributes.get("ID"));
    }

    @Test
    public void testNormalizeTagWithoutPreservingTagCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String normalizedTag = settings.normalizeTag("DIV");
        assertEquals("div", normalizedTag);
    }

    @Test
    public void testNormalizeAttributeWithoutPreservingAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String normalizedAttribute = settings.normalizeAttribute("ID");
        assertEquals("id", normalizedAttribute);
    }

    @Test
    public void testNormalizeEmptyTag() {
        ParseSettings settings = new ParseSettings(true, true);
        String normalizedTag = settings.normalizeTag("");
        assertEquals("", normalizedTag);
    }

    @Test
    public void testNormalizeEmptyAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String normalizedAttribute = settings.normalizeAttribute("");
        assertEquals("", normalizedAttribute);
    }
}