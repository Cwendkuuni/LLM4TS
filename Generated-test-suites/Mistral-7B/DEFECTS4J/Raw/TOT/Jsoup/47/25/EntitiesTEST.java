import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        Character amp = Entities.getCharacterByName("amp");
        Character quot = Entities.getCharacterByName("quot");

        Assert.assertEquals('<', lt);
        Assert.assertEquals('&', amp);
        Assert.assertEquals('"', quot);

        Assert.assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>hello&quot; world&gt;";
        String expectedOutput = "&amp;amp; &amp;lt; &amp;lt;hello&amp;quot; &amp;quot; world&amp;gt;";

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, null, false, false, false);

        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "<title>&lt;hello&quot; world&gt;</title>";
        String expectedOutput = "<title>&amp;lt; &amp;lt;hello&amp;quot; &amp;quot; world&amp;gt;</title>";

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, null, true, false, false);

        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;amp; &amp;quot; &amp;gt;";
        String expectedOutput = "&lt; &amp; &quot; &gt;";

        String output = Entities.unescape(input, false);

        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNull() {
        Entities.unescape(null, false);
    }

    @Test
    public void testCanEncode() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, ' À'));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.utf, 0xFFFE));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.utf, 0xFFFF));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Character amp = entities.get("amp");
        Character quot = entities.get("quot");

        Assert.assertEquals('&', amp);
        Assert.assertEquals('"', quot);

        Character nonexistent = entities.get("nonexistent");
        Assert.assertNull(nonexistent);
    }
}