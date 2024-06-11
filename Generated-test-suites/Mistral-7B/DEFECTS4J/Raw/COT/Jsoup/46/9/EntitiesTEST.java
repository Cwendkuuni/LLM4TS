import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("gt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNullArgument() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNullArgument() {
        Entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeMethodBasicFunctionality() {
        String input = "Hello & World! < Greetings >";
        String expectedOutput = "Hello &amp; World! &lt; Greetings &gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithDifferentOutputSettings() {
        String input = "Hello & World! < Greetings >";
        String xhtmlOutput = "Hello &amp;amp; World! &lt; &amp;amp; Greetings &gt;";
        String baseOutput = "Hello &amp; World! &lt;Greetings&gt;";
        String extendedOutput = "Hello &amp;#x26;&#xA0;&#xA0;World! &lt;Greetings &gt;";

        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.escapeMode(Entities.EscapeMode.xhtml);

        Document.OutputSettings baseSettings = new Document.OutputSettings();
        baseSettings.escapeMode(Entities.EscapeMode.base);

        Document.OutputSettings extendedSettings = new Document.OutputSettings();
        extendedSettings.escapeMode(Entities.EscapeMode.extended);

        String xhtmlResult = Entities.escape(input, xhtmlSettings);
        String baseResult = Entities.escape(input, baseSettings);
        String extendedResult = Entities.escape(input, extendedSettings);

        assertEquals(xhtmlOutput, xhtmlResult);
        assertEquals(baseOutput, baseResult);
        assertEquals(extendedOutput, extendedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities.escape(null, null);
    }

    @Test
    public void testUnescapeMethodBasicFunctionality() {
        String input = "Hello &amp; World! &lt; Greetings &gt;";
        String expectedOutput = "Hello & World! < Greetings >";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethodBasicFunctionality() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        boolean asciiResult = Entities.canEncode(Entities.CoreCharset.ascii, 'A', encoder);
        assertTrue(asciiResult);

        boolean utfResult = Entities.canEncode(Entities.CoreCharset.utf, '√', encoder);
        assertTrue(utfResult);

        boolean fallbackResult = Entities.canEncode(Entities.CoreCharset.fallback, 'Ä', encoder);
        assertTrue(fallbackResult);

        boolean invalidUtf16Result = Entities.canEncode(Entities.CoreCharset.utf, '\uD800', encoder);
        assertFalse(invalidUtf16Result);
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntityWithEmptyString() {
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByNameWithInvalidEntity() {
        assertNull(Entities.getCharacterByName("INVALID_ENTITY"));
    }
}