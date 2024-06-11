package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity_ValidName_ReturnsTrue() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
    }
    
    @Test
    public void testIsNamedEntity_InvalidName_ReturnsFalse() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testIsBaseNamedEntity_ValidName_ReturnsTrue() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }
    
    @Test
    public void testIsBaseNamedEntity_InvalidName_ReturnsFalse() {
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName_ValidName_ReturnsCharacter() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }
    
    @Test
    public void testGetCharacterByName_InvalidName_ReturnsNull() {
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape_StringWithoutSpecialChars_ReturnsOriginalString() {
        String input = "Hello World!";
        String expected = "Hello World!";
        assertEquals(expected, Entities.escape(input, null));
    }
    
    @Test
    public void testEscape_StringWithSpecialChars_ReturnsEscapedString() {
        String input = "This <tag> is &special&!";
        String expected = "This &lt;tag&gt; is &amp;special&amp;!";
        assertEquals(expected, Entities.escape(input, null));
    }
    
    @Test
    public void testUnescape_StringWithoutEscapedChars_ReturnsOriginalString() {
        String input = "Hello World!";
        String expected = "Hello World!";
        assertEquals(expected, Entities.unescape(input));
    }
    
    @Test
    public void testUnescape_StringWithEscapedChars_ReturnsUnescapedString() {
        String input = "This &lt;tag&gt; is &amp;special&amp;!";
        String expected = "This <tag> is &special&!";
        assertEquals(expected, Entities.unescape(input));
    }
    
    @Test
    public void testUnescape_StrictStringWithEscapedChars_ReturnsUnescapedString() {
        String input = "This &lt;tag&gt; is &amp;special&amp;!";
        String expected = "This <tag> is &special&!";
        assertEquals(expected, Entities.unescape(input, true));
    }
    
    @Test
    public void testUnescape_StrictStringWithoutEscapedChars_ReturnsOriginalString() {
        String input = "Hello World!";
        String expected = "Hello World!";
        assertEquals(expected, Entities.unescape(input, true));
    }
}