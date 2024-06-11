import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case: a known named entity
        boolean result1 = Entities.isNamedEntity("lt");
        Assert.assertTrue(result1);

        // Edge case: an unknown named entity
        boolean result2 = Entities.isNamedEntity("unknown");
        Assert.assertFalse(result2);

        // Error scenario: null input
        boolean result3 = Entities.isNamedEntity(null);
        Assert.assertFalse(result3);
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case: a known named entity
        Character result1 = Entities.getCharacterByName("gt");
        Character expected1 = '>';
        Assert.assertEquals(expected1, result1);

        // Edge case: an unknown named entity
        Character result2 = Entities.getCharacterByName("unknown");
        Assert.assertNull(result2);

        // Error scenario: null input
        Character result3 = Entities.getCharacterByName(null);
        Assert.assertNull(result3);
    }

    @Test
    public void testEscape() {
        // Typical use case: escape special characters <, >, &, ", ' in XHTML mode
        String input1 = "<p>&";
        String result1 = Entities.escape(input1, Document.OutputSettings.xhtml());
        String expected1 = "&lt;p&gt;&amp;";
        Assert.assertEquals(expected1, result1);

        // Edge case: no special characters to escape
        String input2 = "hello world";
        String result2 = Entities.escape(input2, Document.OutputSettings.xhtml());
        Assert.assertEquals(input2, result2);

        // Error scenario: null input
        String result3 = Entities.escape(null, Document.OutputSettings.xhtml());
        Assert.assertNull(result3);
    }

    @Test
    public void testUnescape() {
        // Typical use case: unescape HTML entities
        String input1 = "&lt;p&gt;&amp;";
        String result1 = Entities.unescape(input1);
        String expected1 = "<p>&";
        Assert.assertEquals(expected1, result1);

        // Edge case: no HTML entities to unescape
        String input2 = "hello world";
        String result2 = Entities.unescape(input2);
        Assert.assertEquals(input2, result2);

        // Error scenario: null input
        String result3 = Entities.unescape(null);
        Assert.assertNull(result3);
    }
}