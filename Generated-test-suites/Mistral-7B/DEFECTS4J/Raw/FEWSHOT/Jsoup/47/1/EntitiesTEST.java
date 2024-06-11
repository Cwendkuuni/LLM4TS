package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknownentity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknownentity"));
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
        assertEquals('<', map.get( '<' ));
        assertEquals('&', map.get( '&' ));
        assertEquals('>', map.get( '>' ));
        assertEquals('"', map.get( '"' ));
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> map = Entities.EscapeMode.base.getMap();
        assertEquals('<', map.get( '<' ));
        assertEquals('&', map.get( '&' ));
        assertEquals('>', map.get( '>' ));
        assertEquals('"', map.get( '"' ));
        assertEquals("&nbsp;", map.get((char) 0xA0));
    }

    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> map = Entities.EscapeMode.extended.getMap();
        assertEquals('<', map.get( '<' ));
        assertEquals('&', map.get( '&' ));
        assertEquals('>', map.get( '>' ));
        assertEquals('"', map.get( '"' ));
        // Extended set contains all base+xhtml+more
        assertEquals("&eacute;", map.get((char) 0x00C9)); // eacute
        assertEquals("&agrave;", map.get((char) 0x00C0)); // agrave
        // ... add more tests as needed
    }

    @Test
    public void testEscape() {
        StringBuilder accum = new StringBuilder();
        String test = "&lt; &amp; &gt; &quot; &apos; &copy; &reg; &trade; &eacute; &agrave; &nbsp; &iexcl; &oelig; &Scaron; &uuml;";
        Entities.escape(accum, test, null, false, false, false);
        assertEquals("&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;apos; &copy; &reg; &trade; &eacute; &agrave; &nbsp; &iexcl; &oelig; &Scaron; &uuml;", accum.toString());
    }

    @Test
    public void testUnescape() {
        String test = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;apos; &copy; &reg; &trade; &eacute; &agrave; &nbsp; &iexcl; &oelig; &Scaron; &uuml;";
        assertEquals("&lt; &amp; &gt; &quot; &apos; © ® ™ é à ", Entities.unescape(test));
    }

    @Test
    public void testCanEncode() {
        // Test the fast path for Encoder.canEncode.
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, ' '));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '!'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '('));
        assertTrue(Entities.canEncode(CoreCharset.ascii, ')'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '*'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '+'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '-'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '.'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '/'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '0'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '1'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '2'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '3'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '4'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '5'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '6'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '7'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '8'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, '9'));
        assertFalse(Entities.canEncode(CoreCharset.ascii, ':')); // colon is not ASCII

        assertTrue(Entities.canEncode(CoreCharset.utf, '°'));
        assertTrue(Entities.canEncode(CoreCharset.utf, '€'));
        assertTrue(Entities.canEncode(CoreCharset.utf, 'µ'));
        // Add more tests as needed.
        // ...
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals('&', entities.get("amp"));
        assertEquals('<', entities.get("lt"));
        assertEquals('>', entities.get("gt"));
        assertEquals('"', entities.get("quot"));
        assertEquals((char) 0xA0, entities.get("nbsp"));
        // Add more tests as needed.
        // ...
    }
}