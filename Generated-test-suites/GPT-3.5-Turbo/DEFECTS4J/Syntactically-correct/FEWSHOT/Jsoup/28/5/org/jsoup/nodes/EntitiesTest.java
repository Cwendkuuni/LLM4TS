package org.jsoup.nodes;

import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('\u00022'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('\u00026'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\u00027'), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\u0003C'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('\u0003E'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        CharsetEncoder encoder = CharsetEncoder.defaultCharset().newEncoder();
        assertEquals("foo", Entities.escape("foo", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&apos;", Entities.escape("'", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&#37;", Entities.escape("%", out));
        assertEquals("&#x55;", Entities.escape("U", out));

        EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        assertEquals("&amp;", Entities.escape("&", encoder, xhtmlMode));
        assertEquals("&lt;", Entities.escape("<", encoder, xhtmlMode));
        assertEquals("&gt;", Entities.escape(">", encoder, xhtmlMode));
        assertEquals("&quot;", Entities.escape("\"", encoder, xhtmlMode));
        assertEquals("&apos;", Entities.escape("'", encoder, xhtmlMode));
        assertEquals("foo", Entities.escape("foo", encoder, xhtmlMode));
        assertEquals("&#37;", Entities.escape("%", encoder, xhtmlMode));
        assertEquals("&#x55;", Entities.escape("U", encoder, xhtmlMode));
    }

    @Test
    public void testUnescape() {
        assertEquals("foo", Entities.unescape("foo"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("%", Entities.unescape("&#37;"));
        assertEquals("U", Entities.unescape("&#x55;"));
        assertEquals("&#", Entities.unescape("&#"));
        assertEquals("&foo;", Entities.unescape("&foo;"));
        assertEquals("&#;", Entities.unescape("&#;"));
        assertEquals("&foo", Entities.unescape("&foo"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("foo", Entities.unescape("foo", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("'", Entities.unescape("&apos;", true));
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("%", Entities.unescape("&#37;", true));
        assertEquals("U", Entities.unescape("&#x55;", true));
        assertEquals("&#", Entities.unescape("&#", true));
        assertEquals("&foo;", Entities.unescape("&foo;", true));
        assertEquals("&#;", Entities.unescape("&#;", true));
        assertEquals("&foo", Entities.unescape("&foo", true));
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> expected = new HashMap<String, Character>();
        expected.put("foo", 'A');
        expected.put("bar", 'B');
        
        Map<String, Character> result = Entities.loadEntities("test-entities.properties");
        
        assertEquals(expected, result);
    }
}