package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abcd"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abcd"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abcd"));
    }

    @Test
    public void testEscapeXhtml() {
        String result = Entities.escape("hello world", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("hello world", result);
    }

    @Test
    public void testEscapeBase() {
        String result = Entities.escape("hello world", new Document.OutputSettings().escapeMode(EscapeMode.base));
        assertEquals("hello world", result);
    }

    @Test
    public void testEscapeExtended() {
        String result = Entities.escape("hello world", new Document.OutputSettings().escapeMode(EscapeMode.extended));
        assertEquals("hello world", result);
    }

    @Test
    public void testEscapeWithAttributes() {
        String result = Entities.escape("<div id=\"myDiv\">", new Document.OutputSettings().escapeMode(EscapeMode.base));
        assertEquals("&lt;div id=&quot;myDiv&quot;&gt;", result);
    }

    @Test
    public void testEscapeWithWhitespace() {
        String result = Entities.escape("hello    world", new Document.OutputSettings().escapeMode(EscapeMode.base));
        assertEquals("hello    world", result);
    }

    @Test
    public void testEscapeWithLeadingWhitespace() {
        String result = Entities.escape("   hello world", new Document.OutputSettings().escapeMode(EscapeMode.base));
        assertEquals("   hello world", result);
    }

    @Test
    public void testEscapeWithTrailingWhitespace() {
        String result = Entities.escape("hello world   ", new Document.OutputSettings().escapeMode(EscapeMode.base));
        assertEquals("hello world   ", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;div id=&quot;myDiv&quot;&gt;");
        assertEquals("<div id=\"myDiv\">", result);
    }

    @Test
    public void testUnescapeWithoutSemicolon() {
        String result = Entities.unescape("&lt;div id=&quot;myDiv&quot;&amp");
        assertEquals("&lt;div id=&quot;myDiv&quot;&amp", result);
    }

    @Test
    public void testUnescapeWithoutStrict() {
        String result = Entities.unescape("&#60;div id=&#34;myDiv&#34;&#62;", false);
        assertEquals("<div id=\"myDiv\">", result);
    }

    @Test
    public void testUnescapeWithStrict() {
        String result = Entities.unescape("&#60;div id=&#34;myDiv&#34;&#62;", true);
        assertEquals("<div id=\"myDiv\">", result);
    }

    @Test
    public void testCanEncodeAscii() {
        boolean result = Entities.canEncode(CoreCharset.ascii, '<', null);
        assertTrue(result);
    }

    @Test
    public void testCanEncodeUtf() {
        boolean result = Entities.canEncode(CoreCharset.utf, '∮', null);
        assertTrue(result);
    }

    @Test
    public void testCanEncodeFallback() {
        boolean result = Entities.canEncode(CoreCharset.fallback, '∮', null);
        assertTrue(result);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> result = Entities.loadEntities("entities-full.properties");
        assertFalse(result.isEmpty());
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesMissingResource() {
        Entities.loadEntities("invalid-file.properties");
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inputMap = new HashMap<String, Character>();
        inputMap.put("lt", '<');
        inputMap.put("gt", '>');
        inputMap.put("amp", '&');

        Map<Character, String> result = Entities.toCharacterKey(inputMap);
        assertEquals(3, result.size());
        assertEquals("<", result.get('<'));
        assertEquals(">", result.get('>'));
        assertEquals("&", result.get('&'));
    }
}