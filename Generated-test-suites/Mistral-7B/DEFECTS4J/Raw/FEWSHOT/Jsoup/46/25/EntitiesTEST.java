package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entity : fullEntities.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
        }

        Map<Character, String> baseEntities = Entities.base;
        for (Map.Entry<Character, String> entity : baseEntities.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
            assertTrue(Entities.isBaseNamedEntity(entity.getKey().toString()));
        }

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entity : xhtmlEntities.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
            assertTrue(Entities.isBaseNamedEntity(entity.getKey().toString()));
        }

        // Test invalid entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("lt1"));
        assertFalse(Entities.isNamedEntity("\u0000"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entity : fullEntities.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        Map<Character, String> baseEntities = Entities.base;
        for (Map.Entry<Character, String> entity : baseEntities.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entity : xhtmlEntities.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        // Test invalid entities
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("lt1"));
        assertNull(Entities.getCharacterByName("\u0000"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String string = "&lt;foo&gt;<bar>&quot;quoted&quot; &amp;&amp; ampersand &quot;quoted&quot;";

        StringBuilder expected = new StringBuilder();
        expected.append("&#x26;lt;foo&#x26;gt;&lt;bar&#x26;quot;quoted&#x26;quot; &amp;amp; ampersand &#x26;quot;quoted&#x26;quot;");

        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, string, out, false, false, false);

        assertEquals(expected.toString(), actual.toString());

        out = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        actual.setLength(0);
        Entities.escape(actual, string, out, false, false, false);

        expected.setLength(0);
        expected.append("<foo>&lt;bar&quot;quoted&quot; ampersand &quot;quoted&quot;");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUnescape() {
        String string = "&#x26;lt;foo&#x26;gt;&lt;bar&#x26;quot;quoted&amp;amp; ampersand &#x26;quot;quoted&#x26;quot;";

        StringBuilder expected = new StringBuilder();
        expected.append("&lt;foo&gt;<bar>&quot;quoted&quot; &amp;amp; ampersand &quot;quoted&quot;");

        StringBuilder actual = new StringBuilder();
        Entities.unescape(actual, string, false);

        assertEquals(expected.toString(), actual.toString());

        string = "&lt;foo&gt;<bar>&quot;quoted&quot; &amp;amp; ampersand &#x26;quot;quoted&#x26;quot;";

        actual.setLength(0);
        Entities.unescape(actual, string, true);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCanEncodeNullCoreCharset() {
        Entities.canEncode(null, '\u0001', null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeInvalidCharset() {
        Entities.canEncode(new Entities.CoreCharset() {
            @Override
            public static Entities.CoreCharset byName(String name) {
                throw new IllegalArgumentException("Invalid charset name");
            }
        }, '\u0001', null);
    }

    @Test
    public void testCanEncodeAscii() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, ' ', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u007F', null));
    }

    @Test
    public void testCanEncodeUtf() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0080', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\uD800', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\uDBFF', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\uDFFF', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0345', null)); // Greek letter small omicron
    }

    @Test
    public void testCanEncodeFallback() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '€', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '„', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '“', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '≈', null));
    }

    @Test
    public void testCanEncodeNonAsciiForAndroid() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0123', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0124', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0125', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0126', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0127', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0128', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u0129', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, '\u012A', null));

        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0123', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0124', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0125', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0126', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0127', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0128', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u0129', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u012A', null));

        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0123', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0124', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0125', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0126', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0127', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0128', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u0129', null));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.fallback, '\u012A', null));

        // Test non-ASCII characters that are not encodable by Android's CharsetEncoder
        String nonAsciiChars = "🍎🍌🍊🥝🥝🌶🥕🥗🧅";

        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, nonAsciiChars, null, false, false, false);

        StringBuilder expected = new StringBuilder();
        expected.append("&#x1F34F;&#x1F34D;&#x1F345;&#x1F342;&#x1F342;&#x1F336;&#x1F336;&#x1F331;&#x1F33E;&#x1F33E;&#x1F33D;&#x1F33B;&#x1F33B;");

        assertEquals(expected.toString(), actual.toString());
    }
}