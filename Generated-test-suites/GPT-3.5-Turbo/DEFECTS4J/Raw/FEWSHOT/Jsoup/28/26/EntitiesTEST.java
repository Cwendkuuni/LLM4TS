package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.xhtml));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml));
        assertEquals("hello &lt; world", Entities.escape("hello < world", Document.OutputSettings.xhtml));
        assertEquals("hello &gt; world", Entities.escape("hello > world", Document.OutputSettings.xhtml));
        assertEquals("hello &amp; world", Entities.escape("hello & world", Document.OutputSettings.xhtml));
        assertEquals("hello &apos; world", Entities.escape("hello ' world", Document.OutputSettings.xhtml));
        assertEquals("hello &quot; world", Entities.escape("hello \" world", Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("hello < world", Entities.unescape("hello &lt; world"));
        assertEquals("hello > world", Entities.unescape("hello &gt; world"));
        assertEquals("hello & world", Entities.unescape("hello &amp; world"));
        assertEquals("hello ' world", Entities.unescape("hello &apos; world"));
        assertEquals("hello \" world", Entities.unescape("hello &quot; world"));
    }

}