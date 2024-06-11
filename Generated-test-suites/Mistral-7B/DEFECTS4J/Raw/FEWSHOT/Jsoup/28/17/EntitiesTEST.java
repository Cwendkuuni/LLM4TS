package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        Map<Character, String> baseEntities = Entities.baseByVal;

        // Test known named entities
        for (Character entity : fullEntities.values()) {
            assertEquals(entity, Entities.getCharacterByName(String.valueOf(entity)));
            assertTrue(Entities.isNamedEntity(String.valueOf(entity)));
        }

        // Test known named entities in base set
        for (Character entity : xhtmlEntities.keySet()) {
            assertTrue(Entities.isNamedEntity(String.valueOf(entity)));
            assertEquals(entity, Entities.getCharacterByName(String.valueOf(entity)));
        }

        // Test unknown named entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("xyz"));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity("abcdef"));
        assertFalse(Entities.isNamedEntity("\u0001"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String testString = "This is a &test& with &lt;&gt; tags, and &amp; &apos; and '&quot; entities.";
        String xhtmlEscapedString = Entities.escape(testString, Entities.EscapeMode.xhtml, StandardCharsets.UTF_8.newEncoder());
        String baseEscapedString = Entities.escape(testString, Entities.EscapeMode.base, StandardCharsets.UTF_8.newEncoder());
        String extendedEscapedString = Entities.escape(testString, Entities.EscapeMode.extended, StandardCharsets.UTF_8.newEncoder());

        // Test XHTML-escaped string
        assertEquals("This is a &amp;&amp;test&amp; &amp;lt;&amp;gt; tags, and &amp;amp; &amp;apos; and &amp;quot; entities.", xhtmlEscapedString);

        // Test base-escaped string
        assertEquals("This is a &amp;lt;test&amp;gt; with &amp;lt;lt&amp;gt;&amp;gt; tags, and &amp;amp; &amp;apos; and &amp;quot; entities.", baseEscapedString);

        // Test extended-escaped string
        assertEquals("This is a &#x26;amp;&#xA0;test&#x26;gt; with &#x26;lt;lt&#x26;gt;&#xA0;tags, and &#x26;amp; &#x26;apos; and &#x26;quot; entities.", extendedEscapedString);

        // Test unescaping XHTML-escaped string
        assertEquals(testString, Entities.unescape(xhtmlEscapedString, true));

        // Test unescaping base-escaped string
        assertEquals(testString, Entities.unescape(baseEscapedString, true));

        // Test unescaping extended-escaped string
        assertEquals(testString, Entities.unescape(extendedEscapedString, true));

        // Test unescaping invalidly-escaped string
        String invalidlyEscapedString = "This is a &amp;amp;test&amp; &amp;lt;&amp;gt; tags, and &amp;amp; &apos; and &amp;quot; entities.";
        assertEquals(testString, Entities.unescape(invalidlyEscapedString, false));
    }

    @Test
    public void testLoadEntities() {
        // Test loading base entities
        Map<String, Character> baseEntities = Entities.loadEntities("entities-base.properties");
        Map<Character, String> baseEntitiesByVal = Entities.toCharacterKey(baseEntities);

        for (Object[] entity : Entities.xhtmlArray) {
            Character c = Character.valueOf((char) ((Integer) entity[1]).intValue());
            assertEquals(entity[0], baseEntities.get(String.valueOf(entity[0])));
            assertEquals(c, baseEntitiesByVal.get(c));
        }

        // Test loading full entities
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        Map<Character, String> fullEntitiesByVal = Entities.toCharacterKey(fullEntities);

        for (Character entity : fullEntities.values()) {
            String name = Entities.getCharacterByName(String.valueOf(entity));
            assertEquals(name, fullEntities.get(name));
            assertEquals(entity, fullEntitiesByVal.get(entity));
        }

        // Test loading non-existent file
        try {
            Entities.loadEntities("non-existent.properties");
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }
    }

    @Test
    public void testIsNamedEntityWithInvalidInput() {
        // Test empty string
        assertFalse(Entities.isNamedEntity(""));

        // Test null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testGetCharacterByNameWithInvalidInput() {
        // Test null input
        assertNull(Entities.getCharacterByName(null));

        // Test invalid character
        assertNull(Entities.getCharacterByName("\u0001"));
    }

    @Test
    public void testEscapeModeEnums() {
        assertEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.valueOf("xhtml"));
        assertEquals(Entities.EscapeMode.base, Entities.EscapeMode.valueOf("base"));
        assertEquals(Entities.EscapeMode.extended, Entities.EscapeMode.valueOf("extended"));
    }

    @Test
    public void testUnescapePattern() {
        String testString = "This is a &test& with &lt;&gt; tags, and &amp; &apos; and '&quot; entities.";
        String escapedString = Entities.escape(testString, Entities.EscapeMode.extended, StandardCharsets.UTF_8.newEncoder());

        Pattern unescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);?");
        Matcher unescapeMatcher = unescapePattern.matcher(escapedString);

        // Test unescaping named entities
        int position = 0;
        while (unescapeMatcher.find()) {
            String name = unescapeMatcher.group(1);
            Character character = Entities.getCharacterByName(name);
            assertEquals(character, unescapeMatcher.charAt(position));
            position += unescapeMatcher.end();
        }

        // Test unescaping character references
        assertEquals(testString, unescapeMatcher.replaceAll("\\u$1"));
    }

    @Test
    public void testStrictUnescapePattern() {
        String testString = "This is a &test& with &lt;&gt; tags, and &amp; &apos; and '&quot; entities.";
        String escapedString = Entities.escape(testString, Entities.EscapeMode.extended, StandardCharsets.UTF_8.newEncoder());

        Pattern strictUnescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);");
        Matcher strictUnescapeMatcher = strictUnescapePattern.matcher(escapedString);

        // Test unescaping named entities
        int position = 0;
        while (strictUnescapeMatcher.find()) {
            String name = strictUnescapeMatcher.group(1);
            Character character = Entities.getCharacterByName(name);
            assertEquals(character, strictUnescapeMatcher.charAt(position));
            position += strictUnescapeMatcher.end();
        }

        // Test unescaping character references
        assertFalse(strictUnescapeMatcher.matches(""));

        // Test invalidly-escaped string
        String invalidlyEscapedString = "This is a &amp;amp;test&amp; &amp;lt;&amp;gt; tags, and &amp;amp; &apos; and &amp;quot; entities.";
        strictUnescapeMatcher = strictUnescapePattern.matcher(invalidlyEscapedString);
        assertFalse(strictUnescapeMatcher.matches(""));

        // Test unescaping invalid character references
        assertFalse(strictUnescapeMatcher.matches("This is a &invalid;test"));
    }

}