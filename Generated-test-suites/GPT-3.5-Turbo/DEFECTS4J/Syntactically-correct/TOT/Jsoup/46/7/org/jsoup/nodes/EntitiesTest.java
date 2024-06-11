import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case - entity exists
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        // Edge case - entity does not exist
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
        // Error scenario - invalid input
        Assert.assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case - entity exists in base set
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        // Edge case - entity does not exist in base set
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
        // Error scenario - invalid input
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case - entity exists
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Edge case - entity does not exist
        Assert.assertNull(Entities.getCharacterByName("xyz"));
        // Error scenario - invalid input
        Assert.assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Typical use case - string with special characters
        Assert.assertEquals("&lt;&gt;&amp;&quot;&#160;", Entities.escape("<>&\" "));
        // Edge case - empty string
        Assert.assertEquals("", Entities.escape(""));
        // Error scenario - null input
        Assert.assertNull(Entities.escape(null));
    }

    // Test cases for other methods...

}