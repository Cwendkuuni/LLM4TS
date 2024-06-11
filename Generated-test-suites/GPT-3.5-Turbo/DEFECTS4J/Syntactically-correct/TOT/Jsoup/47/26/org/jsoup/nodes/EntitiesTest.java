import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {

    private static final String VALID_ENTITY_NAME = "lt";
    private static final String INVALID_ENTITY_NAME = "invalid_entity";

    @Before
    public void setUp() {
        // Add any setup code here if needed
    }

    @Test
    public void testIsNamedEntity_ValidEntityName_ReturnsTrue() {
        boolean result = Entities.isNamedEntity(VALID_ENTITY_NAME);
        assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_InvalidEntityName_ReturnsFalse() {
        boolean result = Entities.isNamedEntity(INVALID_ENTITY_NAME);
        assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity_ValidEntityName_ReturnsTrue() {
        boolean result = Entities.isBaseNamedEntity(VALID_ENTITY_NAME);
        assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity_InvalidEntityName_ReturnsFalse() {
        boolean result = Entities.isBaseNamedEntity(INVALID_ENTITY_NAME);
        assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_ValidEntityName_ReturnsCharacterValue() {
        Character expected = '<';
        Character result = Entities.getCharacterByName(VALID_ENTITY_NAME);
        assertEquals(expected, result);
    }

    @Test
    public void testGetCharacterByName_InvalidEntityName_ReturnsNull() {
        Character result = Entities.getCharacterByName(INVALID_ENTITY_NAME);
        assertNull(result);
    }

    @Test
    public void testEscape_ValidString_ReturnsEscapedString() {
        String string = "<div>Hello, &amp; World!</div>";
        String expected = "&lt;div&gt;Hello, &amp;amp; World!&lt;/div&gt;";
        String result = Entities.escape(string, null); // add suitable output settings if any
        assertEquals(expected, result);
    }

    @Test
    public void testEscape_EmptyString_ReturnsEmptyString() {
        String string = "";
        String expected = "";
        String result = Entities.escape(string, null); // add suitable output settings if any
        assertEquals(expected, result);
    }

    @Test
    public void testEscape_StringWithWhitespace_NormalizesWhitespace() {
        String string = "   Hello    World!  ";
        String expected = " Hello World! ";
        String result = Entities.escape(string, null); // add suitable output settings if any
        assertEquals(expected, result);
    }

    @Test
    public void testEscape_StringWithLeadingWhitespace_NormalizesWhitespaceAndStripsLeadingWhitespace() {
        String string = "   Hello World!";
        String expected = " Hello World!";
        String result = Entities.escape(string, null); // add suitable output settings if any
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_ValidEscapedString_ReturnsUnescapedString() {
        String string = "&lt;div&gt;Hello, &amp;amp; World!&lt;/div&gt;";
        String expected = "<div>Hello, &amp; World!</div>";
        String result = Entities.unescape(string);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_ValidEscapedStringWithTrailingSemicolon_ReturnsUnescapedString() {
        String string = "&lt;div&gt;Hello, &amp;amp; World!&lt;/div&gt;";
        String expected = "<div>Hello, &amp; World!</div>";
        String result = Entities.unescape(string, true);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_ValidEscapedStringWithoutTrailingSemicolon_ReturnsUnescapedString() {
        String string = "&lt;div&gt;Hello, &amp;amp; World!&lt;/div&gt;";
        String expected = "<div>Hello, &amp; World!</div>";
        String result = Entities.unescape(string, false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_InvalidEscapedString_ReturnsStringAsIs() {
        String string = "Invalid escaped string";
        String expected = "Invalid escaped string";
        String result = Entities.unescape(string);
        assertEquals(expected, result);
    }

}