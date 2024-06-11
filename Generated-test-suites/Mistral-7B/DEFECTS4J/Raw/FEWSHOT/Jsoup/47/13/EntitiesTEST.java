package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity " + name + " is not recognized", Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.base;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity " + name + " is not in base entities", Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertFalse("Entity not in base set: 'foo'", Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
    }

    @Test
    public void testEscapeMode() {
        assertEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.valueOf("xhtml"));
        assertEquals(Entities.EscapeMode.base, Entities.EscapeMode.valueOf("base"));
        assertEquals(Entities.EscapeMode.extended, Entities.EscapeMode.valueOf("extended"));

        try {
            Entities.EscapeMode.valueOf("unknown");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsNamedEntityMap() {
        Map<Character, String> xhtmlEntities = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseEntities = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedEntities = Entities.EscapeMode.extended.getMap();

        assertFalse("Unexpected entity in xhtml map: 'foo'", xhtmlEntities.containsKey('foo'));
        assertTrue("Missing entity in xhtml map: 'lt'", xhtmlEntities.containsKey(Entities.getCharacterByName("lt")));

        assertFalse("Unexpected entity in base map: 'foo'", baseEntities.containsKey('foo'));
        assertTrue("Missing entity in base map: 'lt'", baseEntities.containsKey(Entities.getCharacterByName("lt")));

        assertFalse("Unexpected entity in extended map: 'foo'", extendedEntities.containsKey('foo'));
        assertTrue("Missing entity in extended map: 'lt'", extendedEntities.containsKey(Entities.getCharacterByName("lt")));
        assertTrue("Unexpected entity in extended map: 'gt'", extendedEntities.containsKey(Entities.getCharacterByName("gt")));
        assertFalse("Missing entity in extended map: 'zap'", extendedEntities.containsKey(Entities.getCharacterByName("zap")));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals("&lt;foo&gt;", Entities.escape("foo<bar>", settings));
        assertEquals("foo&gt;bar", Entities.escape("foo>bar", settings));
        assertEquals("foo &amp; bar", Entities.escape("foo & bar", settings));
        assertEquals("foo &amp; bar &amp; baz", Entities.escape("foo & bar & baz", settings));
        assertEquals("foo &quo;t &amp; &quot;bar&quot; &amp; &amp;amp; baz", Entities.escape("foo \"quot\"t & &amp; \"bar\" &amp; &amp;amp; baz", settings));

        settings.setEscapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&lt;foo&gt;", Entities.escape("foo<bar>", settings));
        assertEquals("foo&gt;bar", Entities.escape("foo>bar", settings));
        assertEquals("foo&amp;bar", Entities.escape("foo & bar", settings));
        assertEquals("foo&amp;baz", Entities.escape("foo & baz", settings));
    }

    @Test
    public void testUnescape() {
        assertEquals("foo<bar>", Entities.unescape("&lt;foo&gt;bar", false));
        assertEquals("foo>bar", Entities.unescape("foo&gt;bar", false));
        assertEquals("foo & bar", Entities.unescape("foo&amp;bar", false));
        assertEquals("foo &amp; bar &amp; baz", Entities.unescape("foo&amp;baz&amp;bar", false));
        assertEquals("foo \"quot\"t & &amp; \"bar\"", Entities.unescape("foo &quo;t & &amp; \"bar\"", false));

        assertEquals("foo<bar>", Entities.unescape("&lt;foo&gt;bar", true));
        assertEquals("foo>bar", Entities.unescape("foo&gt;bar", true));
        assertEquals("foo & bar", Entities.unescape("foo&amp
 continued ...
        amp;bar", true));
        assertEquals("foo &amp; bar &amp; baz", Entities.unescape("foo&amp;baz&amp;bar", true));
        assertEquals("foo \"quot\"t & &amp; \"bar\"", Entities.unescape("foo &quo;t & &amp; \"bar\"", true));
    }

    @Test
    public void testCanEncode() {
        CoreCharset ascii = CoreCharset.ascii;
        CoreCharset utf = CoreCharset.utf;
        CoreCharset fallback = CoreCharset.fallback;
        CharsetEncoder encoder = Character.encodingCharset().newEncoder();

        for (char c = 0x00; c <= 0x7F; c++) {
            boolean expected = (c < 0x20 || c == 0x22 || c == 0x23 || c == 0x25 || c == 0x26 || c == 0x27 || c == 0x5B || c == 0x5D || c == 0x5C || c == 0x7F);
            boolean actual = Entities.canEncode(ascii, c, encoder);
            assertEquals("Can encode character 0x" + Integer.toHexString(c) + ": " + expected + " != " + actual, expected, actual);
        }

        for (char c = 0xA0; c <= 0xFFFF; c++) {
            boolean expected = false;
            boolean actual = Entities.canEncode(utf, c, encoder);
            assertEquals("Can encode character 0x" + Integer.toHexString(c) + ": " + expected + " != " + actual, expected, actual);
        }

        for (char c = 0x10000; c <= 0x10FFFF; c++) {
            boolean expected = false;
            boolean actual = Entities.canEncode(fallback, c, encoder);
            assertEquals("Can encode character 0x" + Integer.toHexString(c) + ": " + expected + " != " + actual, expected, actual);
        }
    }

    @Test
    public void testEscapeWithDifferentCharsets() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.encoder(Charset.forName("UTF-8"));

        assertEquals("&amp;foo;&#xA0;", Entities.escape("&foo;", settings));
        assertEquals("&amp;foo;&#xA0;", Entities.escape("foo", settings));
        assertEquals("foo", Entities.unescape("&amp;foo;&#xA0;", false));
        assertEquals("foo", Entities.unescape("&amp;foo;&#xA0;", true));

        settings.encoder(Charset.forName("ISO-8859-1"));

        assertEquals("&amp;#160;foo", Entities.escape("&foo;", settings));
        assertEquals("&amp;#160;foo", Entities.escape("foo", settings));
        assertEquals("foo", Entities.unescape("&amp;#160;foo", false));
        assertEquals("foo", Entities.unescape("&amp;#160;foo", true));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(128, entities.size());

        Map<Character, String> reversedEntities = new HashMap<>();
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            reversedEntities.put(entry.getValue(), entry.getKey());
        }

        assertEquals(' ', reversedEntities.get('\u0020'));
        assertEquals('\u00A0', reversedEntities.get(Character.forName("nbsp")));
        assertEquals('\u00A1', reversedEntities.get(Character.forName("iexcl")));
        // ... other tests omitted for brevity
        assertEquals('\u00DF', reversedEntities.get(Character.forName("zlig")));

        Map<Character, String> extendedEntities = Entities.loadEntities("entities-extended-latin1.properties");
        assertEquals(1052, extendedEntities.size());

        Map<Character, String> reversedExtendedEntities = new HashMap<>();
        for (Map.Entry<String, Character> entry : extendedEntities.entrySet()) {
            reversedExtendedEntities.put(entry.getValue(), entry.getKey());
        }

        assertEquals('\u0131', reversedExtendedEntities.get(Character.forName("LATIN SMALL LETTER SHARP S")));
        assertEquals('\u0133', reversedExtendedEntities.get(Character.forName("LATIN SMALL LETTER S TAKEN FROM C")));
        assertEquals('\u0141', reversedExtendedEntities.get(Character.forName("LATIN CAPITAL LETTER A WITH MACRON")));
        // ... other tests omitted for brevity
        assertEquals('\u017D', reversedExtendedEntities.get(Character.forName("LATIN CAPITAL LETTER L WITH MIDDLE TILDE")));
    }
}