package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Assert;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("apos"));
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        Assert.assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape() {
        Assert.assertEquals("&quot;", Entities.escape("\"", null));
        Assert.assertEquals("&amp;", Entities.escape("&", null));
        Assert.assertEquals("&apos;", Entities.escape("'", null));
        Assert.assertEquals("&lt;", Entities.escape("<", null));
        Assert.assertEquals("&gt;", Entities.escape(">", null));
        Assert.assertEquals("&#39;", Entities.escape("&#39;", null));
    }
    
    @Test
    public void testEscapeCharsetEncoderEscapeMode() {
        Assert.assertEquals("&quot;", Entities.escape("\"", null, Entities.EscapeMode.xhtml));
        Assert.assertEquals("&amp;", Entities.escape("&", null, Entities.EscapeMode.xhtml));
        Assert.assertEquals("&apos;", Entities.escape("'", null, Entities.EscapeMode.xhtml));
        Assert.assertEquals("&lt;", Entities.escape("<", null, Entities.EscapeMode.xhtml));
        Assert.assertEquals("&gt;", Entities.escape(">", null, Entities.EscapeMode.xhtml));
        Assert.assertEquals("&#39;", Entities.escape("&#39;", null, Entities.EscapeMode.xhtml));
    }
    
    @Test
    public void testUnescape() {
        Assert.assertEquals("\"", Entities.unescape("&quot;"));
        Assert.assertEquals("&", Entities.unescape("&amp;"));
        Assert.assertEquals("'", Entities.unescape("&apos;"));
        Assert.assertEquals("<", Entities.unescape("&lt;"));
        Assert.assertEquals(">", Entities.unescape("&gt;"));
        Assert.assertEquals("&#39;", Entities.unescape("&#39;"));
    }
    
    @Test
    public void testUnescapeStrict() {
        Assert.assertEquals("\"", Entities.unescape("&quot;", true));
        Assert.assertEquals("&", Entities.unescape("&amp;", true));
        Assert.assertEquals("'", Entities.unescape("&apos;", true));
        Assert.assertEquals("<", Entities.unescape("&lt;", true));
        Assert.assertEquals(">", Entities.unescape("&gt;", true));
        Assert.assertEquals("&#39;", Entities.unescape("&#39;", true));
    }

}