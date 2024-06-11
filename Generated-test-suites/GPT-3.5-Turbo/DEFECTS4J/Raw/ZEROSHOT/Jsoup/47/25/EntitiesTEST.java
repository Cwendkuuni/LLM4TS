import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf((char) 34), Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("Hello & World <", new Document.OutputSettings());
        Assert.assertEquals("Hello &amp; World &lt;", escapedString);
    }

    @Test
    public void testEscapeInAttribute() {
        String escapedString = Entities.escape("Hello & World <", new Document.OutputSettings(), true, false, false);
        Assert.assertEquals("Hello &amp; World <", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &amp; World &lt;");
        Assert.assertEquals("Hello & World <", unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("Hello &amp; World &lt;", true);
        Assert.assertEquals("Hello &amp; World &lt;", unescapedString);
    }
}