import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        String input = "&lt; &amp; &apos; &gt;";
        String expected = "&amp;lt; &amp;amp; &amp;apos; &amp;gt;";
        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        Assert.assertEquals(expected, output);
    }

    // Test other methods here...
}