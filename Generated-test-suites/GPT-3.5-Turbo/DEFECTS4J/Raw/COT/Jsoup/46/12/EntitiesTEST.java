import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for a known named entity
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        // Test for an unknown named entity
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for a known named entity in the base set
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        // Test for an unknown named entity in the base set
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for a known named entity
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Test for an unknown named entity
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Test basic HTML escape with xhtml EscapeMode
        String input = "<html>";
        String expected = "&lt;html&gt;";
        String escaped = Entities.escape(input, Document.OutputSettings.xhtml());
        Assert.assertEquals(expected, escaped);
    }

    @Test
    public void testUnescape() {
        // Test basic HTML unescape with strict mode
        String input = "&lt;html&gt;";
        String expected = "<html>";
        String unescaped = Entities.unescape(input, true);
        Assert.assertEquals(expected, unescaped);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be written here

}