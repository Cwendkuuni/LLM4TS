package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;

        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue(Entities.isNamedEntity(entityName));
        }

        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;

        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertEquals(entity, Entities.getCharacterByName(entityName));
        }

        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape_Default() {
        String string = "&amp;< >'\"";
        String expected = "&amp;amp;;&lt; &gt;'&apos;\"";

        assertEquals(expected, Entities.escape(string, null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscape_XHTML() {
        String string = "&amp;< >'\"";
        String expected = "&amp;";

        assertEquals(expected, Entities.escape(string, null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testEscape_Extended() {
        String string = "&amp;< >'\"";
        String expected = "&amp;amp;;&lt; &gt;'&apos;\"";

        assertEquals(expected, Entities.escape(string, null, Entities.EscapeMode.extended));
    }

    @Test
    public void testEscape_CustomEncoder() {
        String string = "é";
        String expected = "&Atilde;";

        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        assertEquals(expected, Entities.escape(string, encoder, Entities.EscapeMode.base));
    }

    @Test
    public void testUnescape() {
        String string = "&amp;amp;;&lt; &gt;'&apos;\"";
        String expected = "&amp;< >'\"";

        assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescape_Strict() {
        String string = "&amp;amp;;&lt; &gt;'&apos;\"";
        String expected = "&amp;amp;amp;lt; &amp;gt;'&apos;\"";

        assertEquals(expected, Entities.unescape(string, true));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");

        assertEquals(Entities.base.size(), loadedEntities.size());
        assertEquals(Entities.base, loadedEntities);
    }

    @Test(expected = org.jsoup.nodes.MissingResourceException.class)
    public void testLoadEntities_MissingFile() {
        Entities.loadEntities("missing-file.properties");
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<>();
        entities.put("quot", '\"');
        entities.put("amp", '&');

        Map<Character, String> expectedMap = new HashMap<>();
        expectedMap.put('\"', "quot");
        expectedMap.put('&', "amp");

        Map<Character, String> resultMap = Entities.toCharacterKey(entities);

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testXhtmlArrayInitialization() {
        Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;

        assertEquals(5, xhtmlByVal.size());
        assertEquals("quot", xhtmlByVal.get('"'));
        assertEquals("amp", xhtmlByVal.get('&'));
        // ...
    }
}