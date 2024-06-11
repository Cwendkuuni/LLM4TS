import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Character expected = '<';
        Assert.assertEquals(expected, character);
    }

    @Test
    public void testEscape() {
        String string = "This is a <test> string";
        Document.OutputSettings out = new Document.OutputSettings();
        String escapedString = Entities.escape(string, out);
        String expected = "This is a &lt;test&gt; string";
        Assert.assertEquals(expected, escapedString);
    }

    @Test
    public void testUnescape() {
        String string = "This is a &lt;test&gt; string";
        String unescapedString = Entities.unescape(string);
        String expected = "This is a <test> string";
        Assert.assertEquals(expected, unescapedString);
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testIsNamedEntity_NonExistingEntity() {
        boolean result = Entities.isNamedEntity("nonexisting");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNamedEntity_NullName() {
        boolean result = Entities.isNamedEntity(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_NonExistingEntity() {
        Character character = Entities.getCharacterByName("nonexisting");
        Assert.assertNull(character);
    }

    @Test
    public void testGetCharacterByName_NullName() {
        Character character = Entities.getCharacterByName(null);
        Assert.assertNull(character);
    }

    @Test
    public void testEscape_NullString() {
        Document.OutputSettings out = new Document.OutputSettings();
        String escapedString = Entities.escape(null, out);
        Assert.assertNull(escapedString);
    }

    @Test
    public void testUnescape_NullString() {
        String unescapedString = Entities.unescape(null);
        Assert.assertNull(unescapedString);
    }

    @Test
    public void testEscape_UnencodableCharacter() {
        String string = "This contains a © unencodable character";
        Document.OutputSettings out = new Document.OutputSettings();
        String escapedString = Entities.escape(string, out);
        String expected = "This contains a &#169; unencodable character";
        Assert.assertEquals(expected, escapedString);
    }

    @Test
    public void testUnescape_Strict() {
        String string = "This is a &lt;test&gt; string";
        String unescapedString = Entities.unescape(string, true);
        String expected = string;
        Assert.assertEquals(expected, unescapedString);
    }

    @Test
    public void testUnescape_NonStrict() {
        String string = "This is a &lt;test&gt; string";
        String unescapedString = Entities.unescape(string, false);
        String expected = "This is a <test> string";
        Assert.assertEquals(expected, unescapedString);
    }

}