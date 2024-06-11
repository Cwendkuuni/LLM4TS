package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    /**
     * Test method for {@link Entities#isNamedEntity(String)}
     */
    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertFalse(entities.isNamedEntity("rt"));
    }

    /**
     * Test method for {@link Entities#isBaseNamedEntity(String)}
     */
    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("rt"));
    }

    /**
     * Test method for {@link Entities#getCharacterByName(String)}
     */
    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
    }

    /**
     * Test method for {@link Entities#escape(StringBuilder, String, Document.OutputSettings, boolean, boolean, boolean)}
     */
    @Test
    public void testEscape() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        Document.OutputSettings out = new Document.OutputSettings();
        entities.escape(accum, "&<>", out, false, false, false);

        assertEquals("&amp;&lt;&gt;", accum.toString());
    }

    /**
     * Test method for {@link Entities#escape(StringBuilder, String, Document.OutputSettings, boolean, boolean, boolean)}
     * with attribute context
     */
    @Test
    public void testEscapeWithAttributeContext() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        Document.OutputSettings out = new Document.OutputSettings();
        entities.escape(accum, "&<abcd>", out, true, false, false);

        assertEquals("&amp;&#x61;&#x62;&amp;&lt;&gt;", accum.toString());
    }

    /**
     * Test method for {@link Entities#escape(StringBuilder, String, Document.OutputSettings, boolean, boolean, boolean)}
     * with normalization of whitespaces
     */
    @Test
    public void testEscapeWithNormalizedWhiteSpaces() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();

        Document.OutputSettings out = new Document.OutputSettings();
        entities.escape(accum, " &nbsp; &lt; &gt;  ", out, false, true, false);

        assertEquals("&nbsp;&nbsp; &amp;&#x2009;&#x2009; &lt;&gt;", accum.toString());
    }

    /**
     * Test method for {@link Entities#unescape(String)}
     */
    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        assertEquals("&lt; &gt; &amp;", entities.unescape("&lt; &gt; &amp;"));
    }

    /**
     * Test method for {@link Entities#unescape(String, boolean)}
     */
    @Test
    public void testUnescapeWithStrict() {
        Entities entities = new Entities();
        assertEquals("&lt; &gt; &amp;", entities.unescape("&lt; &gt; &amp;"));

        assertEquals("&lt; &gt; &amp;", entities.unescape("&lt; &gt; &amp; &amp;", true));
        assertEquals("&lt;&lt; &gt;&gt; &amp;&amp;", entities.unescape("&lt;&lt; &gt;&gt; &amp;&amp;", true));
    }

    /**
     * Test method for {@link Entities#canEncode(CoreCharset, char, CharsetEncoder)}
     */
    @Test
    public void testCanEncode() {
        Entities entities = new Entities();

        CoreCharset charset = CoreCharset.ascii;
        assertTrue(entities.canEncode(charset, 'A'));
        assertTrue(entities.canEncode(charset, 'a'));
        assertTrue(entities.canEncode(charset, '&'));
        assertFalse(entities.canEncode(charset, '€'));

        charset = CoreCharset.utf;
        assertTrue(entities.canEncode(charset, 'A'));
        assertTrue(entities.canEncode(charset, 'a'));
        assertTrue(entities.canEncode(charset, '€'));
    }
}