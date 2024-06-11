import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String attributeName = "class";
        String expectedTagName = "html";
        String expectedAttributeName = "class";

        String tag = settings.normalizeTag(tagName);
        String attr = settings.normalizeAttribute(attributeName);

        assertEquals(expectedTagName, tag);
        assertEquals(expectedAttributeName, attr);
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName = "class";
        String expectedTagName = "DIV";
        String expectedAttributeName = "class";

        String tag = settings.normalizeTag(tagName);
        String attr = settings.normalizeAttribute(attributeName);

        assertEquals(expectedTagName, tag);
        assertEquals(expectedAttributeName, attr);
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName = "CLASS";
        String expectedTagName = "DIV";
        String expectedAttributeName = "CLASS";

        String tag = settings.normalizeTag(tagName);
        String attr = settings.normalizeAttribute(attributeName);

        assertEquals(expectedTagName, tag);
        assertEquals(expectedAttributeName, attr);
    }

    @Test(expected = NullPointerException.class)
    public void testNullName_NormalizeTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = null;

        settings.normalizeTag(tagName);
    }

    @Test(expected = NullPointerException.class)
    public void testNullName_NormalizeAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = null;

        settings.normalizeAttribute(attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("CLASS", "testClass");
        attributes.put("name", "testName");

        Attributes normalized = settings.normalizeAttributes(attributes);

        String id = normalized.get("id");
        String className = normalized.get("class");
        String name = normalized.get("name");

        assertEquals("test", id);
        assertEquals("testClass", className);
        assertEquals("testName", name);
    }

    @Test
    public void testPreserveCaseNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        attributes.put("CLASS", "testClass");
        attributes.put("NAME", "testName");

        Attributes normalized = settings.normalizeAttributes(attributes);

        String id = normalized.get("id");
        String className = normalized.get("class");
        String name = normalized.get("name");

        assertEquals("ID", id);
        assertEquals("CLASS", className);
        assertEquals("NAME", name);
    }
}