package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Entities.EscapeMode;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue("Checking if 'lt' is a known named entity", Entities.isNamedEntity("lt"));
        assertTrue("Checking if 'amp' is a known named entity", Entities.isNamedEntity("amp"));
        assertFalse("Checking if 'invalid' is a known named entity", Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue("Checking if 'lt' is a known named entity in the base set", Entities.isBaseNamedEntity("lt"));
        assertTrue("Checking if 'amp' is a known named entity in the base set", Entities.isBaseNamedEntity("amp"));
        assertFalse("Checking if 'invalid' is a known named entity in the base set", Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals("Getting the Character value of the named entity 'lt'", Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals("Getting the Character value of the named entity 'amp'", Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull("Getting the Character value of the invalid named entity 'invalid'", Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String string = "<html>&amp;</html>";
        String expected = "&lt;html&gt;&amp;amp;&lt;/html&gt;";
        assertEquals("Escaping the string '<html>&amp;</html>'", expected, Entities.escape(string, new Document.OutputSettings()));
    }

    @Test
    public void testEscape_inAttribute() {
        String string = "<a href=\"index.html\"><img src='image.jpg'></a>";
        String expected = "&lt;a href=&quot;index.html&quot;&gt;&lt;img src=&#39;image.jpg&#39;&gt;&lt;/a&gt;";
        assertEquals("Escaping the string '<a href=\"index.html\"><img src='image.jpg'></a>'", expected, Entities.escape(string, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;html&gt;&amp;amp;&lt;/html&gt;";
        String expected = "<html>&amp;</html>";
        assertEquals("Unescaping the string '&lt;html&gt;&amp;amp;&lt;/html&gt;'", expected, Entities.unescape(string));
    }

    @Test
    public void testUnescape_strict() {
        String string = "&lt;html&gt;&amp;amp;&lt;/html&gt;;";
        String expected = "<html>&amp;</html>;";
        assertEquals("Unescaping the string '&lt;html&gt;&amp;amp;&lt;/html&gt;;' with strict flag set to true", expected, Entities.unescape(string, true));
    }

    @Test
    public void testEscapeMode_getMap() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        assertEquals("Checking if xhtml escape mode contains the correct map size", 4, xhtmlMap.size());
        assertTrue("Checking if xhtml escape mode map contains the character '&'", xhtmlMap.containsKey('&'));
        assertEquals("Checking if xhtml escape mode map returns the correct value for the character '&'", "amp", xhtmlMap.get('&'));
    }
}