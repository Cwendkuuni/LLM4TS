import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_ValidName_ReturnsTrue() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntity_InvalidName_ReturnsFalse() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity_ValidName_ReturnsTrue() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity_InvalidName_ReturnsFalse() {
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName_ValidName_ReturnsCharacter() {
        Character character = Entities.getCharacterByName("gt");
        assertEquals((Character) '>', character);
    }

    @Test
    public void testGetCharacterByName_InvalidName_ReturnsNull() {
        Character character = Entities.getCharacterByName("invalid");
        assertNull(character);
    }

    @Test
    public void testEscape_NormalString_ReturnsEscapedString() {
        String escapedString = Entities.escape("This is a <test> string.", Document.OutputSettings.html());
        assertEquals("This is a &lt;test&gt; string.", escapedString);
    }

    @Test
    public void testEscape_WithAttributeInAttributeMode_ReturnsAttributeString() {
        String escapedString = Entities.escape("<span data-attr='value'>Text</span>", Document.OutputSettings.html());
        assertEquals("<span data-attr='value'>Text</span>", escapedString);
    }

    @Test
    public void testUnescape_StringWithHtmlEntities_ReturnsUnescapedString() {
        String unescapedString = Entities.unescape("This is a &lt;test&gt; string.");
        assertEquals("This is a <test> string.", unescapedString);
    }

    @Test
    public void testUnescape_StringWithoutTrailingSemicolon_ReturnsUnescapedString() {
        String unescapedString = Entities.unescape("This is a &lt;test&gt; string", true);
        assertEquals("This is a <test> string", unescapedString);
    }

    @Test
    public void testUnescape_StringWithInvalidEntities_ReturnsUnchangedString() {
        String unescapedString = Entities.unescape("This is a &invalid; string.", true);
        assertEquals("This is a &invalid; string.", unescapedString);
    }

    @Test
    public void testLoadEntities_ValidFilename_ReturnsMap() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities);
        assertEquals(2192, entities.size());
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntities_InvalidFilename_ThrowsException() {
        Map<String, Character> entities = Entities.loadEntities("invalid.properties");
    }

    @Test
    public void testToCharacterKey_ValidMap_ReturnsCharacterKeyMap() {
        Map<String, Character> inputMap = new HashMap<>();
        inputMap.put("amp", '&');
        inputMap.put("gt", '>');
        inputMap.put("lt", '<');

        Map<Character, String> characterKeyMap = Entities.toCharacterKey(inputMap);
        assertNotNull(characterKeyMap);
        assertEquals(3, characterKeyMap.size());
        assertEquals("amp", characterKeyMap.get('&'));
        assertEquals("gt", characterKeyMap.get('>'));
        assertEquals("lt", characterKeyMap.get('<'));
    }

    @Test
    public void testCanEncode_ASCIICharset_ReturnsTrue() {
        CharsetEncoder encoder = Mockito.mock(CharsetEncoder.class);
        when(encoder.charset().name()).thenReturn("US-ASCII");

        boolean canEncode = Entities.canEncode(CoreCharset.ascii, 'a', encoder);
        assertTrue(canEncode);
    }

    @Test
    public void testCanEncode_UTFCharset_ReturnsTrue() {
        CharsetEncoder encoder = Mockito.mock(CharsetEncoder.class);
        when(encoder.charset().name()).thenReturn("UTF-8");

        boolean canEncode = Entities.canEncode(CoreCharset.utf, '€', encoder);
        assertTrue(canEncode);
    }

    @Test
    public void testCanEncode_FallbackCharset_ReturnsTrue() {
        CharsetEncoder encoder = Mockito.mock(CharsetEncoder.class);
        when(encoder.canEncode(Mockito.anyString())).thenReturn(true);

        boolean canEncode = Entities.canEncode(CoreCharset.fallback, '©', encoder);
        assertTrue(canEncode);
    }

    

    // Add additional test cases for complete coverage of the Entities class

    
}