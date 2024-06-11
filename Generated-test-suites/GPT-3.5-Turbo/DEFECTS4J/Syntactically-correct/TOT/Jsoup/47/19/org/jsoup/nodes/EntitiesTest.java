import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities.EscapeMode;

public class EntitiesTest {

    // Test cases for isNamedEntity method

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_NotNamedEntity() {
        boolean result = Entities.isNamedEntity("abc");
        Assert.assertFalse(result);
    }

    // Test cases for isBaseNamedEntity method

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity_NotBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("gt");
        Assert.assertFalse(result);
    }

    // Test cases for getCharacterByName method

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', result.charValue());
    }

    @Test
    public void testGetCharacterByName_NullName() {
        Character result = Entities.getCharacterByName(null);
        Assert.assertNull(result);
    }

    // Test cases for escape method

    @Test
    public void testEscape() {
        String input = "This is a <sample> string.";
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(input, out);
        Assert.assertEquals("This is a &lt;sample&gt; string.", result);
    }

    @Test
    public void testEscape_EmptyString() {
        String input = "";
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(input, out);
        Assert.assertEquals("", result);
    }

    // Test cases for unescape method

    @Test
    public void testUnescape() {
        String input = "This is a &lt;sample&gt; string.";
        String result = Entities.unescape(input);
        Assert.assertEquals("This is a <sample> string.", result);
    }

    @Test
    public void testUnescape_EmptyString() {
        String input = "";
        String result = Entities.unescape(input);
        Assert.assertEquals("", result);
    }

}