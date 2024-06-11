package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("x"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;abc&amp;def&gt;g&quot;hi&apos;j", Entities.escape("abc<def>ghi\"j", null, Entities.EscapeMode.base));
        assertEquals("&lt;abc&amp;def&gt;g&quot;hi&apos;j", Entities.escape("abc<def>ghi\"j", null, Entities.EscapeMode.xhtml));
        assertEquals("&#60;abc&#62;def&#62;g&#34;hi&#39;j", Entities.escape("abc<def>ghi\"j", null, Entities.EscapeMode.extended));

        assertEquals("&lt;abc&amp;def&gt;g&quot;hi&apos;j", Entities.escape("abc<def>ghi\"j", StandardCharsets.UTF_8, Entities.EscapeMode.base));
        assertEquals("&#x3C;abc&#x3E;def&#x3C;g&#x22;hi&#x27;j", Entities.escape("abc<def>ghi\"j", StandardCharsets.UTF_8, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc<def>ghi\"j", Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j", false));
        assertEquals("abc<def>ghi\"j", Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j", true)); // strict
        assertEquals("abc<def>ghi\"j", Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j;", false));
        assertEquals("abc<def>ghi\"j", Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j;", true)); // strict

        assertEquals("abc<def>ghi\"j", Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j", false));
        assertNull(Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j;x", false));
        assertNull(Entities.unescape("&lt;abc&amp;def&gt;g&quot;hi&apos;j;x", true)); // strict
    }

    @Test
    public void testLoadEntities() throws IOException {
        Properties entities = new Properties();
        entities.setProperty("lt", "23");
        InputStream is = new ByteArrayInputStream(("entities=" + entities.toString()).getBytes(StandardCharsets.UTF_8));
        Map<String, Character> entitiesMap = Entities.loadEntities("test.properties");
        Character lt = entitiesMap.get("lt");
        assertEquals((char) 23, lt.charValue());
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntitiesFile() {
        Entities.loadEntities("non-existent.properties");
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<>();
        entities.put("lt", '<');
        entities.put("amp", '&');
        Map<Character, String> outMap = Entities.toCharacterKey(entities);
        assertEquals("<", outMap.get('<').toString());
        assertEquals("&", outMap.get('&').toString());
    }
}