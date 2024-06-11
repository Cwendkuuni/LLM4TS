import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("DIV");
        Assert.assertEquals("div", tagName);

        String attrName = settings.normalizeAttribute("id");
        Assert.assertEquals("id", attrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalized = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalized.get("class"));
        Assert.assertArrayEquals(new String[]{"test"}, normalized.get("class").split("\\s+"));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("DIV");
        Assert.assertEquals("DIV", tagName);

        String attrName = settings.normalizeAttribute("id");
        Assert.assertEquals("id", attrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalized = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalized.get("class"));
        Assert.assertArrayEquals(new String[]{"test"}, normalized.get("class").split("\\s+"));
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String tagName = settings.normalizeTag("DIV");
        Assert.assertEquals("div", tagName);

        String attrName = settings.normalizeAttribute("ID");
        Assert.assertEquals("ID", attrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalized = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalized.get("class"));
        Assert.assertArrayEquals(new String[]{"test"}, normalized.get("class").split("\\s+"));
    }

    @Test
    public void testPreserveCaseTagAndAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = settings.normalizeTag("DIV");
        Assert.assertEquals("DIV", tagName);

        String attrName = settings.normalizeAttribute("ID");
        Assert.assertEquals("ID", attrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalized = settings.normalizeAttributes(attributes);
        Assert.assertEquals("class", normalized.get("class"));
        Assert.assertArrayEquals(new String[]{"test"}, normalized.get("class").split("\\s+"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = settings.normalizeTag(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeName() {
        ParseSettings settings = new ParseSettings(false, false);
        String attrName = settings.normalizeAttribute(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        settings.normalizeAttributes(null);
    }
}