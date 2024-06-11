import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities in the full entity set
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));

        // Test for unknown named entities
        Assert.assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty string
        Assert.assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in the base entity set
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("gt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test for unknown named entities in the base entity set
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for empty string
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities in the full entity set
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for unknown named entities
        try {
            Entities.getCharacterByName("unknown");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for empty string
        try {
            Entities.getCharacterByName("");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEscape() {
        String input = "&lt; &gt; &amp; &quot;";
        Document.OutputSettings out = new Document.OutputSettings();

        // Test default escape mode
        String output = Entities.escape(input, out);
        String expectedOutput = "&amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        Assert.assertEquals(expectedOutput, output);

        // Test xhtml escape mode
        out.escapeMode(Entities.EscapeMode.xhtml);
        output = Entities.escape(input, out);
        expectedOutput = "&lt; &gt; &amp; &quot;";
        Assert.assertEquals(expectedOutput, output);

        // Test base escape mode
        out.escapeMode(Entities.EscapeMode.base);
        output = Entities.escape(input, out);
        expectedOutput = "&amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        Assert.assertEquals(expectedOutput, output);

        // Test extended escape mode
        out.escapeMode(Entities.EscapeMode.extended);
        output = Entities.escape(input, out);
        expectedOutput = "&#x3C; &#x3E; &#x26; &#x22;";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt; &amp;gt; &amp;amp; &amp;quot;";

        // Test default unescape mode
        String output = Entities.unescape(input);
        Assert.assertEquals("< > & \"", output);

        // Test strict unescape mode
        String outputStrict = Entities.unescape(input, true);
        Assert.assertEquals("< > & \"", outputStrict);
    }

    @Test
    public void testCanEncode() {
        // Test for common ASCII characters
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = Character.encodings().get("UTF-8");
        for (char c = 0; c < 128; c++) {
            assertTrue(Entities.canEncode(charset, c, encoder));
        }

        // Test for surrogate pairs
        char c1 = '\uD800';
        char c2 = '\uDC00';
        assertFalse(Entities.canEncode(CoreCharset.ascii, c1, encoder));
        assertFalse(Entities.canEncode(CoreCharset.ascii, c2, encoder));
        assertTrue(Entities.canEncode(CoreCharset.utf, c1, encoder));
        assertTrue(Entities.canEncode(CoreCharset.utf, c2, encoder));
    }
}