import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(true, settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        assertEquals("div", settings.normalizeTag(tagName));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "ID";
        assertEquals("id", settings.normalizeAttribute(attributeName));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.asList().get(0).getKey());
        assertEquals("123", normalizedAttributes.asList().get(0).getValue());
    }
}