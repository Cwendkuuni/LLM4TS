package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(namedEntity));
            assertEquals(character, Entities.getCharacterByName(namedEntity));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String namedEntity = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(namedEntity));
            assertEquals(character, Entities.getCharacterByName(namedEntity));
        }

        // check for entities not in the base set
        assertFalse(Entities.isBaseNamedEntity("lt"));
        assertNull(Entities.getCharacterByName("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.fullByVal;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(namedEntity));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStringForIsNamedEntity() {
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStringForGetCharacterByName() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeMethodBasic() {
        Entities entities = new Entities();
        String string = "&amp;<tag>&quot;";
        StringBuilder expected = new StringBuilder();
        expected.append("&amp;");
        expected.append("&lt;tag&gt;");
        expected.append("&quot;");

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, string, null, false, false, false);
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testEscapeMethodWithInAttribute() {
        Entities entities = new Entities();
        String string = "&amp;<tag attr=\"&quot; &amp;lt;&gt; &amp;quot;>";
        StringBuilder expected = new StringBuilder();
        expected.append("&amp;amp;");
        expected.append("&lt;tag attr=&quot; &amp;lt;&gt; &amp;quot;&gt;");

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, string, null, true, false, false);
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testEscapeMethodWithNormaliseWhite() {
        Entities entities = new Entities();
        String string = " \t&nbsp;hello world \t";
        StringBuilder expected = new StringBuilder();
        expected.append(" hello world ");

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, string, null, false, true, false);
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testEscapeMethodWithStripLeadingWhite() {
        Entities entities = new Entities();
        String string = " \t&nbsp;hello world \t";
        StringBuilder expected = new StringBuilder();
        expected.append("hello world ");

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, string, null, false, true, true);
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset charset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null; // not used

        // Ascii characters
        for (char c = 0; c < 128; c++) {
            assertTrue(entities.canEncode(charset, c, encoder));
        }

        // Some surrogate pairs
        char c1 = 0xD800;
        char c2 = 0xDC00;
        assertTrue(entities.canEncode(charset, c1, encoder));
        assertTrue(entities.canEncode(charset, c2, encoder));
        assertFalse(entities.canEncode(charset, c1 + c2, encoder));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithInvalidCoreCharset() {
        Entities entities = new Entities();
        CoreCharset charset = null;
        CharsetEncoder encoder = null; // not used

        entities.canEncode(charset, 'a', encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithInvalidCharacter() {
        Entities entities = new Entities();
        CoreCharset charset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null; // not used

        entities.canEncode(charset, '\uD800', encoder);
    }

    @Test
    public void testUnescapeMethodBasic() {
        Entities entities = new Entities();
        String string = "&amp;<tag>&quot;";
        String expected = "<tag>&quot;";

        String unescaped = entities.unescape(string);
        assertEquals(expected, unescaped);
    }

    @Test
    public void testUnescapeMethodStrict() {
        Entities entities = new Entities();
        String string = "&amp;<tag>&quot;";
        String expected = "<tag>&quot;";

        String unescaped = entities.unescape(string, true);
        assertEquals(expected, unescaped);
    }

    @Test
    public void testUnescapeMethodInvalidInput() {
        Entities entities = new Entities();
        String string = "&amp;<tag>&quot;";
        assertNull(entities.unescape(null));
        assertNull(entities.unescape("", false));
        assertNull(entities.unescape("", true));
    }

    @Test
    public void testIsNamedEntityWithUpperCase() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String namedEntity = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(namedEntity));
            assertTrue(Entities.isNamedEntity(namedEntity.toUpperCase()));
            assertEquals(character, Entities.getCharacterByName(namedEntity));
            assertEquals(character, Entities.getCharacterByName(namedEntity.toUpperCase()));
        }
    }

    @Test
    public void testIsBaseNamedEntityWithUpperCase() {
        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String namedEntity = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(namedEntity));
            assertTrue(Entities.isBaseNamedEntity(namedEntity.toUpperCase()));
            assertEquals(character, Entities.getCharacterByName(namedEntity));
            assertEquals(character, Entities.getCharacterByName(namedEntity.toUpperCase()));
        }
    }

}