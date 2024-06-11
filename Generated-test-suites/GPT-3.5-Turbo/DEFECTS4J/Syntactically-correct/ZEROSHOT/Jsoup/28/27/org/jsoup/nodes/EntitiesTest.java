import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test with known named entity
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);

        // Test with unknown named entity
        result = Entities.isNamedEntity("dummy");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        // Test with known named entity
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals((Character) '<', character);

        // Test with unknown named entity
        character = Entities.getCharacterByName("dummy");
        Assert.assertNull(character);
    }

    @Test
    public void testEscape() {
        // Test with valid string and escape mode
        String string = "<html>";
        String escapedString = Entities.escape(string, CharsetEncoder.defaultCharset(), Entities.EscapeMode.xhtml);
        Assert.assertEquals("&lt;html&gt;", escapedString);

        // Test with invalid string and escape mode
        string = "\uFFFD";
        escapedString = Entities.escape(string, CharsetEncoder.defaultCharset(), Entities.EscapeMode.xhtml);
        Assert.assertEquals(string, escapedString);
    }

    @Test
    public void testUnescape() {
        // Test with valid string
        String string = "&lt;html&gt;";
        String unescapedString = Entities.unescape(string);
        Assert.assertEquals("<html>", unescapedString);

        // Test with invalid string
        string = "\uFFFD";
        unescapedString = Entities.unescape(string);
        Assert.assertEquals(string, unescapedString);
    }
}