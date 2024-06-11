import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Boolean actual = settings.preserveTagCase();
        Boolean expected = true;
        assert(actual.equals(expected));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Boolean actual = settings.preserveAttributeCase();
        Boolean expected = true;
        assert(actual.equals(expected));
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String actual = settings.normalizeTag("TAG");
        String expected = "tag";
        assert(actual.equalsIgnoreCase(expected));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String actual = settings.normalizeAttribute("ATTR");
        String expected = "attr";
        assert(actual.equalsIgnoreCase(expected));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("name1", "value1");
        attributes.put("name2", "Value2");
        Attributes actual = settings.normalizeAttributes(attributes);
        Attributes expected = new Attributes();
        expected.put("name1", "name1");
        expected.put("name2", "name2");
        assert(actual.equals(expected));
    }

    @Test
    public void testPreserveTagCase_True() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag("TAG");
        String expected = "TAG";
        assert(actual.equals(expected));
    }

    @Test
    public void testPreserveAttributeCase_True() {
        ParseSettings settings = new ParseSettings(false, true);
        String actual = settings.normalizeAttribute("ATTR");
        String expected = "ATTR";
        assert(actual.equals(expected));
    }

    @Test(expected = AssertionError.class)
    public void testNormalizeTag_Null() {
        ParseSettings settings = new ParseSettings(false, false);
        String actual = settings.normalizeTag(null);
        assertNull(actual);
    }

    @Test(expected = AssertionError.class)
    public void testNormalizeAttribute_Null() {
        ParseSettings settings = new ParseSettings(false, false);
        String actual = settings.normalizeAttribute(null);
        assertNull(actual);
    }

    @Test
    public void testNormalizeAttributes_Null() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = null;
        Attributes actual = settings.normalizeAttributes(attributes);
        assertNull(actual);
    }

    @Test
    public void testNormalizeAttributes_Empty() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        Attributes actual = settings.normalizeAttributes(attributes);
        assert(actual.isEmpty());
    }
}