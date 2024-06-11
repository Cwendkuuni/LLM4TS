import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "This is a test &amp; string.";
        String expected = "This is a test &amp;amp; string.";
        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        assertEquals(expected, output);
    }

    @Test
    public void testEscapeWithNullDocumentOutputSettings() {
        String input = "This is a test &amp; string.";
        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullCharsetEncoder() {
        String input = "This is a test &amp; string.";
        Entities.escape(input, null, Entities.EscapeMode.base);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithUnknownEscapeMode() {
        String input = "This is a test &amp; string.";
        Entities.escape(input, null, null);
    }

    @Test
    public void testUnescapeWithEmptyString() {
        String input = "";
        String output = Entities.unescape(input);
        assertEquals("", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNull() {
        String input = null;
        String output = Entities.unescape(input);
        assertEquals("", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithInvalidInput() {
        String input = "This is & not a valid input.";
        String output = Entities.unescape(input);
        assertEquals("This is & not a valid input.", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullBoolean() {
        String input = "This is a test &amp; string.";
        Entities.unescape(input, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnescapeWithStrictModeAndInvalidInput() {
        String input = "This is a test &amp; string.";
        String output = Entities.unescape(input, true);
        assertEquals(input, output);
    }

    @Test
    public void testIsNamedEntityWithNull() {
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsNamedEntityWithEmptyString() {
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByNameWithNull() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testGetCharacterByNameWithEmptyString() {
        assertNull(Entities.getCharacterByName(""));
    }
}