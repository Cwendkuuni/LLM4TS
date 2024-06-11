package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' corresponds to character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity '" + name + "' should be known", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity '" + name + "' corresponds to character " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("'foo' should not be a base named entity", Entities.isBaseNamedEntity("foo"));
        assertFalse("'&foo;' should not be a base named entity", Entities.isBaseNamedEntity("&foo;"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' corresponds to character " + character, character, Entities.getCharacterByName(name));
        }

        assertNull("getCharacterByName should return null for unknown entity", Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscapeModeXhtml() {
        Character[] chars = { '&', '<', '>', '"', '\u00A0' };
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
        for (Character character : chars) {
            String escaped = map.get(character);
            assertEquals("xhtml should escape '" + character + "' as '" + escaped + "'", escaped, Entities.getCharacterByName(escaped));
        }
    }

    @Test
    public void testEscapeModeBase() {
        Character[] chars = { '&', '<', '>', '"' };
        Map<Character, String> map = Entities.EscapeMode.base.getMap();
        for (Character character : chars) {
            String escaped = map.get(character);
            String unescaped = Entities.getCharacterByName(escaped);
            assertEquals("base should escape '" + character + "' as '" + escaped + "' and unescape as '" + unescaped + "'", unescaped, Entities.getCharacterByName(escaped));
        }

        assertNull("base should not escape character '" + '\u00A0' + "'", Entities.getCharacterByName("\u00A0"));
    }

    @Test
    public void testEscapeModeExtended() {
        Character[] chars = { '&', '<', '>', '"', '\u00A0', '\u00E1' }; // 'á' is in extended set
        Map<Character, String> map = Entities.EscapeMode.extended.getMap();
        for (Character character : chars) {
            String escaped = map.get(character);
            String unescaped = Entities.getCharacterByName(escaped);
            assertEquals("extended should escape '" + character + "' as '" + escaped + "' and unescape as '" + unescaped + "'", unescaped, Entities.getCharacterByName(escaped));
        }
    }

    @Test
    public void testEscape() {
        String source = "&lt;foo&quot;bar&gt;";
        StringBuilder expected = new StringBuilder();
        Entities.escape(expected, source, null, false, false, false);
        String target = expected.toString();
        String reference = "<foo\">bar</fo>";
        assertEquals("Escaped string should be '" + reference + "'", reference, target);

        expected.setLength(0);
        source = "&amp;lt;foo&quot;bar&gt;";
        Entities.escape(expected, source, null, false, false, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference + "' even if input is already escaped", reference, target);

        expected.setLength(0);
        source = "&amp;amp;lt;foo&quot;bar&gt;";
        Entities.escape(expected, source, null, false, false, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference + "' even if input is already doubly escaped", reference, target);

        expected.setLength(0);
        source = "&lt;foo&quot;bar&gt;";
        Entities.escape(expected, source, null, true, false, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference + "' when in attribute", reference, target);

        expected.setLength(0);
        source = "&lt;foo bar&quot;&gt;";
        Entities.escape(expected, source, null, false, false, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference + "' when whitespace present", reference, target);

        expected.setLength(0);
        source = "&lt;foo bar&quot;&gt;";
        Entities.escape(expected, source, null, false, true, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference.replaceAll("\\s", " ") + "' when normalizing whitespace", reference.replaceAll("\\s", " "), target);

        expected.setLength(0);
        source = " &lt;foo bar&quot; &gt;";
        Entities.escape(expected, source, null, false, false, false);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference + "' when leading/trailing whitespace present", reference, target);

        expected.setLength(0);
        source = " &lt;foo bar&quot; &gt;";
        Entities.escape(expected, source, null, false, false, true);
        target = expected.toString();
        assertEquals("Escaped string should be '" + reference.replaceAll("^\\s|\\s$", "") + "' when stripping leading/trailing whitespace", reference.replaceAll("^\\s|\\s$", ""), target);
    }

    @Test
    public void testUnescape() {
        String source = "<foo>&#x61;&#x6C;&#x65;&#xA0;&#xC3;&#xA1;</foo>";
        String reference = "<foo>abcá</foo>";
        String unescaped = Entities.unescape(source, false);
        assertEquals("Unescaped string should be '" + reference + "'", reference, unescaped);

        unescaped = Entities.unescape(source, true);
        assertEquals("Unescaped string should be '" + reference + "' even if input is already unescaped", reference, unescaped);
    }

    @Test
    public void testCanEncode() {
        Map<Character, CoreCharset> charsetMap = Arrays.asList(Entities.ascii, Entities.utf, Entities.fallback).stream()
                .collect(Collectors.toMap(CoreCharset::getName, Function.identity()));

        for (Character character : Arrays.asList('a', '\u00E1')) { // 'á' is in extended set
            CoreCharset charset = charsetMap.get(Entities.CoreCharset.utf);
            assertTrue("Character '" + character + "' should be encodable by utf", Entities.canEncode(charset, character, Entities.CoreCharset.fallback));
        }

        charsetMap = Arrays.asList(Entities.ascii, Entities.utf).stream()
                .collect(Collectors.toMap(CoreCharset::getName, Function.identity()));

        for (Character character : Arrays.asList('a', '\u00E1')) { // 'á' is in extended set
            CoreCharset charset = charsetMap.get(Entities.CoreCharset.ascii);
            assertFalse("Character '" + character + "' should not be encodable by ascii", Entities.canEncode(charset, character, Entities.CoreCharset.fallback));
        }

        charsetMap = Collections.singletonMap(Entities.CoreCharset.fallback.getName(), Entities.CoreCharset.fallback);

        for (Character character : Arrays.asList('a', '\u00E1')) { // 'á' is in extended set
            CoreCharset charset = charsetMap.get(Entities.CoreCharset.fallback);
            assertTrue("Character '" + character + "' should be encodable by fallback", Entities.canEncode(charset, character, null));
        }
    }
}