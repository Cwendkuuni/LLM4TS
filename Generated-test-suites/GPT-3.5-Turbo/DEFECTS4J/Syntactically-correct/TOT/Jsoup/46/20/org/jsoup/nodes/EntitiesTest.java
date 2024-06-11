import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        // Edge case: empty string
        Assert.assertFalse(Entities.isNamedEntity(""));
        // Error scenario: non-existing entity
        Assert.assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        // Edge case: uppercase entity
        Assert.assertTrue(Entities.isBaseNamedEntity("AMP"));
        // Error scenario: non-existing entity
        Assert.assertFalse(Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Edge case: uppercase entity
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("LT"));
        // Error scenario: non-existing entity
        Assert.assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        // Typical use case
        Assert.assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        // Edge case: empty string
        Assert.assertEquals("", Entities.escape("", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        // Error scenario: null input
        Assert.assertEquals("", Entities.escape(null, new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        Assert.assertEquals("\"", Entities.unescape("&quot;"));
        // Edge case: empty string
        Assert.assertEquals("", Entities.unescape(""));
        // Error scenario: null input
        Assert.assertEquals("", Entities.unescape(null));
    }
}