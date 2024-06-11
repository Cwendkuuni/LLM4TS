import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("invalid");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("gt");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("quot");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("invalid");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', (char) result);

        result = Entities.getCharacterByName("amp");
        Assert.assertEquals('&', (char) result);

        result = Entities.getCharacterByName("gt");
        Assert.assertEquals('>', (char) result);

        result = Entities.getCharacterByName("quot");
        Assert.assertEquals('"', (char) result);

        result = Entities.getCharacterByName("invalid");
        Assert.assertNull(result);
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("&");
        Assert.assertEquals("&amp;", result);

        result = Entities.escape("<", null);
        Assert.assertEquals("&lt;", result);

        result = Entities.escape(">", null);
        Assert.assertEquals("&gt;", result);

        result = Entities.escape("\"", null);
        Assert.assertEquals("&quot;", result);

        result = Entities.escape(" ", null);
        Assert.assertEquals(" ", result);

        result = Entities.escape("\u00A0", null);
        Assert.assertEquals("\u00A0", result);

        result = Entities.escape("invalid", null);
        Assert.assertEquals("invalid", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&amp;");
        Assert.assertEquals("&", result);

        result = Entities.unescape("&lt;");
        Assert.assertEquals("<", result);

        result = Entities.unescape("&gt;");
        Assert.assertEquals(">", result);

        result = Entities.unescape("&quot;");
        Assert.assertEquals("\"", result);

        result = Entities.unescape("invalid");
        Assert.assertEquals("invalid", result);
    }
}