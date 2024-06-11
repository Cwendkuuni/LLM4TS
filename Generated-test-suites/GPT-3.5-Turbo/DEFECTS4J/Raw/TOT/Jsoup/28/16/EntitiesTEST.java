import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test typical named entity
        Assert.assertTrue(Entities.isNamedEntity("lt"));

        // Test invalid named entity
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test typical named entity
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Test invalid named entity
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Test escaping string with special characters
        Assert.assertEquals("&lt;html&gt;", Entities.escape("<html>", null));

        // Test escaping string without special characters
        Assert.assertEquals("HelloWorld", Entities.escape("HelloWorld", null));
    }

    @Test
    public void testUnescape() {
        // Test unescaping string with named entity
        Assert.assertEquals("<html>", Entities.unescape("&lt;html&gt;"));

        // Test unescaping string with decimal entity
        Assert.assertEquals("©", Entities.unescape("&#169;"));

        // Test unescaping string with hexadecimal entity
        Assert.assertEquals("€", Entities.unescape("&#x20AC;"));

        // Test unescaping string without entities
        Assert.assertEquals("HelloWorld", Entities.unescape("HelloWorld"));
    }

    @Test
    public void testUnescapeWithStrict() {
        // Test unescaping string with strict flag and missing trailing semicolon
        Assert.assertEquals("<html>", Entities.unescape("&lt;html&gt;", true));

        // Test unescaping string with strict flag and valid trailing semicolon
        Assert.assertEquals("HelloWorld", Entities.unescape("HelloWorld;", true));
    }

    @Test
    public void testLoadEntities() {
        // Test loading entities properties file
        Assert.assertNotNull(Entities.loadEntities("entities-full.properties"));
    }
}