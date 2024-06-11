package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    // Testing isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge case - empty string
        Assert.assertFalse(Entities.isNamedEntity(""));
        
        // Error scenario - unknown entity
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    // Testing isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge case - empty string
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
        
        // Error scenario - unknown entity
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    // Testing getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', character.charValue());
        
        // Edge case - empty string
        character = Entities.getCharacterByName("");
        Assert.assertNull(character);
        
        // Error scenario - unknown entity
        character = Entities.getCharacterByName("unknown");
        Assert.assertNull(character);
    }

    // Testing escape method
    @Test
    public void testEscape() {
        // Typical use case
        String escapedString = Entities.escape("<div class=\"test\">Hello world!</div>", new Document.OutputSettings());
        Assert.assertEquals("&lt;div class=&quot;test&quot;&gt;Hello world!&lt;/div&gt;", escapedString);
        
        // Edge case - empty string
        escapedString = Entities.escape("", new Document.OutputSettings());
        Assert.assertEquals("", escapedString);
        
        // Error scenario - null input
        escapedString = Entities.escape(null, new Document.OutputSettings());
        Assert.assertNull(escapedString);
    }

    // Testing unescape method
    @Test
    public void testUnescape() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;div class=&quot;test&quot;&gt;Hello world!&lt;/div&gt;");
        Assert.assertEquals("<div class=\"test\">Hello world!</div>", unescapedString);
        
        // Edge case - empty string
        unescapedString = Entities.unescape("");
        Assert.assertEquals("", unescapedString);
        
        // Error scenario - null input
        unescapedString = Entities.unescape(null);
        Assert.assertNull(unescapedString);
    }

    // Testing unescape with strict flag
    @Test
    public void testUnescapeWithStrictFlag() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;div class=&quot;test&quot;&gt;Hello world!&lt;/div&gt;", true);
        Assert.assertEquals("<div class=\"test\">Hello world!</div>", unescapedString);
        
        // Edge case - empty string
        unescapedString = Entities.unescape("", true);
        Assert.assertEquals("", unescapedString);
        
        // Error scenario - null input
        unescapedString = Entities.unescape(null, true);
        Assert.assertNull(unescapedString);
    }
}