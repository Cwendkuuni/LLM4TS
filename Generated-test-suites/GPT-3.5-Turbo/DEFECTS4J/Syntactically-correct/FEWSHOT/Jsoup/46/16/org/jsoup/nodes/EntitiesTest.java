package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {
  
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("abc"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("abc"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape(" ", out));
        assertEquals("&#60;", Entities.escape("<", out));
        assertEquals("&#62;", Entities.escape(">", out));
        assertEquals("\"", Entities.escape("\"", out));
    }

    @Test
    public void testEscapeStringWithOutAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "&", out, false, false, false);
        assertEquals("&amp;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, " ", out, false, false, false);
        assertEquals("&nbsp;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "<", out, false, false, false);
        assertEquals("&lt;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, ">", out, false, false, false);
        assertEquals("&gt;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "\"", out, false, false, false);
        assertEquals("\"", accum.toString());
    }

    @Test
    public void testEscapeStringWithAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "&", out, true, false, false);
        assertEquals("&amp;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, " ", out, true, false, false);
        assertEquals(" ", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "<", out, true, false, false);
        assertEquals("<", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, ">", out, true, false, false);
        assertEquals(">", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "\"", out, true, false, false);
        assertEquals("&quot;", accum.toString());
    }

    @Test
    public void testEscapeStringWithNormaliseWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "&", out, false, true, false);
        assertEquals("&amp;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, " ", out, false, true, false);
        assertEquals(" ", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "<", out, false, true, false);
        assertEquals("&lt;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, ">", out, false, true, false);
        assertEquals("&gt;", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "\"", out, false, true, false);
        assertEquals("\"", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, " \n\t", out, false, true, true);
        assertEquals(" ", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, "<\n\t", out, false, true, true);
        assertEquals("<", accum.toString());

        accum = new StringBuilder();
        Entities.escape(accum, ">\n\t", out, false, true, true);
        assertEquals(">", accum.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals(" ", Entities.unescape("&nbsp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals(" ", Entities.unescape("&nbsp;", true));
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&amp;", Entities.unescape("&amp"));
    }
    
    @Test
    public void testCanEncode() {
        CoreCharset asciiCharset = Entities.CoreCharset.byName("US-ASCII");
        CoreCharset utfCharset = Entities.CoreCharset.byName("UTF-8");
        CharsetEncoder fallbackEncoder = null; // in real implementation, you should pass an actual fallback encoder
        
        assertTrue(Entities.canEncode(asciiCharset, 'a', fallbackEncoder));
        assertTrue(Entities.canEncode(asciiCharset, 'A', fallbackEncoder));
        assertFalse(Entities.canEncode(asciiCharset, 'é', fallbackEncoder));
        
        assertTrue(Entities.canEncode(utfCharset, 'a', fallbackEncoder));
        assertTrue(Entities.canEncode(utfCharset, 'A', fallbackEncoder));
        assertTrue(Entities.canEncode(utfCharset, 'é', fallbackEncoder));
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(151, entities.size());
        assertTrue(entities.containsKey("lt"));
        assertTrue(entities.containsKey("gt"));
        assertTrue(entities.containsKey("amp"));
        assertTrue(entities.containsKey("quot"));
        assertTrue(entities.containsKey("nbsp"));
        assertEquals(Character.valueOf('<'), entities.get("lt"));
        assertEquals(Character.valueOf('>'), entities.get("gt"));
        assertEquals(Character.valueOf('&'), entities.get("amp"));
        assertEquals(Character.valueOf('\"'), entities.get("quot"));
        assertEquals(Character.valueOf('\u00A0'), entities.get("nbsp"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<String, Character>();
        
        inMap.put("lt", '<');
        inMap.put("gt", '>');
        inMap.put("amp", '&');
        inMap.put("quot", '\"');
        inMap.put("nbsp", '\u00A0');
        
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        
        assertEquals(5, outMap.size());
        assertTrue(outMap.containsKey('<'));
        assertTrue(outMap.containsKey('>'));
        assertTrue(outMap.containsKey('&'));
        assertTrue(outMap.containsKey('\"'));
        assertTrue(outMap.containsKey('\u00A0'));
        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
        assertEquals("amp", outMap.get('&'));
        assertEquals("quot", outMap.get('\"'));
        assertEquals("nbsp", outMap.get('\u00A0'));
    }

}