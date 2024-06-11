import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test unknown named entities
        assertFalse(Entities.isNamedEntity("unknowntag"));

        // Test empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;

        // Test known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals("'", Entities.getCharacterByName("apos"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test unknown named entities
        assertNull(Entities.getCharacterByName("unknowntag"));

        // Test empty string
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscapeMethod() {
        String input = "This is a test <tag> with &amp; special characters and åäö éè Éè Àà ÀÀ Ää Öö";

        // Test XHTML output
        String xhtmlOutput = Entities.escape(input, Document.OutputSettings.xhtml(), null);
        assertEquals("This is a test &lt;tag&gt; with &amp;special characters and &#228;&#229;&#246; &#233;&#232; &#195;&#196; &#194;&#195; &#196;&#197; &#224;&#225; &#225;&#224; &#228;&#229; &#246;", xhtmlOutput);

        // Test default HTML output
        String htmlOutput = Entities.escape(input, null, null);
        assertEquals("This is a test &lt;tag&gt; with &amp;special characters and &Aring;&#x00E5;&#x00F6; &#233;&#232; &#195;&#196; &#194;&#195; &#196;&#197; &#224;&#225; &#225;&#224; &#228;&#229; &#246;", htmlOutput);

        // Test extended HTML output
        String extendedOutput = Entities.escape(input, null, Entities.EscapeMode.extended);
        assertEquals("This is a test &lt;tag&gt; with &amp;special characters and &#x00C3;&#x00A5;&#x00E6; &#x00E3;&#x00B2; &#195;&#196; &#194;&#195; &#196;&#197; &#224;&#225; &#225;&#224; &#228;&#229; &#246;", extendedOutput);
    }

    @Test
    public void testUnescapeMethod() {
        String input = "This is a test &lt;tag&gt; with &amp;special characters and &#x00C3;&#x00A5;&#x00E6; &#x00E3;&#x00B2; &#195;&#196; &#194;&#195; &#196;&#197; &#224;&#225; &#225;&#224; &#228;&#229; &#246;";

        // Test XHTML output
        String xhtmlOutput = Entities.unescape(input);
        assertEquals("This is a test <tag> with &amp;special characters and ÅÄÖ éè Éè Àà ÀÀ Ää Öö", xhtmlOutput);

        // Test default HTML output
        String htmlOutput = Entities.unescape(input, false);
        assertEquals("This is a test <tag> with &amp;special characters and ÅÄÖ éè Èè Àà ÀÀ Ää Öö", htmlOutput);

        // Test strict HTML output
        String strictHtmlOutput = Entities.unescape(input, true);
        assertEquals("This is a test <tag> with &amp;special characters and ÅÄÖ éè Èè Àà ÀÀ Ää Öö", strictHtmlOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities.escape("input", null, "invalidMode");
    }
}