package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.fullByVal;
        for (Map.entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entityCodePoint = entry.getValue();
            assertTrue("Named entity '" + entityName + "' is not recognized", Entities.isNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' returns wrong character", entityCodePoint, Entities.getCharacterByName(entityName));
        }

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character entityCodePoint = entry.getKey();
            String entityName = entry.getValue();
            assertTrue("XHTML named entity '" + entityName + "' is not recognized", Entities.isNamedEntity(entityName));
            assertEquals("XHTML named entity '" + entityName + "' returns wrong character", entityCodePoint, Entities.getCharacterByName(entityName));
        }

        assertFalse("Unknown named entity is recognized", Entities.isNamedEntity("unkownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.baseByVal;
        for (Map.entry<String, Character> entry : baseEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entityCodePoint = entry.getValue();
            assertTrue("Base named entity '" + entityName + "' is not recognized", Entities.isBaseNamedEntity(entityName));
            assertEquals("Base named entity '" + entityName + "' returns wrong character", entityCodePoint, Entities.getCharacterByName(entityName));
        }

        String unknownEntity = "unkownEntity";
        assertFalse("Unknown named entity is recognized", Entities.isBaseNamedEntity(unknownEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.fullByVal;
        for (Map.entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entityCodePoint = entry.getValue();
            assertEquals("Named entity '" + entityName + "' returns wrong character", entityCodePoint, Entities.getCharacterByName(entityName));
        }

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character entityCodePoint = entry.getKey();
            String entityName = entry.getValue();
            assertEquals("XHTML named entity '" + entityName + "' returns wrong character", entityCodePoint, Entities.getCharacterByName(entityName));
        }

        assertNull("Unknown named entity returns non-null character", Entities.getCharacterByName("unkownEntity"));
    }

    @Test
    public void testEscape() {
        String testString = "This is a test &lt; with &amp; entities and &#x00A9; copyright symbol";

        assertEquals("This is a test &amp;lt; with &amp;amp; entities and &amp;#x00A9; copyright symbol", Entities.escape(testString, null));

        Map<Character, String> customMap = new HashMap<>();
        customMap.put('&', "ampersand");
        customMap.put('<', "lessThan");
        Document.OutputSettings outputSettings = new Document.OutputSettings(Entities.EscapeMode.base, customMap, null);

        assertEquals("This is a test ampersand&lt; with ampersandamp; entities and &amp;#x00A9; copyright symbol", Entities.escape(testString, outputSettings));
    }

    @Test
    public void testUnescape() {
        String testString = "This is a test &lt; with &amp; entities and &apos;quotes&apos; &#x00A9; copyright symbol";

        assertEquals("This is a test < with entities and 'quotes' © copyright symbol", Entities.unescape(testString));
        assertEquals("This is a test <with entities and 'quotes' > © copyright symbol", Entities.unescape(testString, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNull() {
        Entities.unescape(null);
    }

    @Test
    public void testCanEncode() {
        assertFalse("Can't encode empty charset", Entities.canEncode(Entities.CoreCharset.ascii, '\u0000', null));
        assertFalse("Can't encode non-ASCII character using ascii charset", Entities.canEncode(Entities.CoreCharset.ascii, '\u00E4', null));

        assertTrue("Can encode ASCII character using ascii charset", Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        assertTrue("Can encode 'normal' UTF-8 character using UTF charset", Entities.canEncode(Entities.CoreCharset.utf, '\u00E4', null));
        assertTrue("Can encode 'surrogate' UTF-8 character using UTF charset", Entities.canEncode(Entities.CoreCharset.utf, '\uD835\uDC00', null));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-custom.properties");

        assertEquals(1, entities.size());
        assertEquals('&', entities.get("amp"));
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> map = new HashMap<>();
        map.put('&', "ampersand");
        map.put('>', "greaterThan");

        Map<Character, String> expected = new HashMap<>();
        expected.put('&', "ampersand");
        expected.put('>', "greaterThan");

        Map<Character, String> result = Entities.toCharacterKey(map);

        assertEquals(expected, result);
    }
}