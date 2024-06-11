package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Parser;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.CoreCharset;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(EscapeMode.complete);
        assertEquals("&#60;&#62;&amp;&quot;", Entities.escape("<>&\"", out));

        out.escapeMode(EscapeMode.xhtml);
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"", out));

        out.escapeMode(EscapeMode.base);
        assertEquals("<>&\"", Entities.escape("<>&\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<<>>&&\"\"", Entities.unescape("&lt;&lt;&gt;&gt;&amp;&amp;&quot;&quot;"));
        assertEquals("<>&\"\"", Entities.unescape("<>&\"\"", true));
        assertEquals("<>&\"\"", Entities.unescape("<>&\"\"", false));
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = CharsetEncoder.availableCharsets().get("US-ASCII").newEncoder();
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', encoder));
        assertFalse(Entities.canEncode(CoreCharset.ascii, 'ü', encoder));

        encoder = CharsetEncoder.availableCharsets().get("UTF-8").newEncoder();
        assertTrue(Entities.canEncode(CoreCharset.utf, 'A', encoder));

        encoder = CharsetEncoder.availableCharsets().get("ISO-8859-1").newEncoder();
        assertTrue(Entities.canEncode(CoreCharset.fallback, 'A', encoder));
    }
    
    // Additional test cases for loadEntities and toCharacterKey methods
    @Test
    public void testLoadEntities() {
        Map<String, Character> expectedEntities = new HashMap<>();
        expectedEntities.put("nbsp", ' ');
        expectedEntities.put("hello", 'h');

        Map<String, Character> loadedEntities = Entities.loadEntities("test-entities.properties");

        assertEquals(expectedEntities.size(), loadedEntities.size());
        assertTrue(loadedEntities.containsKey("nbsp"));
        assertTrue(loadedEntities.containsKey("hello"));
        assertEquals(Character.valueOf(' '), loadedEntities.get("nbsp"));
        assertEquals(Character.valueOf('h'), loadedEntities.get("hello"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("hello", 'h');
        inMap.put("Hello", 'h');
        inMap.put("world", 'w');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(2, outMap.size());
        assertTrue(outMap.containsKey('h'));
        assertTrue(outMap.containsKey('w'));
        assertEquals("Hello", outMap.get('h'));
        assertEquals("world", outMap.get('w'));
    }
}