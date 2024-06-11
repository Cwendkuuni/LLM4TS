import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    // Test for isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(isNamedEntity("lt"));
        assertFalse(isNamedEntity("gt")); // Since "gt" is not present in the given class
        assertFalse(isNamedEntity("zz")); // Since "zz" is not a valid named entity
    }

    // Test for isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(isBaseNamedEntity("lt"));
        assertFalse(isBaseNamedEntity("amp")); // Since "amp" is not in the base entity set
        assertFalse(isBaseNamedEntity("zz")); // Since "zz" is not a valid named entity
    }

    // Test for getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));
        assertNull(getCharacterByName("zz")); // Since "zz" is not a valid named entity
    }

    // Test for escape method with default settings
    @Test
    public void testEscape() {
        String input = "&lt;Hello&amp; World&gt;";
        String expectedOutput = "&amp;lt;Hello&amp;amp;amp;amp;amp;amp;Amp;amp;gt;";
        String output = escape(input, null); // null for OutputSettings
        assertEquals(expectedOutput, output);
    }

    // Test for escape method with specific OutputSettings
    @Test
    public void testEscapeWithOutputSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.xhtml);

        String input = "&lt;Hello&amp; World&gt;";
        String expectedOutput = "&lt;Hello&amp;amp;amp;lt; World&amp;gt;";
        String output = escape(null, input, settings, false, false, false);
        assertEquals(expectedOutput, output);
    }

    // Test for unescape method
    @Test
    public void testUnescape() {
        String input = "&amp;lt;Hello&amp;amp;amp;amp;lt; World&amp;gt;";
        String expectedOutput = "&lt;Hello&lt; World&gt;";
        String output = unescape(input, false);
        assertEquals(expectedOutput, output);
    }

    // Test for canEncode method
    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        char character = 'é'; // é is a character that requires UTF-8 encoding

        assertTrue(canEncode(charset, character, null));
    }

    // Edge case tests for escape method
    @Test(expected = IllegalArgumentException.class)
    public void testEscapeNullInput() {
        escape(null, null, null, false, false, false);
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeNullString() {
        escape(null, null, null, false, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeNegativeOffset() {
        String string = "Hello";
        escape(string, null, null, false, false, false);
        escape(string, 1, null, false, false, false);
        escape(string, -1, null, false, false, false);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testEscapeCharCount() {
        String string = "Hello";
        escape(string, 0, null, false, false, false);
        escape(string, 0, null, false, false, false); // already processed the first character
        escape(string, 4, null, false, false, false); // longer than the string length
    }

    // Edge case tests for canEncode method
    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeNullCharset() {
        canEncode(null, 'é', null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeNegativeCharacter() {
        CoreCharset charset = CoreCharset.utf;
        canEncode(charset, '\uFFFFFF', null); // Unicode character U+FFFFFF is not representable in UTF-8
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeNullCharacter() {
        CoreCharset charset = CoreCharset.utf;
        canEncode(charset, null, null);
    }
}