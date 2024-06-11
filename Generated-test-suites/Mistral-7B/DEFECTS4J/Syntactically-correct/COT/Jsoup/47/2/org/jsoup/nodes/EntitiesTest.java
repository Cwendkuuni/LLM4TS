import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("gt")); // different character
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("x")); // not a named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt")); // different character
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set
        Assert.assertFalse(Entities.isBaseNamedEntity("x")); // not a named entity
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("x")); // not a named entity
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        Entities.getCharacterByName(null);
    }

    @Test
    public void testEscape_Basic() {
        String input = "Hello <world> & this & that & 0xA0 & 0x80 & \u00A9 & \u20AC & \uD800";
        String expectedOutput = "Hello &amp;lt;world&amp;gt; &amp;amp; &amp;amp; &amp;#xa0; &amp;#x80; &amp;#x00A9; &amp;#x20AC; &amp;#xD800;";
        String output = Entities.escape(input, null);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_InAttribute() {
        String input = "Hello <world> & this & that & 0xA0 & 0x80 & \u00A9 & \u20AC & \uD800 & 'foo' bar";
        String expectedOutput = "Hello &amp;lt;world&amp;gt; &amp;amp; &amp;amp; &amp;#xa0; &amp;#x80; &amp;#x00A9; &amp;#x20AC; &amp;#xD800; &amp;#39;foo&amp;quot; bar";
        String output = Entities.escape(input, null, true, false, false);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_NormalizeWhite() {
        String input = "Hello \t world \n \r \t foo";
        String expectedOutput = "Hello \t \t world \n \r \t \t foo";
        String output = Entities.escape(input, null, false, true, false);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_StripLeadingWhite() {
        String input = " \tHello world \n \r \t foo";
        String expectedOutput = "Hello world \n \r foo";
        String output = Entities.escape(input, null, false, true, true);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscape_NullInput() {
        Entities.escape(null, null);
    }

    @Test
    public void testUnescape_Basic() {
        String input = "Hello &amp;lt;world&amp;gt; &amp;amp; &amp;amp; &amp;#xa0; &amp;#x80; &amp;#x00A9; &amp;#x20AC; &amp;#xD800;";
        String expectedOutput = "Hello <world> &amp; &amp; & #xa0; &#x80; © € 𐠀";
        String output = Entities.unescape(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape_Strict() {
        String input = "Hello &amp;lt;world&amp;gt; &amp;amp; &amp;amp; &amp;#xa0; &amp;#x80; &amp;#x00A9; &amp;#x20AC; &amp;#xD800;";
        String expectedOutput = "Hello <world>\u003C/world\u003E &amp; &amp; & \u00A0 & \u0080 & \u00A9 & \u20AC & \uD800";
        String output = Entities.unescape(input, true);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode_Ascii() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'À')); // different character
    }

    @Test
    public void testCanEncode_Utf() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'À'));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '😀')); // different character
    }

    @Test
    public void testCanEncode_Fallback() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'À')); // using fallback encoder
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.fallback, '😃')); // different character
    }
}