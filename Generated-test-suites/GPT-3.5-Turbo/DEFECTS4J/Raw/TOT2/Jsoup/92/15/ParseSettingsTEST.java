import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(true, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertEquals(true, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "PReSeRvE";
        assertEquals("preserve", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HeLLoWorlD";
        assertEquals("helloworld", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Foo", "bar");
        attributes.put("Baz", "qux");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("bar", normalizedAttributes.get("foo"));
        assertEquals("qux", normalizedAttributes.get("baz"));
    }

    @Test
    public void testEdgeCaseNormalizeTag() {
        String emptyTagName = "    ";
        assertEquals("", parseSettings.normalizeTag(emptyTagName));
    }

    @Test
    public void testEdgeCaseNormalizeAttribute() {
        String emptyAttributeName = "   ";
        assertEquals("", parseSettings.normalizeAttribute(emptyAttributeName));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeNormalizeAttributes() {
        Attributes nullAttributes = null;
        parseSettings.normalizeAttributes(nullAttributes);
    }

}