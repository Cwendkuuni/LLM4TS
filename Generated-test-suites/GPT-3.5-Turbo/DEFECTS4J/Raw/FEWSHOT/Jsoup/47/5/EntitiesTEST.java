package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.BeforeClass;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals("foo &amp; bar", Entities.escape("foo & bar", out));
        assertEquals("foo&nbsp;bar", Entities.escape("foo\u00A0bar", out));
        assertEquals("foo &lt; bar", Entities.escape("foo < bar", out));
        assertEquals("foo &gt; bar", Entities.escape("foo > bar", out));
        assertEquals("foo&quot;bar&quot;", Entities.escape("foo\"bar\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("foo & bar", Entities.unescape("foo &amp; bar"));
        assertEquals("foo < bar", Entities.unescape("foo &lt; bar"));
        assertEquals("foo > bar", Entities.unescape("foo &gt; bar"));
        assertEquals("foo\"bar\"", Entities.unescape("foo&quot;bar&quot;"));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("foo &amp; bar", Entities.unescape("foo &amp; bar", true));
        assertEquals("foo < bar", Entities.unescape("foo &lt; bar", true));
        assertEquals("foo > bar", Entities.unescape("foo &gt; bar", true));
        assertEquals("foo&quot;bar&quot;", Entities.unescape("foo&quot;bar&quot;", true));
    }

}