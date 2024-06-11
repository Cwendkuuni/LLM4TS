package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("notAnEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("notAnEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(null, Entities.getCharacterByName("quot"));
        assertEquals(null, Entities.getCharacterByName("notAnEntity"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<div>&amp;</div>", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("&lt;div&gt;&amp;amp;&lt;/div&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;");
        assertEquals("<div>&amp;</div>", unescaped);
    }

    @Test
    public void testEscape_withLeadingWhite() {
        String escaped = Entities.escape("  <div>&amp;</div>", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("  &lt;div&gt;&amp;amp;&lt;/div&gt;", escaped);
    }
    
    @Test
    public void testEscape_inAttribute() {
        String escaped = Entities.escape("<div class=\"&amp;\">", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals("&lt;div class=\"&amp;amp;\"&gt;", escaped);
    }

    @Test
    public void testEscape_normaliseWhite() {
        String escaped = Entities.escape("  <div>\t&amp;\n</div>  ", new Document.OutputSettings().escapeMode(EscapeMode.xhtml).prettyPrint(true));
        assertEquals("  <div> &amp; </div>  ", escaped);
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        String escaped = Entities.escape("  <div> &amp; </div>", new Document.OutputSettings().escapeMode(EscapeMode.xhtml).prettyPrint(true));
        assertEquals("<div> &amp; </div>", escaped);
    }

    @Test
    public void testEscape_canEncode() {
        Map<Character, String> map = EscapeMode.xhtml.getMap();
        map.put('¾', "&frac34;");
        String escaped = Entities.escape("¼", new Document.OutputSettings().escapeMode(EscapeMode.xhtml));
        assertEquals(map.get(Character.valueOf('¾')), escaped);
    }

    @Test
    public void testUnescape_strict() {
        String unescaped = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;", true);
        assertEquals("<div>&amp;</div>", unescaped);
    }

    @Test
    public void testUnescape_notStrict() {
        String unescaped = Entities.unescape("&lt;div&gt;&amp;amp;&lt;/div&gt;", false);
        assertEquals("<div>&amp;</div>", unescaped);
    }
}