import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.nio.charset.StandardCharsets;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', (char) character);
    }

    @Test
    public void testEscape_XHTML() {
        String escapedString = Entities.escape("<html>", StandardCharsets.UTF_8, Entities.EscapeMode.xhtml);
        Assert.assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testEscape_Base() {
        String escapedString = Entities.escape("<html>", StandardCharsets.UTF_8, Entities.EscapeMode.base);
        Assert.assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testEscape_Extended() {
        String escapedString = Entities.escape("<html>", StandardCharsets.UTF_8, Entities.EscapeMode.extended);
        Assert.assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        Assert.assertEquals("<html>", unescapedString);
    }

    @Test
    public void testUnescape_Strict() {
        String unescapedString = Entities.unescape("&lt;html&gt;", true);
        Assert.assertEquals("<html>", unescapedString);
    }

    // Additional test cases for potential edge cases and exceptions

    @Test
    public void testIsNamedEntity_InvalidName() {
        boolean result = Entities.isNamedEntity("invalid");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_InvalidName() {
        Character character = Entities.getCharacterByName("invalid");
        Assert.assertNull(character);
    }

    @Test
    public void testEscape_InvalidEncoder() {
        String escapedString = Entities.escape("<html>", null, Entities.EscapeMode.base);
        Assert.assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testUnescape_NoEntities() {
        String unescapedString = Entities.unescape("html");
        Assert.assertEquals("html", unescapedString);
    }

    @Test
    public void testLoadEntities_InvalidFile() {
        boolean exceptionThrown = false;
        try {
            Entities.loadEntities("invalid.properties");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }
}