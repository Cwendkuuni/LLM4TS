package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknowne"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknowne"));
    }

    @Test
    public void testGetCharacterByNameXHTML() {
        assertEquals('<', Entities.getCharacterByName(Entities.EscapeMode.xhtml.name().toUpperCase(), "lt"));
        assertEquals('&', Entities.getCharacterByName(Entities.EscapeMode.xhtml.name().toUpperCase(), "amp"));
        assertNull(Entities.getCharacterByName(Entities.EscapeMode.xhtml.name().toUpperCase(), "unknowne"));
    }

    @Test
    public void testGetCharacterByNameBase() {
        assertEquals('<', Entities.getCharacterByName(Entities.EscapeMode.base.name().toUpperCase(), "lt"));
        assertEquals('&', Entities.getCharacterByName(Entities.EscapeMode.base.name().toUpperCase(), "amp"));
        assertNull(Entities.getCharacterByName(Entities.EscapeMode.base.name().toUpperCase(), "unknowne"));
    }

    @Test
    public void testGetCharacterByNameExtended() {
        assertEquals('<', Entities.getCharacterByName(Entities.EscapeMode.extended.name().toUpperCase(), "lt"));
        assertEquals('&', Entities.getCharacterByName(Entities.EscapeMode.extended.name().toUpperCase(), "amp"));
        assertEquals("'", Entities.getCharacterByName(Entities.EscapeMode.extended.name().toUpperCase(), "apos"));
        assertNull(Entities.getCharacterByName(Entities.EscapeMode.extended.name().toUpperCase(), "unknowne"));
    }

    @Test
    public void testEscape() {
        String string = "&lt; &gt; &amp; '&apos; &quot; &e &nbsp; &#123; &#x123; &#x34F;";
        assertEquals("&amp;lt;&gt;&amp;amp;&apos;&quot; &amp;#x22; &amp;#xA0; &amp;#x0233; &amp;#x1033;", Entities.escape(string, null, Entities.EscapeMode.base));
        assertEquals("&amp;lt;&gt;&amp;amp;&apos;&quot; &amp;#x22; &#xA0; &#x0233; &#x1033;", Entities.escape(string, null, Entities.EscapeMode.xhtml));
        assertEquals("&amp;lt;&gt;&amp;amp;&apos;&quot; &amp;#x22; &#xA0; &#x0233; &#x1033;", Entities.escape(string, null, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String string = "&amp;lt;&gt;&amp;amp;&apos;&quot; &amp;#x22; &amp;#xA0; &amp;#x0233; &amp;#x1033;";
        assertEquals("&lt;>&gt;&amp; &apos;&quot; \" &amp;#xA0; &amp;#x0233; &amp;#x1033;", Entities.unescape(string));
        assertEquals("&lt;>&gt;&amp; &apos;&quot; \" &amp;#xA0; &amp;#x0233; &amp;#x1033;", Entities.unescape(string, false));
        assertEquals("&lt;>&gt;&amp; &apos;&quot; \" \u00A0 \u0233 \u1033", Entities.unescape(string, true));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> map = Entities.loadEntities("entities-base.properties");
        assertEquals(38, map.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> map = new HashMap<>();
        map.put("lt", '<');
        map.put("amp", '&');
        map.put("apos", '\'');
        map.put("quot", '"');
        Map<Character, String> newMap = Entities.toCharacterKey(map);
        assertEquals(4, newMap.size());
        assertEquals("quot", newMap.get('<'));
        assertEquals("amp", newMap.get('&'));
        assertEquals("apos", newMap.get('\''));
        assertEquals("lt", newMap.get((char) 0x0003C));
    }
}