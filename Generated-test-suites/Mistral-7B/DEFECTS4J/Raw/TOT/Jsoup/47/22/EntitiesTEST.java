import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method for valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test isNamedEntity method for invalid named entities
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test isBaseNamedEntity method for valid named entities in base set
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test isBaseNamedEntity method for invalid named entities
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method for valid named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test getCharacterByName method for invalid named entities
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        // Test escape method with default settings
        String input = "&lt;hello&amp; world&gt;";
        String expectedOutput = "&amp;lt;hello&amp;amp;amp;amp;amp;amp;amp;amp;amp;lt;world&amp;gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);

        // Test escape method with XHTML settings
        String xhtmlInput = "<hello& world>";
        String xhtmlExpectedOutput = "&lt;hello&amp;amp;amp;quot; world&gt;";
        String xhtmlOutput = Entities.escape(xhtmlInput, Entities.EscapeMode.xhtml);
        assertEquals(xhtmlExpectedOutput, xhtmlOutput);
    }

    @Test
    public void testUnescape() {
        // Test unescape method with default settings
        String input = "&amp;lt;hello&amp;amp;amp;amp;amp;amp;amp;amp;lt;world&amp;gt;";
        String expectedOutput = "&lt;hello world&gt;";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        // Test unescape method with strict settings
        String strictInput = "&amp;lt;hello world&amp;gt;";
        String strictOutput = "<hello world>";
        String outputStrict = Entities.unescape(strictInput, true);
        assertEquals(strictOutput, outputStrict);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncode_InvalidCharset() {
        Entities.canEncode(null, 'é', null);
    }

    @Test
    public void testCanEncode_ASCII() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
    }

    @Test
    public void testCanEncode_UTF() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', null));
    }

    @Test
    public void testCanEncode_Fallback() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'é', null));
    }

    @Test
    public void testCanEncode_UTF8() {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', encoder));
    }

    @Test
    public void testCanEncode_UTF16() {
        CharsetEncoder encoder = Charset.forName("UTF-16").newEncoder();
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', encoder));
    }
}