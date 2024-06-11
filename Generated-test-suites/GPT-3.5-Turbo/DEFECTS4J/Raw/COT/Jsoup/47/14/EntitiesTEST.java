
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String name = "amp";
        boolean result = Entities.isNamedEntity(name);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        String name = "lt";
        boolean result = Entities.isBaseNamedEntity(name);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        String name = "gt";
        Character result = Entities.getCharacterByName(name);
        Character expected = '>';
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEscape() {
        String string = "<html>";
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(string, out);
        String expected = "&lt;html&gt;";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;html&gt;";
        String result = Entities.unescape(string);
        String expected = "<html>";
        Assert.assertEquals(expected, result);
    }
    
    // Test edge case where the input string is empty
    @Test
    public void testEscapeEmptyString() {
        String string = "";
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(string, out);
        String expected = "";
        Assert.assertEquals(expected, result);
    }
    
    // Test exception handling scenario where the input string is null
    @Test(expected = NullPointerException.class)
    public void testEscapeNullString() {
        String string = null;
        Document.OutputSettings out = new Document.OutputSettings();
        Entities.escape(string, out);
    }
    
    // Test edge case where the input string contains a surrogate pair
    @Test
    public void testEscapeSurrogatePair() {
        String string = "\uD83D\uDC4D"; // thumbs up emoji surrogate pair
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(string, out);
        String expected = "\uD83D\uDC4D"; // unchanged
        Assert.assertEquals(expected, result);
    }

    // Test edge case where the input string contains a character that cannot be encoded using the given encoder
    @Test
    public void testEscapeUnencodableCharacter() {
        String string = "क"; // Devanagari letter "Ka" cannot be encoded using ASCII encoder
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(string, out);
        String expected = "&#x915;"; // hex representation of the character code point
        Assert.assertEquals(expected, result);
    }

    // Test edge case where the input string contains a named entity that is not present in the base set
    @Test
    public void testIsNamedEntityInBaseSet() {
        String name = "mdash";
        boolean result = Entities.isBaseNamedEntity(name);
        Assert.assertFalse(result);
    }
}
