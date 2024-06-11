package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    private Entities.EscapeMode escapeMode;
    private Entities entity;

    @Before
    public void setUp() {
        escapeMode = Entities.EscapeMode.xhtml;
        entity = new Entities();
    }

    @Test
    public void testIsNamedEntity_validEntity_returnsTrue() {
        String name = "amp";
        assertTrue(entity.isNamedEntity(name));
    }

    @Test
    public void testIsNamedEntity_invalidEntity_returnsFalse() {
        String name = "foo";
        assertFalse(entity.isNamedEntity(name));
    }

    @Test
    public void testIsBaseNamedEntity_validEntity_returnsTrue() {
        String name = "amp";
        assertTrue(entity.isBaseNamedEntity(name));
    }

    @Test
    public void testIsBaseNamedEntity_invalidEntity_returnsFalse() {
        String name = "foo";
        assertFalse(entity.isBaseNamedEntity(name));
    }

    @Test
    public void testGetCharacterByName_validEntity_returnsCharacter() {
        String name = "amp";
        Character expectedCharacter = '&';
        assertEquals(expectedCharacter, entity.getCharacterByName(name));
    }

    @Test
    public void testGetCharacterByName_invalidEntity_returnsNull() {
        String name = "foo";
        assertNull(entity.getCharacterByName(name));
    }

    @Test
    public void testEscape_outisNull_returnsEscapedString() {
        String string = "<html> &amp;";
        Document.OutputSettings out = null;
        String expectedString = "&lt;html&gt; &amp;amp;";
        assertEquals(expectedString, entity.escape(string, out));
    }

    @Test
    public void testEscape_outNotNull_returnsEscapedString() {
        String string = "<html> &amp;";
        Document.OutputSettings out = new Document.OutputSettings();
        String expectedString = "&lt;html&gt; &amp;amp;";
        assertEquals(expectedString, entity.escape(string, out));
    }

    @Test
    public void testUnescape_strictFalse_returnsUnescapedString() {
        String string = "&lt;html&gt; &amp;amp;";
        String expectedString = "<html> &";
        assertEquals(expectedString, entity.unescape(string));
    }

    @Test
    public void testUnescape_strictTrue_returnsUnescapedString() {
        String string = "&lt;html&gt; &amp;amp;";
        String expectedString = "<html> &";
        assertEquals(expectedString, entity.unescape(string, true));
    }

    @Test
    public void testLoadEntities_validFilename_returnsLoadedEntities() {
        String filename = "entities-full.properties";
        Map<String, Character> entities = entity.loadEntities(filename);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntities_invalidFilename_throwsMissingResourceException() {
        String filename = "invalid.properties";
        entity.loadEntities(filename);
    }

    @Test
    public void testToCharacterKey_validInput_returnsCharacterKey() {
        Map<Character, String> inMap = entity.loadEntities("entities-full.properties");
        Map<Character, String> outMap = entity.toCharacterKey(inMap);
        assertNotNull(outMap);
        assertFalse(outMap.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToCharacterKey_nullInput_throwsIllegalArgumentException() {
        entity.toCharacterKey(null);
    }
}