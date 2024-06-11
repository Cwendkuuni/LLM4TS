import org.junit.Test;
import org.junit.Assert;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_typical() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_edge() {
        boolean result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_error() {
        boolean result = Entities.isNamedEntity("abc");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_typical() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', result.charValue());
    }

    @Test
    public void testGetCharacterByName_edge() {
        Character result = Entities.getCharacterByName("quot");
        Assert.assertEquals('"', result.charValue());
    }

    @Test
    public void testGetCharacterByName_error() {
        Character result = Entities.getCharacterByName("abc");
        Assert.assertNull(result);
    }

    @Test
    public void testEscape_typical() {
        String result = Entities.escape("Hello < World", Document.OutputSettings.xhtml);
        Assert.assertEquals("Hello &lt; World", result);
    }

    @Test
    public void testEscape_edge() {
        String result = Entities.escape("&", Document.OutputSettings.base);
        Assert.assertEquals("&amp;", result);
    }

    @Test
    public void testEscape_error() {
        String result = Entities.escape("\uD835\uDF22", Document.OutputSettings.base);
        Assert.assertEquals("&#119042;", result);
    }

    @Test
    public void testUnescape_typical() {
        String result = Entities.unescape("Hello &lt; World");
        Assert.assertEquals("Hello < World", result);
    }

    @Test
    public void testUnescape_edge() {
        String result = Entities.unescape("&amp;");
        Assert.assertEquals("&", result);
    }

    @Test
    public void testUnescape_error() {
        String result = Entities.unescape("&#119042;");
        Assert.assertEquals("\uD835\uDF22", result);
    }
}